package ch.hslu.appe.repositories;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Responsible for generating unique article numbers.
 */
public final class ArticleNrGenerator {
    private final static int MAX_TRIES = 1000;
    private final Set<Integer> takenArticleNumbers = new HashSet<>();
    private int lastGeneratedArticleNr;

    /**
     * Adhere to the command-query separation principle postulated by Bertrand Meyer which says
     * that a method should either be a command and mutate state or return something (without mutating state).
     * @return
     */
    public int getLastGeneratedArticleNr() {
        return lastGeneratedArticleNr;
    }
    
    /**
     * Generate a new article number.
     */
    public void generate() {
        int currentTries = MAX_TRIES;
        // We shouldn't loop indefinitely when we're out of numbers.
        while (currentTries > 0) {
            int id = ThreadLocalRandom.current().nextInt(10000, Integer.MAX_VALUE);
            if (!takenArticleNumbers.contains(id)) {
                takenArticleNumbers.add(id);
                lastGeneratedArticleNr = id;
                return;
            }
            currentTries--;
        }
        throw new IllegalStateException("Failed to generate a free random article id.");
    }
}
