package xyz.nedderhoff.citytweets;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(
        properties = {
                "bearerToken=fakeBearerToken"
        }
)
class CityTweetsApplicationTests {

    @Test
    void contextLoads() {
        assert(true);
    }

}
