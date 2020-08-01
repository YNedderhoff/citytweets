package xyz.nedderhoff.citytweets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;

@SpringBootApplication
public class CityTweetsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CityTweetsApplication.class, args);
	}

	@Bean
	public Twitter twitter(){
		return new TwitterFactory().getInstance();
	}

}
