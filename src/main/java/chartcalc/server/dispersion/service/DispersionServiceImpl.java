package chartcalc.server.dispersion.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import chartcalc.server.data.model.Data;
import chartcalc.server.data.repository.DataRepository;
import chartcalc.server.dispersion.dto.DispersionRequestDto;
import chartcalc.server.dispersion.dto.DispersionResponseDto;
import chartcalc.server.dispersion.exceptions.DataSourceFormatException;
import chartcalc.server.dispersion.exceptions.SymbolNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DispersionServiceImpl implements DispersionService {
	final DataRepository dataRepository;
	
	final WebClient client;

	final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public DispersionResponseDto calculate(DispersionRequestDto request) {
		String symbol = request.getSymbol();

		Data data = dataRepository.findById(symbol)
				.filter(d -> d.getDate().isAfter(request.getTo()))
				.orElse(Data.builder()
					.symbol(symbol)
					.json(client
							.get()
							.uri(b -> b
									.path("quote/" + request.getSymbol() + "/historical")
									.queryParam("assetclass", "index")
									.queryParam("fromdate", request.getFrom().format(formatter))
									.queryParam("todate", request.getTo().format(formatter))
									.queryParam("limit", "999999999")
									.build())
							.retrieve()
							.bodyToMono(String.class)
							.block())
					.date(LocalDate.now())
					.build());

		dataRepository.save(data);

		ObjectMapper mapper = new ObjectMapper();

		JsonNode rowNodes;

		try {
			rowNodes = mapper.readTree(data.getJson())
					.get("data")
					.get("tradesTable")
					.get("rows");
		} catch (NullPointerException e) {
			throw new SymbolNotFoundException();
		} catch (JsonProcessingException e) {
			throw new DataSourceFormatException();
		}

		int iterations = rowNodes.size() - request.getInterval();

		double sum = 0, averageReturn = 0;

		for (int i = 0; i < iterations; i++) {
			double left = Double.parseDouble(rowNodes.get(i)
					.get("close").asText().replace(",", ""));

			double right = Double.parseDouble(rowNodes.get(i + request.getInterval())
					.get("close").asText().replace(",", ""));

			sum += right - left;
		}
		
		averageReturn = sum / iterations;

		return DispersionResponseDto.builder()
				.symbol(request.getSymbol())
				.from(request.getFrom())
				.to(request.getTo())
				.interval(request.getInterval())
				.averageReturn(averageReturn)
				.build();
	}
}
