package chartcalc.server.runner;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import chartcalc.server.data.model.Data;
import chartcalc.server.data.repository.DataRepository;
import chartcalc.server.data.service.DataService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ServerRunner implements CommandLineRunner {
	final DataRepository dataRepository;

	final DataService dataService;

	final ObjectMapper mapper = new ObjectMapper();

	@Override
	public void run(String... args) throws Exception {
		if (dataRepository.count() != 0) {
			return;
		}

		fillStockSymbols();

		fillIndexSymbols();
	}

	void fillStockSymbols() {
		JsonNode rowNodes;

		try {
			String json = dataService.requestScreenerJson("stocks", 999999999, 0);

			rowNodes = mapper.readTree(json)
					.get("data")
					.get("table")
					.get("rows");
		} catch (Exception e) {
			return;
		}

		List<Data> dataList = new ArrayList<>();

		rowNodes.forEach(node -> {
			Data data = Data.builder()
					.symbol(node.get("symbol").asText())
					.assetClass("stocks")
					.build();

			dataList.add(data);
		});
		
		dataRepository.saveAll(dataList);
	}

	void fillIndexSymbols() {
		List<Data> dataList = new ArrayList<>();

		final int step = 50;

		for (int i = 0; i <= 500; i += step) {
			JsonNode rowNodes;

			try {
				String json = dataService.requestScreenerJson("index", step, i);

				rowNodes = mapper.readTree(json)
						.get("data")
						.get("records")
						.get("data")
						.get("rows");
			} catch (Exception e) {
				break;
			}

			rowNodes.forEach(node -> {
				Data data = Data.builder()
						.symbol(node.get("symbol").asText())
						.assetClass("index")
						.build();

				dataList.add(data);
			});
		}

		dataRepository.saveAll(dataList);
	}
}
