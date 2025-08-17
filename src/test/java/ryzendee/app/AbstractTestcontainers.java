package ryzendee.app;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractTestcontainers {

    private static final String POSTGRES_IMAGE = "postgres:16.9-alpine";
    private static final String RABBIT_IMAGE = "rabbitmq:4.1.3-management-alpine";

    @ServiceConnection
    protected static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_IMAGE));

    @ServiceConnection
    protected static RabbitMQContainer rabbitMQContainer =
            new RabbitMQContainer(DockerImageName.parse(RABBIT_IMAGE));
}