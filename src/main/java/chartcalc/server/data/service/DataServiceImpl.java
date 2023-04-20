package chartcalc.server.data.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import chartcalc.server.data.model.Data;
import chartcalc.server.data.repository.DataRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {
	final DataRepository dataRepository;
	
	final WebClient client;
	
	final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public Iterable<String> listSymbols() {
		return dataRepository.findSymbols();
	}

	@Override
	public String requestJson(Data data) {
		return client
				.get()
				.uri(b -> b
						.path("quote/" + data.getSymbol() + "/historical")
						.queryParam("assetclass", data.getAssetClass())
						.queryParam("fromdate", "1970-01-01")
						.queryParam("todate", LocalDate.now().format(formatter))
						.queryParam("limit", "999999999")
						.build())
				.retrieve()
				.bodyToMono(String.class)
				.block();
	}
}
