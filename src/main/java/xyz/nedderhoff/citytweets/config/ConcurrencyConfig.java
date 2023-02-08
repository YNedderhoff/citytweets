package xyz.nedderhoff.citytweets.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.nedderhoff.citytweets.service.RepostService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ConcurrencyConfig {
    private static final Logger logger = LoggerFactory.getLogger(ConcurrencyConfig.class);

    @Bean
    public ExecutorService repostJobExecutorService(List<RepostService> repostServices) {
        final int size = repostServices.size();
        logger.info("Creating repostJobExecutorService with size {}", size);
        return Executors.newFixedThreadPool(size);
    }
}
