package ryzendee.app.scheduler;


import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ryzendee.app.common.enums.ProcessingStatus;
import ryzendee.app.models.Inbox;
import ryzendee.app.scheduler.processor.InboxProcessor;
import ryzendee.app.repository.InboxRepository;

import java.util.List;

/**
 * Шедулер для периодической обработки сообщений из {@link Inbox}.
 * Находит сообщения со статусом {@link ProcessingStatus#PENDING},
 * обрабатывает через {@link InboxProcessor} и обновляет их статус.
 *
 * @author Dmitry Ryazantsev
 */
@Component
@Slf4j
public class InboxScheduler {

    private static final String SORT_BY_CREATED_AT = "createdAt";

    private final InboxRepository inboxRepository;
    private final InboxProcessor inboxProcessor;
    private final int batchSize;

    public InboxScheduler(InboxRepository inboxRepository,
                          InboxProcessor inboxProcessor,
                          @Value("${inbox.scheduler.batch-size:50}") int batchSize) {
        this.inboxRepository = inboxRepository;
        this.inboxProcessor = inboxProcessor;
        this.batchSize = batchSize;
    }

    /**
     * Выполняет обработку сообщений из {@link Inbox} с ограничением batchSize.
     * Сортировка выполняется по полю {@code createdAt}.
     * Статус сообщений обновляется на {@link ProcessingStatus#PROCESSED}
     * или {@link ProcessingStatus#FAILED} в зависимости от результата обработки.
     */
    @Transactional
    @Scheduled(
            fixedDelayString = "${inbox.scheduler.fixed-delay-ms:5000}",
            initialDelayString = "${inbox.scheduler.initial-delay-ms:1000}")
    @SchedulerLock(
            name = "processInboxMessages",
            lockAtMostFor = "PT5M",
            lockAtLeastFor = "PT0S"
    )
    public void process() {
        Pageable limitAndSort = PageRequest.of(0, batchSize, Sort.by(SORT_BY_CREATED_AT).ascending());
        List<Inbox> inboxList = inboxRepository.findAllByStatus(ProcessingStatus.PENDING, limitAndSort);

        inboxList.forEach(inboxEntity -> {
                    try {
                        inboxProcessor.process(inboxEntity);
                        inboxEntity.setStatus(ProcessingStatus.PROCESSED);
                    } catch (Exception ex) {
                        inboxEntity.setStatus(ProcessingStatus.FAILED);
                    }
                });

        inboxRepository.saveAll(inboxList);
    }
}
