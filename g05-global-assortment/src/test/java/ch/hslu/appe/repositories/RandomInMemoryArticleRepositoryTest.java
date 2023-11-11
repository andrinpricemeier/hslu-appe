package ch.hslu.appe.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.hslu.appe.stock.api.StockFactory;

final class RandomInMemoryArticleRepositoryTest {
    @Test
    void testCreatingRepositoryCreatesRandomArticles() {
        final var repository = new RandomInMemoryArticleRepository(StockFactory.getStock());
        assertThat(repository.getAll()).isNotEmpty();
    }

    @Test
    void testReserveWorks() throws ArticleReservationFailedException {
        final var repository = new RandomInMemoryArticleRepository(StockFactory.getStock());
        final var articles = repository.getAll();
        final var reservationNr = repository.reserve(articles.get(0).getArticleNr(), 1);
        assertThat(reservationNr).isGreaterThan(0);
    }

    @Test
    void testReserveUpdatesStock() throws ArticleReservationFailedException {
        final var repository = new RandomInMemoryArticleRepository(StockFactory.getStock());
        final var articles = repository.getAll();
        final int stockAfter = articles.get(0).getStock() - 1;
        repository.reserve(articles.get(0).getArticleNr(), 1);
        assertThat(articles.get(0).getStock()).isEqualTo(stockAfter);
    }

    @Test
    void testReservingTooManyArticlesThrowsException() {
        final var repository = new RandomInMemoryArticleRepository(StockFactory.getStock());
        final var articles = repository.getAll();
        assertThrows(ArticleReservationFailedException.class, () -> repository.reserve(articles.get(0).getArticleNr(), articles.get(0).getStock() + 1));
    }

    @Test
    void testFreeReservation() throws ArticleReservationFailedException {
        final var repository = new RandomInMemoryArticleRepository(StockFactory.getStock());
        final var articles = repository.getAll();
        final var reservationNr = repository.reserve(articles.get(0).getArticleNr(), 1);
        repository.freeReservation(reservationNr);
        assertThat(repository.getByReservationNr(reservationNr)).isNull();
    }

    @Test
    void testOrderRemovesReservation() throws ArticleReservationFailedException {
        final var repository = new RandomInMemoryArticleRepository(StockFactory.getStock());
        final var articles = repository.getAll();
        final var reservationNr = repository.reserve(articles.get(0).getArticleNr(), 1);
        repository.order(reservationNr);
        assertThat(repository.getByReservationNr(reservationNr)).isNull();
    }

    @Test
    void testGetArticleByReservationNrWorks() throws ArticleReservationFailedException {
        final var repository = new RandomInMemoryArticleRepository(StockFactory.getStock());
        final var articles = repository.getAll();
        final var reservationNr = repository.reserve(articles.get(0).getArticleNr(), 1);
        assertThat(repository.getByReservationNr(reservationNr)).isEqualTo(articles.get(0));
    }
}
