package net.mckoon.spider.visit;

import javax.inject.Inject;
import javax.inject.Provider;

import net.mckoon.spider.SpiderTestGuiceModule;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Unit tests for {@link DefaultJsoupConnectionService}.
 */
@Guice(modules = SpiderTestGuiceModule.class)
public class DefaultJsoupConnectionServiceTest {

    private static final String URL = "https://www.cc.gatech.edu/";

    @Inject
    private Provider<DefaultJsoupConnectionService> toTestProvider;

    private DefaultJsoupConnectionService toTest;

    /**
     * Set up tests.
     *
     * @throws Exception on error.
     */
    @BeforeMethod
    public void setUp() throws Exception {
        /* Create instance to test. */
        toTest = toTestProvider.get();
    }

    /**
     * It should throw {@link NullPointerException} when url is null.
     *
     * @throws Exception on error.
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void testConnectWhenNull() throws Exception {
        /* Make the call. */
        toTest.connect(null);
    }

    /**
     * It should return a {@link Connection} with the provided URL.
     *
     * @throws Exception on error.
     */
    @Test
    public void testConnectWhenSuccess() throws Exception {
        /* Set up test. */
        Connection expected = Jsoup.connect(URL);

        /* Make the call. */
        Connection actual = toTest.connect(URL);

        /* Verify results. */

        /* Connection and its request do not implement equals. Verify URLs match. */
        assertEquals(
                actual.request().url(),
                expected.request().url()
        );

    }

}
