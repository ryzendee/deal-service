package ryzendee.app.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ryzendee.app.common.dto.ContractorUpdateEvent;
import ryzendee.app.models.Inbox;
import ryzendee.app.repository.InboxRepository;

/**
 * Слушатель очереди для обработки событий создания/обновления контрагента {@link ContractorUpdateEvent}.
 *
 * Каждое событие сохраняется {@link Inbox} для дальнейшей асинхронной обработки.
 *
 * @author Dmitry Ryazantsev
 */
@Component
@RabbitListener(queues = "${rabbit.contractor.queue}")
@Slf4j
@RequiredArgsConstructor
public class ContractorUpdateEventListener {

    private final InboxRepository inboxRepository;
    private final ObjectMapper objectMapper;

    /**
     * Обрабатывает входящее событие обновления контрагента.
     * <p>
     * Сохраняет событие в {@link Inbox} в виде JSON для дальнейшей обработки.
     * <ul>
     *     <li>Если событие уже обработано (DataIntegrityViolationException) — оно игнорируется.</li>
     *     <li>Любые другие исключения оборачиваются в {@link AmqpRejectAndDontRequeueException}.</li>
     * </ul>
     * </p>
     *
     * @param event событие обновления контрагента
     * @throws AmqpRejectAndDontRequeueException при ошибках обработки сообщения
     */
    @Transactional
    @RabbitHandler
    public void handle(@Payload ContractorUpdateEvent event) {
        try {
            createAndSaveInboxMessage(event);
        } catch (DataIntegrityViolationException ex) {
            log.info("Event was processed: eventType={}, eventId={}", event.getEventType(), event.getEventId());
        } catch (Exception ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }

    private void createAndSaveInboxMessage(ContractorUpdateEvent event) throws JsonProcessingException {
        Inbox entity = Inbox.builder()
                .eventId(event.getEventId())
                .payload(toJson(event))
                .eventType(event.getEventType())
                .build();
        inboxRepository.saveAndFlush(entity);
    }

    private String toJson(ContractorUpdateEvent event) throws JsonProcessingException {
        return objectMapper.writeValueAsString(event);
    }
}
