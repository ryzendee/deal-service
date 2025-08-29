package ryzendee.app.testutils.testcontainers;

import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.*;

/**
 * {@code @EnableTestcontainers} — пользовательская аннотация,
 * которая включает {@link TestcontainersInitializer} для интеграционных тестов.
 * <p>
 * <p>При запуске такого теста Spring автоматически применит
 * {@link TestcontainersInitializer}, который поднимет контейнеры и пропишет
 * необходимые проперти в тестовый контекст.</p>
 *
 * <h3>Как использовать</h3>
 * Вместо базового класса с контейнерами нужно написать следущее:
 * <pre>{@code
 * @SpringBootTest
 * @EnableTestcontainers
 * class SomeIntegrationTest {
 *     // тесты
 * }
 * }</pre>
 *
 * @author Dmitry Ryazantsev
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ContextConfiguration(initializers = TestcontainersInitializer.class)
public @interface EnableTestcontainers {
}