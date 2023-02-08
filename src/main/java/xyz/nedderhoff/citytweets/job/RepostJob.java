package xyz.nedderhoff.citytweets.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.service.RepostService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

@Component
@EnableScheduling
public class RepostJob {
    private static final Logger logger = LoggerFactory.getLogger(RepostJob.class);
    private static final int FETCHING_RATE = 1000 * 60 * 5;

    private final List<RepostService> repostServices;
    private final ExecutorService repostJobExecutorService;

    public RepostJob(List<RepostService> repostServices, ExecutorService repostJobExecutorService) {
        this.repostServices = repostServices;
        this.repostJobExecutorService = repostJobExecutorService;
    }

    @Scheduled(fixedRate = FETCHING_RATE)
    public void run() throws ExecutionException, InterruptedException {
        logger.info("Running RepostJob scheduled job in thread {}", Thread.currentThread().getName());
        final List<CompletableFuture<Void>> completableFutures = repostServices.stream()
                .map(repostService -> CompletableFuture.runAsync(repostService::repost, repostJobExecutorService))
                .toList();

        final Void unused = CompletableFuture.allOf(completableFutures.toArray(CompletableFuture[]::new)).get();
        logger.info("Finished RepostJob scheduled job in thread {}, all jobs complete", Thread.currentThread().getName());

    }
}
