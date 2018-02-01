package net.mckoon.spider.crawl;

import java.lang.invoke.MethodHandles;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import com.codahale.metrics.annotation.Timed;
import net.mckoon.spider.WebPage;
import net.mckoon.spider.exception.PageLoadException;
import net.mckoon.spider.frontier.FrontierService;
import net.mckoon.spider.index.IndexService;
import net.mckoon.spider.visit.VisitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

/**
 * Default implementation of {@link CrawlService}.
 */
public class DefaultCrawlService implements CrawlService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final FrontierService frontierService;
    private final IndexService indexService;
    private final VisitService visitService;

    /**
     * Injectable constructor.
     *
     * @param frontierService for fetching the next URL to visit, and adding new URLs.
     * @param indexService for indexing each page after it is visited.
     * @param visitService for visiting a page in the frontier.
     */
    @Inject
    public DefaultCrawlService(
            @Nonnull FrontierService frontierService,
            @Nonnull IndexService indexService,
            @Nonnull VisitService visitService
    ) {
        this.frontierService = requireNonNull(frontierService);
        this.indexService = requireNonNull(indexService);
        this.visitService = requireNonNull(visitService);
    }

    @Timed
    @Override
    public void crawl() {
        LOGGER.info("Crawling started.");

        String url = frontierService.getNextUrl();

        while (url != null) {

            /* Visit the URL to get the page. */
            WebPage webPage;
            try {
                webPage = visitService.visit(url);
                processWebPage(webPage);
            } catch (PageLoadException pageLoadException) {
                /* Log exception and keep processing other URLs. */
                LOGGER.error(
                        "Error visiting URL: {}",
                        url,
                        pageLoadException
                );
            }

            /* Move on to the next URL. */
            url = frontierService.getNextUrl();
        }

        LOGGER.info("Crawling complete.");
    }

    private void processWebPage(
            @Nonnull WebPage webPage
    ) {
        /* Add URLs linked from page to frontier. */
        for (String link : webPage.getLinks()) {
            frontierService.addUrl(link);
        }

        /* Index the page. */
        indexService.index(webPage);
    }

}
