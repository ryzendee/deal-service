package ryzendee.app.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rabbit")
@Data
public class RabbitProperties {

    private Contractor contractor = new Contractor();

    public String getContractorQueue() { return contractor.getQueue(); }
    public String getContractorExchange() { return contractor.getExchange(); }
    public String getContractorRoutingKey() { return contractor.getRoutingKey(); }
    public String getContractorDeadQueue() { return contractor.getDead().getQueue(); }
    public String getContractorDeadExchange() { return contractor.getDead().getExchange(); }
    public String getContractorDeadRoutingKey() { return contractor.getDead().getRoutingKey(); }
    public String getContractorRetryExchange() { return contractor.getRetry().getExchange(); }
    public String getContractorRetryRoutingKey() { return contractor.getRetry().getRoutingKey(); }
    public int getContractorDeadTtl() { return contractor.getDeadTtl(); }

    @Data
    public static class Contractor {
        private String queue;
        private String exchange;
        private String routingKey;
        private Dead dead = new Dead();
        private Retry retry = new Retry();
        private int deadTtl;

        @Data
        public static class Dead {
            private String queue;
            private String exchange;
            private String routingKey;
        }

        @Data
        public static class Retry {
            private String exchange;
            private String routingKey;
        }
    }
}
