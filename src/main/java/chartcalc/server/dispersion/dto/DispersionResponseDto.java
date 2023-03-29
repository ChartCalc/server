package chartcalc.server.dispersion.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DispersionResponseDto {
	String symbol;

	LocalDate from;

	LocalDate to;

	Integer interval;

	Double averageReturn;
}
