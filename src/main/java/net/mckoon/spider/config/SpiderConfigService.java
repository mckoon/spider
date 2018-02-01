package net.mckoon.spider.config;

import java.time.Duration;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Service for getting configuration values for the application.
 */
public interface SpiderConfigService {

    /**
     * Returns the list of allowed host substrings that must match URLs visited when crawling if host filter is enabled.
     *
     * @return the list of host name strings.
     */
    @Nonnull
    List<String> getHostFilterAllowedHostFilters();

    /**
     * Returns true if filtering URLs to specific host names is enabled.
     *
     * @return true if host filtering URL is enabled, otherwise false.
     */
    boolean isHostFilterEnabled();

    /**
     * Gets the list of seed URLs to add to the crawl frontier on start-up.
     *
     * @return the list of seed URL strings.
     */
    @Nonnull
    List<String> getSeedUrls();

    /**
     * Gets the duration for how often to report gathered application metrics.
     *
     * @return the {@link Duration} for how frequent to report metrics.
     */
    @Nonnull
    Duration getMetricsReporterPeriod();

    /**
     * Gets the search instance hostname.
     *
     * @return The host name for the search instance.
     * It should either be a machine name or a textual representation of its IP address.
     */
    @Nonnull
    String getSearchHost();

    /**
     * Gets the search instance port number.
     *
     * @return the port number for the search instance.
     */
    int getSearchPort();

    /**
     * Gets the name of the search index.
     *
     * @return the search index string.
     */
    @Nonnull
    String getSearchIndexName();

    /**
     * Gets the name of the type of documents to create in the search index.
     *
     * @return the search document type string.
     */
    @Nonnull
    String getSearchDocumentType();

}
