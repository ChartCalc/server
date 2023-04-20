package chartcalc.server.dispersion.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DispersionResponseDto {
	String symbol;

	String assetClass;

	LocalDate from;

	LocalDate to;

	Integer interval;

	Double averageReturn;
}
