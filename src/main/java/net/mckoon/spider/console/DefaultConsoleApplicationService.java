package net.mckoon.spider.console;

import java.lang.invoke.MethodHandles;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import com.codahale.metrics.annotation.Timed;
import net.mckoon.spider.config.SpiderConfigService;
import net.mckoon.spider.crawl.CrawlService;
import net.mckoon.spider.exception.NoSeedUrlException;
import net.mckoon.spider.frontier.FrontierService;
import net.mckoon.spider.index.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

/**
 * Default implementation of {@link ConsoleApplicationService}.
 */
public class DefaultConsoleApplicationService implements ConsoleApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final CrawlService crawlService;
    private final FrontierService frontierService;
    private final IndexService indexService;
    private final SpiderConfigService spiderConfigService;

    /**
     * Injectable constructor.
     *
     * @param crawlService to start crawling the frontier.
     * @param frontierService to populate the frontier with the seed URLs.
     * @param indexService to clear the search index before starting a fresh crawl.
     * @param spiderConfigService to fetch configuration values.
     */
    @Inject
    public DefaultConsoleApplicationService(
            @Nonnull CrawlService crawlService,
            @Nonnull FrontierService frontierService,
            @Nonnull IndexService indexService,
            @Nonnull SpiderConfigService spiderConfigService
    ) {
        this.crawlService = requireNonNull(crawlService);
        this.frontierService = requireNonNull(frontierService);
        this.indexService = requireNonNull(indexService);
        this.spiderConfigService = requireNonNull(spiderConfigService);
    }

    @Timed
    @Override
    public void execute(
            @Nullable String... args
    ) {
        LOGGER.info("Executing Console Application.");

        List<String> seedUrls = spiderConfigService.getSeedUrls();

        if (seedUrls.isEmpty()) {
            LOGGER.error("No seed URLs configured. Exiting.");
            throw new NoSeedUrlException();
        }

        /* App currently has no persistence layer, so clear search index for fresh crawl. */
        indexService.clearAll();

        /* Populate crawl frontier with seed URLs to start. */
        for (String seedUrl : seedUrls) {
            LOGGER.debug("Adding seed URL: {}", seedUrl);
            frontierService.addUrl(seedUrl);
        }

        /* Start crawling the frontier. */
        crawlService.crawl();

        LOGGER.info("Console Application execution complete.");
    }

}
