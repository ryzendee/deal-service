package ryzendee.app.dto;

/**
 * DTO с деталями роли, включая её идентификатор, название и категорию.
 *
 * @author Dmitry Ryazantsev
 */
public record RoleDetails(String id, String name, String category) {
}
