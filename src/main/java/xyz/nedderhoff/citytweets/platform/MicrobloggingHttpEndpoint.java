package xyz.nedderhoff.citytweets.platform;

import org.springframework.web.client.RestTemplate;

public abstract class MicrobloggingHttpEndpoint {
    protected final RestTemplate rt;

    public MicrobloggingHttpEndpoint(RestTemplate rt) {
        this.rt = rt;
    }
}
