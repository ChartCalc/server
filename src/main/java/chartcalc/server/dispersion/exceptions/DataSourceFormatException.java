package chartcalc.server.dispersion.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DataSourceFormatException extends RuntimeException {
	private static final long serialVersionUID = -1823968088149739895L;
}
