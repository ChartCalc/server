package chartcalc.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ServerConfiguration {
	@Bean
	public WebClient getWebClient() {
		return WebClient.builder()
				.baseUrl("https://api.nasdaq.com/api/")
				.exchangeStrategies(ExchangeStrategies.builder()
						.codecs(c -> c
								.defaultCodecs()
								.maxInMemorySize(10 * 1024 * 1024))
						.build())
				.build();
	}
}
