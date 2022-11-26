package xyz.nedderhoff.citytweets.job;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.service.RepostService;

import java.util.List;

@Component
@EnableScheduling
public class RepostJob {
    private static final int FETCHING_RATE = 1000 * 60 * 5;

    private final List<RepostService> repostServices;

    public RepostJob(List<RepostService> repostServices) {
        this.repostServices = repostServices;
    }

    @Scheduled(fixedRate = FETCHING_RATE)
    public void run() {
        repostServices.forEach(RepostService::repost);
    }
}
