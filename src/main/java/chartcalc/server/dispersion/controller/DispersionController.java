package chartcalc.server.dispersion.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chartcalc.server.dispersion.dto.DispersionRequestDto;
import chartcalc.server.dispersion.dto.DispersionResponseDto;
import chartcalc.server.dispersion.service.DispersionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/dispersion")
@RequiredArgsConstructor
public class DispersionController {
	final DispersionService dispersionService;

	@GetMapping
	public DispersionResponseDto calculate(DispersionRequestDto request) {
		return dispersionService.calculate(request);
	}
}
