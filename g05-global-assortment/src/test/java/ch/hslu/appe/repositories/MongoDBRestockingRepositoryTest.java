package ch.hslu.appe.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

import ch.hslu.appe.entities.Restocking;

/**
 * NOTE: This class and its methods have to be public, otherwise the TestContainers framework doesn't work.
 * NOTE: Docker has to be started first. Disabled because they don't work on GitLab.
 */
@Testcontainers
@Disabled
public final class MongoDBRestockingRepositoryTest {
    @Container
    public GenericContainer mongoDB = new GenericContainer(DockerImageName.parse("mongo:4.2.5"))
            .withExposedPorts(27017);
    private MongoDBRestockingRepository repository;

    @BeforeEach
    public void setUp() {
        final var url = mongoDB.getHost();
        final var port = mongoDB.getFirstMappedPort();
        repository = new MongoDBRestockingRepository(url, port, "", "");
    }

    @Test
    public void testAddWorks() {
        final var restocking = new Restocking();
        restocking.setArticleNr(123);
        restocking.setAmount(20);
        repository.add(restocking);
        final var retrieved = repository.getAll().get(0);
        assertThat(retrieved.getArticleNr()).isEqualTo(restocking.getArticleNr());
        assertThat(retrieved.getAmount()).isEqualTo(restocking.getAmount());
    }

    @Test
    public void testGetAllWorks() {
        final var restocking = new Restocking();
        restocking.setArticleNr(123);
        restocking.setAmount(20);
        repository.add(restocking);
        final var restocking2 = new Restocking();
        restocking2.setArticleNr(1234);
        restocking2.setAmount(200);
        repository.add(restocking2);
        assertThat(repository.getAll().size()).isEqualTo(2);
    }
}
