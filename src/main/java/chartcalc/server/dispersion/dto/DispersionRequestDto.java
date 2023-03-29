package chartcalc.server.dispersion.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DispersionRequestDto {
	String symbol;

	LocalDate from;

	LocalDate to;

	Integer interval;
}
