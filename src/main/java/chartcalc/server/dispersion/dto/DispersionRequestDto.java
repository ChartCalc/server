package chartcalc.server.dispersion.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispersionRequestDto {
	String symbol;

	LocalDate from;

	LocalDate to;

	Integer interval;
}
