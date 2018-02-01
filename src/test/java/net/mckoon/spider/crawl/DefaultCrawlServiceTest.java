package net.mckoon.spider.crawl;

import javax.inject.Inject;
import javax.inject.Provider;

import net.mckoon.spider.SpiderTestGuiceModule;
import net.mckoon.spider.WebPage;
import net.mckoon.spider.exception.PageLoadException;
import net.mckoon.spider.frontier.FrontierService;
import net.mckoon.spider.index.IndexService;
import net.mckoon.spider.visit.VisitService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DefaultCrawlService}.
 */
@Guice(modules = SpiderTestGuiceModule.class)
public class DefaultCrawlServiceTest {

    @Inject
    private FrontierService frontierService;

    @Inject
    private IndexService indexService;

    @Inject
    private WebPage webPage;

    @Inject
    private VisitService visitService;

    @Inject
    private Provider<DefaultCrawlService> toTestProvider;

    private DefaultCrawlService toTest;

    /**
     * Set up tests.
     *
     * @throws Exception on error.
     */
    @BeforeMethod
    public void setUp() throws Exception {
        /* Reset the mocks. */
        reset(
                frontierService,
                indexService,
                visitService
        );

        /* Create instance to test. */
        toTest = toTestProvider.get();
    }

    /**
     * It should not visit or index pages or add URLs when crawl frontier is empty.
     *
     * @throws Exception on error.
     */
    @Test
    public void testCrawlWhenFrontierEmpty() throws Exception {
        /* Train the mocks. */
        when(frontierService.getNextUrl())
                .thenReturn(null);

        /* Make the call. */
        toTest.crawl();

        /* Verify results. */
        verify(frontierService, never()).addUrl(anyString());
        verify(indexService, never()).index(any(WebPage.class));
        verify(visitService, never()).visit(anyString());
    }

    /**
     * It should visit and index the next URL when the frontier is not empty.
     *
     * @throws Exception on error.
     */
    @Test
    public void testCrawlWhenFrontierIsNotEmpty() throws Exception {
        /* Train the mocks. */
        when(frontierService.getNextUrl())
                .thenReturn(webPage.getLocation())
                .thenReturn(null);

        when(visitService.visit(webPage.getLocation()))
                .thenReturn(webPage);

        /* Make the call. */
        toTest.crawl();

        /* Verify results. */
        verify(visitService).visit(webPage.getLocation());
        verify(indexService).index(webPage);

        for (String link : webPage.getLinks()) {
            verify(frontierService).addUrl(link);
        }
    }

    /**
     * It should visit a URL and move on to next URL, when visitService throws {@link PageLoadException}.
     *
     * @throws Exception on error.
     */
    @Test
    public void testCrawlWhenPageLoadException() throws Exception {
        /* Set up test. */
        String errorUrl = "error URL";

        /* Train the mocks. */
        when(frontierService.getNextUrl())
                .thenReturn(errorUrl)
                .thenReturn(webPage.getLocation())
                .thenReturn(null);

        when(visitService.visit(errorUrl))
                .thenThrow(new PageLoadException());

        when(visitService.visit(webPage.getLocation()))
                .thenReturn(webPage);

        /* Make the call. */
        toTest.crawl();

        /* Verify results. */
        verify(visitService).visit(errorUrl);
        verify(visitService).visit(webPage.getLocation());
        verify(indexService).index(webPage);

        for (String link : webPage.getLinks()) {
            verify(frontierService).addUrl(link);
        }

    }

}
