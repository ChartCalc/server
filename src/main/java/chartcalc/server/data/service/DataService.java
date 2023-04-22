package chartcalc.server.data.service;

import chartcalc.server.data.model.Data;

public interface DataService {
	Iterable<String> listSymbols();

	String requestQuoteJson(Data data, int limit, int offset);
	
	String requestScreenerJson(String assetClass, int limit, int offset);
}
