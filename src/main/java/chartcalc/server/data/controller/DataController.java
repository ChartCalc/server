package chartcalc.server.data.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chartcalc.server.data.service.DataService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/data")
@RequiredArgsConstructor
@CrossOrigin
public class DataController {
	final DataService dataService;

	@GetMapping
	public Iterable<String> listSymbols() {
		return dataService.listSymbols();
	}
}
