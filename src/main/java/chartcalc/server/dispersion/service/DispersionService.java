package chartcalc.server.dispersion.service;

import chartcalc.server.dispersion.dto.DispersionRequestDto;
import chartcalc.server.dispersion.dto.DispersionResponseDto;

public interface DispersionService {
	DispersionResponseDto calculate(DispersionRequestDto request);
}
