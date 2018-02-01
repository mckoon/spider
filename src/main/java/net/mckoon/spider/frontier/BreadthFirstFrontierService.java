package net.mckoon.spider.frontier;

import java.lang.invoke.MethodHandles;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import com.codahale.metrics.annotation.Gauge;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

/**
 * Default implementation of the {@link FrontierService}.
 */
public class BreadthFirstFrontierService implements FrontierService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Deque<String> frontier;
    private final Set<String> visited;
    private final UrlService urlService;

    /**
     * Injectable constructor.
     */
    @Inject
    public BreadthFirstFrontierService(
            @Nonnull UrlService urlService
    ) {
        this.urlService = urlService;
        this.frontier = new ArrayDeque<>();
        this.visited = new HashSet<>();
    }

    @Timed
    @Override
    public synchronized boolean addUrl(
            @Nonnull String url
    ) {
        requireNonNull(url, "url cannot be null");

        boolean urlAdded = false;

        if (!urlService.isUrlInScope(url)) {
            LOGGER.info("URL not in scope: {}", url);
        } else if (visited.contains(url)) {
            LOGGER.info("URL already visited: {}", url);
        } else if (frontier.contains(url)) {
            LOGGER.info("URL already in frontier: {}", url);
        } else {
            urlAdded = frontier.offerLast(url);
            LOGGER.info("Adding URL to frontier: {}, Success: {}", url, urlAdded);
        }

        return urlAdded;
    }

    @Timed
    @Nullable
    @Override
    public synchronized String getNextUrl() {
        /* Remove first URL from queue. */
        String url = frontier.pollFirst();

        if (url == null) {
            LOGGER.info("No URL to visit in frontier.");
        } else {
            /* Track URL as visited. */
            visited.add(url);

            LOGGER.info("Visiting URL: {}", url);
        }

        return url;
    }

    @Gauge
    @Override
    public long getFrontierCount() {
        return frontier.size();
    }

    @Gauge
    @Override
    public long getVisitedCount() {
        return visited.size();
    }

}
