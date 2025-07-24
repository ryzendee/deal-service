package ryzendee.app.util.exporter;

import java.util.function.Function;

/**
 * Представляет колонку с заголовком и функцией извлечения значения из объекта.
 *
 * @param <T> тип объекта для извлечения значения
 *
 * @author Dmitry Ryazantsev
 */
public class Column<T> {

    private final String header;
    private final Function<T, String> extractor;

    public Column(String header, Function<T, String> extractor) {
        this.header = header;
        this.extractor = extractor;
    }

    public String getHeader() {
        return header;
    }

    public String extractValue(T item) {
        String result = extractor.apply(item);
        return result != null ? result : "";
    }

}
