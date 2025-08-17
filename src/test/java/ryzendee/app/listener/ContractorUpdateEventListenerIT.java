package ryzendee.app.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import ryzendee.app.AbstractTestcontainers;
import ryzendee.app.common.dto.ContractorUpdateEvent;
import ryzendee.app.config.RabbitConfig;
import ryzendee.app.config.props.RabbitProperties;
import ryzendee.app.models.Inbox;
import ryzendee.app.repository.InboxRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static ryzendee.app.testutils.FixtureUtil.contractorUpdateEventFixture;

@DirtiesContext
@SpringBootTest(
        properties = "rabbit.contractor.dead-ttl=2000",
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {ContractorUpdateEventListener.class, RabbitAutoConfiguration.class, RabbitConfig.class}
)
public class ContractorUpdateEventListenerIT extends AbstractTestcontainers {

    private static final int TIMEOUT_MS = 10000;

    @MockitoSpyBean
    private ContractorUpdateEventListener contractorUpdateEventListener;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitProperties rabbitProperties;
    @MockitoBean
    private InboxRepository inboxRepository;
    @MockitoBean
    private ObjectMapper objectMapper;

    private ContractorUpdateEvent contractorUpdateEvent;

    @BeforeAll
    static void startContainer() {
        rabbitMQContainer.start();
    }

    @BeforeEach
    void setUp() {
        contractorUpdateEvent = contractorUpdateEventFixture();
    }

    @Test
    void handle_processSuccess_shouldCreateInbox() throws JsonProcessingException {
        // Arrange
        when(objectMapper.writeValueAsString(contractorUpdateEvent)).thenReturn("{}");
        ArgumentCaptor<ContractorUpdateEvent> eventCaptor
                = ArgumentCaptor.forClass(ContractorUpdateEvent.class);

        // Act
        convertAndSendEventToContractorExchange();
        verify(contractorUpdateEventListener, timeout(TIMEOUT_MS)).handle(eventCaptor.capture());

        // Assert
        ContractorUpdateEvent handledEvent = eventCaptor.getValue();
        assertThat(handledEvent).isEqualTo(contractorUpdateEvent);

        verify(objectMapper).writeValueAsString(handledEvent);
        verify(inboxRepository).saveAndFlush(any(Inbox.class));
    }

    @Test
    void handle_messageAlreadyProcessed_shouldSkip() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(contractorUpdateEvent)).thenReturn("{}");
        doThrow(DataIntegrityViolationException.class)
                .when(inboxRepository).saveAndFlush(any(Inbox.class));

        convertAndSendEventToContractorExchange();
        verify(contractorUpdateEventListener, timeout(TIMEOUT_MS)).handle(contractorUpdateEvent);

        verify(inboxRepository, timeout(TIMEOUT_MS)).saveAndFlush(any(Inbox.class));
    }

    @Test
    void handle_processFailed_shouldSendIntoDlq() throws JsonProcessingException {
        doThrow(JsonProcessingException.class)
                .when(objectMapper).writeValueAsString(contractorUpdateEvent);

        convertAndSendEventToContractorExchange();

        assertThatEventWasInDeadLetterQueue();
    }

    @Test
    void handle_processFailed_shouldRetry() throws JsonProcessingException {
        // Arrange
        doThrow(JsonProcessingException.class)
                .when(objectMapper).writeValueAsString(contractorUpdateEvent);

        // Act
        convertAndSendEventToContractorExchange();

        // Assert
        assertThatEventWasInDeadLetterQueue();

        verify(contractorUpdateEventListener, timeout(TIMEOUT_MS + rabbitProperties.getContractorDeadTtl()))
                .handle(contractorUpdateEvent);
    }

    private void assertThatEventWasInDeadLetterQueue() {
        Object dlqMsg = rabbitTemplate.receiveAndConvert(rabbitProperties.getContractorDeadQueue(), TIMEOUT_MS);
        assertThat(dlqMsg).isInstanceOf(ContractorUpdateEvent.class);
        assertThat((ContractorUpdateEvent) dlqMsg).isEqualTo(contractorUpdateEvent);
    }

    private void convertAndSendEventToContractorExchange() {
        rabbitTemplate.convertAndSend(
                rabbitProperties.getContractorExchange(),
                rabbitProperties.getContractorRoutingKey(),
                contractorUpdateEvent
        );
    }
}
