package xyz.nedderhoff.citytweets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.nedderhoff.citytweets.service.RepostService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ConcurrencyConfig {

    @Bean
    public ExecutorService repostJobExecutorService(List<RepostService> repostServices) {
        return Executors.newFixedThreadPool(repostServices.size());
    }
}
