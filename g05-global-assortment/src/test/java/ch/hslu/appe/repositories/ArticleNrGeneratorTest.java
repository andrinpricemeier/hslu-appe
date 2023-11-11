package ch.hslu.appe.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

final class ArticleNrGeneratorTest {
    @Test
    void testGenerateWorks() {
        final var generator = new ArticleNrGenerator();
        generator.generate();
        final var ID_COUNT = 10000;
        // Create some amount of ids and see if they all pass.
        for (int i = 0; i < ID_COUNT; i++) {
            assertThat(generator.getLastGeneratedArticleNr()).isGreaterThanOrEqualTo(10000);
        }
    }
}
