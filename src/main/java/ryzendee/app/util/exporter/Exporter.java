package ryzendee.app.util.exporter;

import java.util.Collection;

/**
 * Интерфейс для экспорта коллекции объектов.
 *
 * @param <T> тип объектов для экспорта
 *
 * @author Dmitry Ryazantsev
 */
public interface Exporter<T> {

    /**
     * Экспортирует переданную коллекцию объектов.
     *
     * @param items коллекция объектов для экспорта
     * @return результат экспорта
     */
    ExportResult export(Collection<T> items);
}
