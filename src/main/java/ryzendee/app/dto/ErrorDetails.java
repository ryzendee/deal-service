package ryzendee.app.dto;

import java.util.List;

/**
 * DTO для передачи списка сообщений об ошибках.
 *
 * @author Dmitry Ryazantsev
 */
public record ErrorDetails(List<String> messages) {
}
