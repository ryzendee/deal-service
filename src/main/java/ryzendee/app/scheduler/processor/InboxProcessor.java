package ryzendee.app.scheduler.processor;

import ryzendee.app.models.Inbox;

/**
 * Сервис для обработки сообщений из входящей очереди {@link Inbox}.
 *
 * Реализации данного интерфейса должны определить логику обработки конкретного события.
 *
 * @author Dmitry Ryazantsev
 */
public interface InboxProcessor {

    /**
     * Обрабатывает указанное сообщение {@link Inbox} из входящей очереди.
     *
     * @param inbox сообщение для обработки
     */
    void process(Inbox inbox);
}
