package ryzendee.app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ryzendee.app.config.props.RabbitProperties;

@EnableRabbit
@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
@RequiredArgsConstructor
public class RabbitConfig {

    private static final String DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
    private static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
    private static final String MESSAGE_TTL = "x-message-ttl";

    private final RabbitProperties rabbitProperties;

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Contractor
    @Bean
    public Queue contractorQueue() {
        return QueueBuilder.durable(rabbitProperties.getContractorQueue())
                .withArgument(DEAD_LETTER_EXCHANGE, rabbitProperties.getContractorDeadExchange())
                .withArgument(DEAD_LETTER_ROUTING_KEY, rabbitProperties.getContractorDeadRoutingKey())
                .build();
    }

    @Bean
    public DirectExchange contractorExchange() {
        return new DirectExchange(rabbitProperties.getContractorExchange());
    }

    @Bean
    public Binding contractorBinding() {
        return BindingBuilder.bind(contractorQueue())
                .to(contractorExchange())
                .with(rabbitProperties.getContractorRoutingKey());
    }

    // DLQ
    @Bean
    public Queue contractorDeadQueue() {
        return QueueBuilder.durable(rabbitProperties.getContractorDeadQueue())
                .withArgument(MESSAGE_TTL, rabbitProperties.getContractorDeadTtl())
                .withArgument(DEAD_LETTER_EXCHANGE, rabbitProperties.getContractorDeadExchange())
                .withArgument(DEAD_LETTER_ROUTING_KEY, rabbitProperties.getContractorDeadRoutingKey())
                .build();
    }

    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(rabbitProperties.getContractorDeadExchange());
    }

    @Bean
    public Binding deadBinding() {
        return BindingBuilder.bind(contractorDeadQueue())
                .to(deadExchange())
                .with(rabbitProperties.getContractorDeadRoutingKey());
    }

    // Retry
    @Bean
    public DirectExchange retryExchange() {
        return new DirectExchange(rabbitProperties.getContractorRetryExchange());
    }

    @Bean
    public Binding retryBinding() {
        return BindingBuilder.bind(contractorQueue())
                .to(retryExchange())
                .with(rabbitProperties.getContractorRetryRoutingKey());
    }
}
