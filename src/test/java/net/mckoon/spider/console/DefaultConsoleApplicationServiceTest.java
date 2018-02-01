package net.mckoon.spider.console;

import javax.inject.Inject;
import javax.inject.Provider;

import com.google.common.collect.ImmutableList;
import net.mckoon.spider.SpiderTestGuiceModule;
import net.mckoon.spider.config.SpiderConfigService;
import net.mckoon.spider.crawl.CrawlService;
import net.mckoon.spider.exception.NoSeedUrlException;
import net.mckoon.spider.frontier.FrontierService;
import net.mckoon.spider.index.IndexService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * Unit tests for {@link DefaultConsoleApplicationService}.
 */
@Guice(modules = SpiderTestGuiceModule.class)
public class DefaultConsoleApplicationServiceTest {

    private static final String SEED_URL1 = "seed url 1";
    private static final String SEED_URL2 = "seed url 2";

    @Inject
    private CrawlService crawlService;

    @Inject
    private FrontierService frontierService;

    @Inject
    private IndexService indexService;

    @Inject
    private SpiderConfigService spiderConfigService;

    @Inject
    private Provider<DefaultConsoleApplicationService> toTestProvider;

    private DefaultConsoleApplicationService toTest;

    /**
     * Set up tests.
     *
     * @throws Exception on error.
     */
    @BeforeMethod
    public void setUp() throws Exception {
        /* Reset the mocks. */
        reset(
                crawlService,
                frontierService,
                indexService,
                spiderConfigService
        );

        /* Create instance to test. */
        toTest = toTestProvider.get();
    }

    /**
     * It should throw {@link NoSeedUrlException} when there are no seed URLs configured.
     *
     * @throws Exception on error.
     */
    @Test(expectedExceptions = NoSeedUrlException.class)
    public void testExecuteWhenNoSeedUrls() throws Exception {
        /* Train the mocks. */
        when(spiderConfigService.getSeedUrls())
                .thenReturn(ImmutableList.of());

        /* Make the call. */
        toTest.execute();
    }

    /**
     * It should reset search index, add seed URLs and start crawling when seed URLs exist.
     *
     * @throws Exception on error.
     */
    @Test
    public void testExecuteWhenSeedUrlsExist() throws Exception {
        /* Train the mocks. */
        when(spiderConfigService.getSeedUrls())
                .thenReturn(ImmutableList.of(SEED_URL1, SEED_URL2));

        when(frontierService.addUrl(SEED_URL1))
                .thenReturn(true);

        when(frontierService.addUrl(SEED_URL2))
                .thenReturn(true);

        /* Make the call. */
        toTest.execute();

        /* Verify results. */
        verify(indexService).clearAll();
        verify(frontierService).addUrl(SEED_URL1);
        verify(frontierService).addUrl(SEED_URL2);
        verify(crawlService).crawl();
    }

}
