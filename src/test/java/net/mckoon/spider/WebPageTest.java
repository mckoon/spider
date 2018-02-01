package net.mckoon.spider;

import javax.inject.Inject;

import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Unit tests for {@link WebPage}.
 */
@Guice(modules = SpiderTestGuiceModule.class)
public class WebPageTest {

    @Inject
    private WebPage webPage;

    /**
     * It should be equal to a builder built from the instance.
     *
     * @throws Exception on error.
     */
    @Test
    public void testBuilderFrom() throws Exception {
        WebPage webPageCopy = WebPage.Builder.from(webPage).build();
        assertEquals(webPageCopy, webPage);
    }

}
