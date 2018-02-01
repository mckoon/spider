package net.mckoon.spider.frontier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Service for managing the visited and unvisited URLs.
 */
public interface FrontierService {

    /**
     * Adds a URL to the crawl frontier if not previously visited.
     *
     * @param url to add to crawl frontier.
     * @return true if the url was added, false if it was already visited.
     */
    boolean addUrl(
            @Nonnull String url
    );

    /**
     * Removes the next URL from the frontier, marking it as visited.
     * If there are no unvisited URLs, returns null.
     *
     * @return the next URL to visit.
     */
    @Nullable
    String getNextUrl();

    /**
     * Retrieves the total count of non-visited URLs in the crawl frontier.
     *
     * @return the count of visited URLs.
     */
    long getFrontierCount();

    /**
     * Retrieves the total count of visited URLs.
     *
     * @return the count of visited URLs.
     */
    long getVisitedCount();

}
