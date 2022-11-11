package xyz.nedderhoff.citytweets.endpoint.platform;

import org.springframework.web.client.RestTemplate;

public abstract class MicrobloggingHttpEndpoint {
    protected final RestTemplate rt;

    public MicrobloggingHttpEndpoint(RestTemplate rt) {
        this.rt = rt;
    }
}
