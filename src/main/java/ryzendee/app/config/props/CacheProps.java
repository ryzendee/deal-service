package ryzendee.app.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@ConfigurationProperties(prefix = "cache")
@Component
@Data
public class CacheProps {

    private CacheSettings deals;
    private CacheSettings dealMetadata;

    @Data
    public static class CacheSettings {
        private Duration ttl;
    }
}
