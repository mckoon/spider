package net.mckoon.spider.frontier;

import javax.inject.Inject;
import javax.inject.Provider;

import net.mckoon.spider.SpiderTestGuiceModule;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * Unit tests for {@link BreadthFirstFrontierService}.
 */
@Guice(modules = SpiderTestGuiceModule.class)
public class BreadthFirstFrontierServiceTest {

    private static final String URL = "some url";
    private static final String ANOTHER_URL = "another url";

    @Inject
    private UrlService urlService;

    @Inject
    private Provider<BreadthFirstFrontierService> toTestProvider;

    private BreadthFirstFrontierService toTest;

    /**
     * Set up tests.
     *
     * @throws Exception on error.
     */
    @BeforeMethod
    public void setUp() throws Exception {
        /* Reset the mocks. */
        reset(urlService);

        /* Create instance to test. */
        toTest = toTestProvider.get();
    }

    /**
     * It should throw {@link NullPointerException} when url is null.
     *
     * @throws Exception on error.
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void testAddUrlWhenUrlIsNull() throws Exception {
        /* Make the call. */
        toTest.addUrl(null);
    }

    /**
     * It should return false and not add URL to frontier when URL is not in scope.
     *
     * @throws Exception on error.
     */
    @Test
    public void testAddUrlWhenUrlIsNotInScope() throws Exception {
        /* Train the mocks. */
        when(urlService.isUrlInScope(URL))
                .thenReturn(false);

        /* Make the call. */
        boolean actual = toTest.addUrl(URL);

        /* Verify results. */
        assertFalse(actual);
        assertEquals(toTest.getFrontierCount(), 0);
        assertEquals(toTest.getVisitedCount(), 0);
    }

    /**
     * It should return true and add url to frontier when frontier and visited are empty.
     *
     * @throws Exception on error.
     */
    @Test
    public void testAddUrlWhenUrlIsNewAndFrontierEmpty() throws Exception {
        /* Train the mocks. */
        when(urlService.isUrlInScope(URL))
                .thenReturn(true);

        /* Make the call. */
        boolean actual = toTest.addUrl(URL);

        /* Verify results. */
        assertTrue(actual);
        assertEquals(toTest.getFrontierCount(), 1);
        assertEquals(toTest.getVisitedCount(), 0);
    }

    /**
     * It should return true and add url to frontier when url is NOT in frontier or visited.
     *
     * @throws Exception on error.
     */
    @Test
    public void testAddUrlWhenUrlIsNewAndFrontierNotEmpty() throws Exception {
        /* Train the mocks. */
        when(urlService.isUrlInScope(ANOTHER_URL))
                .thenReturn(true);

        when(urlService.isUrlInScope(URL))
                .thenReturn(true);

        /* Set up test. */
        toTest.addUrl(ANOTHER_URL);

        /* Make the call. */
        boolean actual = toTest.addUrl(URL);

        /* Verify results. */
        assertTrue(actual);
        assertEquals(toTest.getFrontierCount(), 2);
        assertEquals(toTest.getVisitedCount(), 0);
    }

    /**
     * It should return false and NOT add url to frontier when url is already in frontier.
     *
     * @throws Exception on error.
     */
    @Test
    public void testAddUrlWhenUrlIsAlreadyInFrontier() throws Exception {
        /* Train the mocks. */
        when(urlService.isUrlInScope(URL))
                .thenReturn(true);

        /* Set up test. */
        toTest.addUrl(URL);

        /* Make the call. */
        boolean actual = toTest.addUrl(URL);

        /* Verify results. */
        assertFalse(actual);
        assertEquals(toTest.getFrontierCount(), 1);
        assertEquals(toTest.getVisitedCount(), 0);
    }

    /**
     * It should return false and NOT add url to frontier when url is already visited.
     *
     * @throws Exception on error.
     */
    @Test
    public void testAddUrlWhenUrlIsAlreadyVisited() throws Exception {
        /* Train the mocks. */
        when(urlService.isUrlInScope(URL))
                .thenReturn(true);

        /* Set up test. */
        toTest.addUrl(URL);
        toTest.getNextUrl();

        /* Make the call. */
        boolean actual = toTest.addUrl(URL);

        /* Verify results. */
        assertFalse(actual);
        assertEquals(toTest.getFrontierCount(), 0);
        assertEquals(toTest.getVisitedCount(), 1);
    }

    /**
     * It should return null when frontier is empty.
     *
     * @throws Exception on error.
     */
    @Test
    public void testGetNextUrlWhenFrontierEmpty() throws Exception {
        /* Make the call. */
        String actual = toTest.getNextUrl();

        /* Verify results. */
        assertNull(actual);
        assertEquals(toTest.getFrontierCount(), 0);
        assertEquals(toTest.getVisitedCount(), 0);
    }

    /**
     * It should return URL from front of queue and mark it as visited when frontier is NOT empty.
     *
     * @throws Exception on error.
     */
    @Test
    public void testGetNextUrlWhenFrontierNotEmpty() throws Exception {
        /* Set up test. */
        when(urlService.isUrlInScope(ANOTHER_URL))
                .thenReturn(true);

        when(urlService.isUrlInScope(URL))
                .thenReturn(true);

        toTest.addUrl(URL);
        toTest.addUrl(ANOTHER_URL);

        /* Make the call. */
        String actual = toTest.getNextUrl();

        /* Verify results. */
        assertEquals(actual, URL);
        assertEquals(toTest.getFrontierCount(), 1);
        assertEquals(toTest.getVisitedCount(), 1);
    }

    /**
     * It should return current frontier and visited sizes.
     *
     * @throws Exception on error.
     */
    @Test
    public void testCounts() throws Exception {
        /* Train the mocks. */
        when(urlService.isUrlInScope(ANOTHER_URL))
                .thenReturn(true);

        when(urlService.isUrlInScope(URL))
                .thenReturn(true);

        assertEquals(toTest.getFrontierCount(), 0);
        assertEquals(toTest.getVisitedCount(), 0);

        toTest.addUrl(URL);
        assertEquals(toTest.getFrontierCount(), 1);
        assertEquals(toTest.getVisitedCount(), 0);

        toTest.addUrl(ANOTHER_URL);
        assertEquals(toTest.getFrontierCount(), 2);
        assertEquals(toTest.getVisitedCount(), 0);

        toTest.getNextUrl();
        assertEquals(toTest.getFrontierCount(), 1);
        assertEquals(toTest.getVisitedCount(), 1);

        toTest.getNextUrl();
        assertEquals(toTest.getFrontierCount(), 0);
        assertEquals(toTest.getVisitedCount(), 2);
    }

}
