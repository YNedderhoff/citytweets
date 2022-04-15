package xyz.nedderhoff.citytweets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import xyz.nedderhoff.citytweets.config.AccountProperties;

@SpringBootApplication
@EnableConfigurationProperties(AccountProperties.class)
public class CityTweetsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CityTweetsApplication.class, args);
	}

	@Bean
	public Twitter twitter(){
		return new TwitterFactory().getInstance();
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

}
