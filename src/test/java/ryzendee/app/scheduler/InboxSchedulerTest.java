package ryzendee.app.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ryzendee.app.common.enums.ProcessingStatus;
import ryzendee.app.models.Inbox;
import ryzendee.app.scheduler.processor.InboxProcessor;
import ryzendee.app.repository.InboxRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InboxSchedulerTest {

    private InboxScheduler inboxScheduler;
    @Mock
    private InboxRepository inboxRepository;
    @Mock
    private InboxProcessor inboxProcessor;

    private Inbox inbox;
    private List<Inbox> inboxList;

    @BeforeEach
    void setUp() {
        inboxScheduler = new InboxScheduler(inboxRepository, inboxProcessor, 50);
        inbox = Inbox.builder().build();
        inboxList = new ArrayList<>();
        inboxList.add(inbox);
    }

    @Test
    void process_successProcessing_shouldSaveInboxWithProcessedStatus() {
        when(inboxRepository.findAllByStatus(eq(ProcessingStatus.PENDING), any(Pageable.class)))
                .thenReturn(inboxList);

        inboxScheduler.process();

        assertThat(inbox.getStatus()).isEqualTo(ProcessingStatus.PROCESSED);
        verify(inboxRepository).findAllByStatus(eq(ProcessingStatus.PENDING), any(Pageable.class));
        verify(inboxProcessor).process(inbox);
        verify(inboxRepository).saveAll(inboxList);
    }

    @Test
    void process_processingFailed_shouldSaveInboxWithFailedStatus() {
        when(inboxRepository.findAllByStatus(eq(ProcessingStatus.PENDING), any(Pageable.class)))
                .thenReturn(inboxList);
        doThrow(RuntimeException.class)
                .when(inboxProcessor).process(inbox);

        inboxScheduler.process();

        assertThat(inbox.getStatus()).isEqualTo(ProcessingStatus.FAILED);
        verify(inboxRepository).findAllByStatus(eq(ProcessingStatus.PENDING), any(Pageable.class));
        verify(inboxProcessor).process(inbox);
        verify(inboxRepository).saveAll(inboxList);
    }
}
