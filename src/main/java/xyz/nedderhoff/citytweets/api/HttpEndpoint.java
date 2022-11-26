package xyz.nedderhoff.citytweets.api;

import org.springframework.web.client.RestTemplate;

public abstract class HttpEndpoint{
    protected final RestTemplate rt;

    public HttpEndpoint(RestTemplate rt) {
        this.rt = rt;
    }
}
