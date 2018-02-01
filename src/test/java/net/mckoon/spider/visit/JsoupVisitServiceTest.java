package net.mckoon.spider.visit;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Provider;

import net.mckoon.spider.SpiderTestGuiceModule;
import net.mckoon.spider.WebPage;
import net.mckoon.spider.exception.PageLoadException;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * Unit tests for {@link JsoupVisitService}.
 */
@Guice(modules = SpiderTestGuiceModule.class)
public class JsoupVisitServiceTest {

    private static final String LINK_SELECTOR = "a[href]";

    @Inject
    private Connection connection;

    @Inject
    private Document document;

    @Inject
    private Elements elements;

    @Inject
    private WebPage webPage;

    @Inject
    private JsoupConnectionService jsoupConnectionService;

    @Inject
    private Provider<JsoupVisitService> toTestProvider;

    private JsoupVisitService toTest;

    /**
     * Set up tests.
     *
     * @throws Exception on error.
     */
    @BeforeMethod
    public void setUp() throws Exception {
        /* Reset the mocks. */
        reset(
                connection,
                jsoupConnectionService
        );

        /* Create instance to test. */
        toTest = toTestProvider.get();
    }

    /**
     * It should throw {@link NullPointerException} when location is null.
     *
     * @throws Exception on error.
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void testVisitWhenLocationNull() throws Exception {
        /* Make the call. */
        toTest.visit(null);
    }

    /**
     * It should return the {@link WebPage} when location is not null.
     *
     * @throws Exception on error.
     */
    @Test(expectedExceptions = PageLoadException.class)
    public void testVisitWhenErrorLoadingPage() throws Exception {
        /* Train the mocks. */
        when(jsoupConnectionService.connect(webPage.getLocation()))
                .thenReturn(connection);

        when(connection.get())
                .thenThrow(new IOException());

        /* Make the call. */
        toTest.visit(webPage.getLocation());
    }

    /**
     * It should return the {@link WebPage} when location is not null.
     *
     * @throws Exception on error.
     */
    @Test
    public void testVisitWhenSuccess() throws Exception {
        /* Set up test. */
        final WebPage expected = webPage;

        /* Train the mocks. */
        when(jsoupConnectionService.connect(webPage.getLocation()))
                .thenReturn(connection);

        when(connection.get())
                .thenReturn(document);

        when(document.select(LINK_SELECTOR))
                .thenReturn(elements);

        when(document.location())
                .thenReturn(webPage.getLocation());

        when(document.title())
                .thenReturn(webPage.getTitle());

        when(document.text())
                .thenReturn(webPage.getText());

        /* Make the call. */
        WebPage actual = toTest.visit(expected.getLocation());

        /* Verify results. */

        /* Since WebPage contains an Instant, compare the other values. */
        assertEquals(actual.getLinks(), expected.getLinks());
        assertEquals(actual.getLocation(), expected.getLocation());
        assertEquals(actual.getText(), expected.getText());
        assertEquals(actual.getTitle(), expected.getTitle());
    }

}
