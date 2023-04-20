package chartcalc.server.data.service;

import chartcalc.server.data.model.Data;

public interface DataService {
	Iterable<String> listSymbols();

	String requestJson(Data data);
}
