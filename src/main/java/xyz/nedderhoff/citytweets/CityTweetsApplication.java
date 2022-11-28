package xyz.nedderhoff.citytweets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.config.AccountProperties;

@SpringBootApplication
@EnableConfigurationProperties(AccountProperties.class)
public class CityTweetsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CityTweetsApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

}
