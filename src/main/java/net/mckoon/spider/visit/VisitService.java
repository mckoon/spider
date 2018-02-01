package net.mckoon.spider.visit;

import javax.annotation.Nonnull;

import net.mckoon.spider.WebPage;

/**
 * Service for visiting a web page at a specific location.
 */
public interface VisitService {

    /**
     * Loads and processes the page at the provided URL, returning the {@link WebPage} instance.
     *
     * @param location the {@link String} for the address to visit.
     * @return the {@link WebPage} instance created from the provided address.
     */
    @Nonnull
    WebPage visit(
            @Nonnull String location
    );

}
