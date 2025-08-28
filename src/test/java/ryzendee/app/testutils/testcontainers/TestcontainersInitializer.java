package ryzendee.app.testutils.testcontainers;


import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.stream.Stream;


/**
 * {@code TestcontainersInitializer} — инициализатор Spring ApplicationContext,
 * который поднимает необходимые контейнетры
 * один раз для всех интеграционных тестов.
 * <p>
 * Особенности:
 * <ul>
 *   <li>Запускает контейнеры параллельно (через {@link Startables}) для ускорения тестов.</li>
 *   <li>Прописывает настройки подключений напрямую в Spring Environment
 *   (через {@link TestPropertyValues#of}), чтобы они были доступны в тестовом контексте.</li>
 *   <li>Избавляет от необходимости наследовать все тесты от общего абстрактного класса с контейнерами.</li>
 * </ul>
 *
 * Контейнеры будут автоматически подняты один раз и их параметры применены
 * к тестовому Spring Boot контексту.
 *
 * <p>Используется совместно с аннотацией {@link EnableTestcontainers}.
 *
 * @author Dmitry Ryazantsev
 */
public class TestcontainersInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Duration TIMEOUT = Duration.ofMinutes(3);
    private static final String POSTGRES_IMAGE = "postgres:16.9-alpine";
    private static final String RABBIT_IMAGE = "rabbitmq:4.1.3-management-alpine";
    private static final String REDIS_IMAGE = "redis:7.4.0-alpine";

    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_IMAGE))
                    .withStartupTimeout(TIMEOUT)
                    .withReuse(true);

    private static final RabbitMQContainer rabbit =
            new RabbitMQContainer(DockerImageName.parse(RABBIT_IMAGE))
                    .withStartupTimeout(TIMEOUT)
                    .withReuse(true);

    private static final RedisContainer redis =
            new RedisContainer(DockerImageName.parse(REDIS_IMAGE))
                    .withStartupTimeout(TIMEOUT)
                    .withReuse(true);

    // Параллельный запуск
    static {
        Startables.deepStart(Stream.of(postgres, rabbit, redis)).join();
    }

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        TestPropertyValues.of(
                "spring.datasource.url=" + postgres.getJdbcUrl(),
                "spring.datasource.username=" + postgres.getUsername(),
                "spring.datasource.password=" + postgres.getPassword(),

                "spring.rabbitmq.host=" + rabbit.getHost(),
                "spring.rabbitmq.port=" + rabbit.getAmqpPort(),
                "spring.rabbitmq.username=" + rabbit.getAdminUsername(),
                "spring.rabbitmq.password=" + rabbit.getAdminPassword(),

                "spring.data.redis.host=" + redis.getRedisHost(),
                "spring.data.redis.port=" + redis.getRedisPort()
        ).applyTo(context.getEnvironment());
    }
}