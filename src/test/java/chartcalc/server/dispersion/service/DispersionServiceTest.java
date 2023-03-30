package chartcalc.server.dispersion.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import chartcalc.server.dispersion.dto.DispersionRequestDto;
import chartcalc.server.dispersion.dto.DispersionResponseDto;

@SpringBootTest
class DispersionServiceTest {
	@Autowired
	DispersionService service;

	DispersionRequestDto request;

	@BeforeEach
	void setUp() throws Exception {
		request = DispersionRequestDto.builder()
				.symbol("SPX")
				.from(LocalDate.of(2013, 3, 29))
				.to(LocalDate.of(2023, 3, 29))
				.interval(365)
				.build();
	}

	@Test
	void testCalculate() {
		DispersionResponseDto response = service.calculate(request);

		assertEquals(response.getSymbol(), request.getSymbol());
		assertEquals(response.getFrom(), request.getFrom());
		assertEquals(response.getTo(), request.getTo());
		assertEquals(response.getInterval(), request.getInterval());
		assertNotNull(response.getAverageReturn());
	}
}
