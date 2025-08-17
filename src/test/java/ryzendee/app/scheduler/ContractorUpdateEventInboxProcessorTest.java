package ryzendee.app.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ryzendee.app.common.dto.ContractorUpdateEvent;
import ryzendee.app.common.exception.MappingException;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.scheduler.processor.ContractorUpdateEventInboxProcessor;
import ryzendee.app.models.Inbox;
import ryzendee.app.models.DealContractor;
import ryzendee.app.repository.DealContractorRepository;

import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static ryzendee.app.testutils.FixtureUtil.contractorUpdateEventFixture;

@ExtendWith(MockitoExtension.class)
public class ContractorUpdateEventInboxProcessorTest {

    @InjectMocks
    private ContractorUpdateEventInboxProcessor processor;

    @Mock
    private DealContractorRepository repository;

    @Mock
    private ObjectMapper mapper;

    private Inbox inbox;
    private DealContractor dealContractor;
    private ContractorUpdateEvent contractorUpdateEvent;

    @BeforeEach
    void setUp() {
        inbox = Inbox.builder().build();
        dealContractor = DealContractor.builder().build();
        contractorUpdateEvent = contractorUpdateEventFixture();
    }

    @Test
    void process_existsContractor_shouldUpdateWithDataFromEvent() throws JsonProcessingException {
        when(mapper.readValue(inbox.getPayload(), ContractorUpdateEvent.class))
                .thenReturn(contractorUpdateEvent);
        when(repository.findById(fromString(contractorUpdateEvent.getContractorId())))
                .thenReturn(Optional.of(dealContractor));

        processor.process(inbox);

        assertThat(dealContractor.getName()).isEqualTo(contractorUpdateEvent.getName());
        assertThat(dealContractor.getInn()).isEqualTo(contractorUpdateEvent.getInn());
        verify(mapper).readValue(inbox.getPayload(), ContractorUpdateEvent.class);
        verify(repository).findById(fromString(contractorUpdateEvent.getContractorId()));
        verify(repository).save(dealContractor);
    }

    @Test
    void process_nonActualEventData_shouldDoNotUpdate() throws JsonProcessingException {
        dealContractor.setModifyDate(now().plusDays(1));
        when(mapper.readValue(inbox.getPayload(), ContractorUpdateEvent.class))
                .thenReturn(contractorUpdateEvent);
        when(repository.findById(fromString(contractorUpdateEvent.getContractorId())))
                .thenReturn(Optional.of(dealContractor));

        processor.process(inbox);

        verify(mapper).readValue(inbox.getPayload(), ContractorUpdateEvent.class);
        verify(repository).findById(fromString(contractorUpdateEvent.getContractorId()));
        verify(repository, never()).save(dealContractor);
    }

    @Test
    void process_nonExistsContractor_shouldThrowEx() throws JsonProcessingException {
        when(mapper.readValue(inbox.getPayload(), ContractorUpdateEvent.class))
                .thenReturn(contractorUpdateEvent);
        when(repository.findById(fromString(contractorUpdateEvent.getContractorId())))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> processor.process(inbox))
                .isInstanceOf(ResourceNotFoundException.class)
                .message().isNotBlank();

        verify(mapper).readValue(inbox.getPayload(), ContractorUpdateEvent.class);
        verify(repository).findById(fromString(contractorUpdateEvent.getContractorId()));
        verify(repository, never()).save(dealContractor);
    }

    @Test
    void process_invalidMapping_shouldThrowEx() throws JsonProcessingException {
        doThrow(JsonProcessingException.class)
                .when(mapper).readValue(inbox.getPayload(), ContractorUpdateEvent.class);

        assertThatThrownBy(() -> processor.process(inbox))
                .isInstanceOf(MappingException.class)
                .message().isNotBlank();

        verify(mapper).readValue(inbox.getPayload(), ContractorUpdateEvent.class);
    }
}
