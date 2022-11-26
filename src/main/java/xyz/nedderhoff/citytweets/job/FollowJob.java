package xyz.nedderhoff.citytweets.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.service.FollowService;

import java.util.List;

@Component
@EnableScheduling
public class FollowJob {
    private static final Logger logger = LoggerFactory.getLogger(FollowJob.class);
    private static final int FOLLOW_RATE = 1000 * 60 * 60 * 24;

    private final List<FollowService> followServices;

    public FollowJob(List<FollowService> followServices) {
        this.followServices = followServices;
    }

    @Scheduled(fixedRate = FOLLOW_RATE)
    public void run() {
        followServices.forEach(FollowService::follow);
    }
}
