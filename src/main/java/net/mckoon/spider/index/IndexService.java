package net.mckoon.spider.index;

import javax.annotation.Nonnull;

import net.mckoon.spider.WebPage;

/**
 * Service for indexing {@link WebPage} instances.
 */
public interface IndexService {

    /**
     * Resets the search index, deleting all indexed data.
     */
    void clearAll();

    /**
     * Adds the provided {@link WebPage} instance to the search index.
     *
     * @param webPage the {@link WebPage} instance to index.
     */
    void index(
            @Nonnull WebPage webPage
    );

}
