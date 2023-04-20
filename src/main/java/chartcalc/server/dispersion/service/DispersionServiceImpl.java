package chartcalc.server.dispersion.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import chartcalc.server.data.model.Data;
import chartcalc.server.data.repository.DataRepository;
import chartcalc.server.data.service.DataService;
import chartcalc.server.dispersion.dto.DispersionRequestDto;
import chartcalc.server.dispersion.dto.DispersionResponseDto;
import chartcalc.server.dispersion.exceptions.DataSourceFormatException;
import chartcalc.server.dispersion.exceptions.SymbolNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DispersionServiceImpl implements DispersionService {
	final DataRepository dataRepository;
	
	final DataService dataService;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	@Override
	public DispersionResponseDto calculate(DispersionRequestDto request) {
		String symbol = request.getSymbol();

		Data data = dataRepository.findById(symbol)
				.filter(d -> d.getJson() != null)
				.filter(d -> d.getDate().isAfter(request.getTo()))
				.orElse(Data.builder()
					.symbol(symbol)
					.assetClass("index")
					.date(LocalDate.now())
					.build());

		data.setJson(dataService.requestJson(data));

		ObjectMapper mapper = new ObjectMapper();

		JsonNode rowNodes;

		try {
			rowNodes = mapper.readTree(data.getJson())
					.get("data")
					.get("tradesTable")
					.get("rows");
		} catch (NullPointerException e) {
			try {
				data.setAssetClass("stocks");
				data.setJson(dataService.requestJson(data));
				
				rowNodes = mapper.readTree(data.getJson())
						.get("data")
						.get("tradesTable")
						.get("rows");
			} catch (NullPointerException ee) {
				throw new SymbolNotFoundException();
			} catch (JsonProcessingException ee) {
				throw new DataSourceFormatException();
			}
		} catch (JsonProcessingException e) {
			throw new DataSourceFormatException();
		}

		dataRepository.save(data);

		int counter = 0;

		int iterations = rowNodes.size() - request.getInterval();

		double sum = 0, averageReturn = 0;

		for (int i = 0; i < iterations; i++) {
			LocalDate date = LocalDate.parse(rowNodes.get(i).get("date").asText(), formatter);

			if (date.isAfter(request.getTo())) {
				continue;
			}
			else if (date.isBefore(request.getFrom())) {
				break;
			}

			double right = Double.parseDouble(rowNodes.get(i)
					.get("close").asText().replace("$", "").replace(",", ""));

			double left = Double.parseDouble(rowNodes.get(i + request.getInterval())
					.get("close").asText().replace("$", "").replace(",", ""));

			sum += (right / left - 1) * 100;
			counter++;
		}

		averageReturn = sum / counter;

		return DispersionResponseDto.builder()
				.symbol(request.getSymbol())
				.assetClass(data.getAssetClass())
				.from(request.getFrom())
				.to(request.getTo())
				.interval(request.getInterval())
				.averageReturn(averageReturn)
				.build();
	}
}
