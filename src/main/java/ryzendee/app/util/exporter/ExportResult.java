package ryzendee.app.util.exporter;

import lombok.Builder;

/**
 * Результат экспорта с именем файла и содержимым в виде массива байтов.
 *
 * @author Dmitry Ryazantsev
 */
@Builder
public record ExportResult(String filename, byte[] content) {

}
