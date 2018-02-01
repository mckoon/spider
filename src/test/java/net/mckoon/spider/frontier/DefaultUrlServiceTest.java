package net.mckoon.spider.frontier;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import com.google.common.collect.ImmutableList;
import net.mckoon.spider.SpiderTestGuiceModule;
import net.mckoon.spider.config.SpiderConfigService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * Unit tests for {@link DefaultUrlService}.
 */
@Guice(modules = SpiderTestGuiceModule.class)
public class DefaultUrlServiceTest {

    private static final List<String> ALLOWED_HOST_FILTERS = ImmutableList.of(
            "cc.gatech.edu"
    );

    @Inject
    private SpiderConfigService spiderConfigService;

    @Inject
    private Provider<DefaultUrlService> toTestProvider;

    private DefaultUrlService toTest;

    /**
     * Set up tests.
     *
     * @throws Exception on error.
     */
    @BeforeMethod
    public void setUp() throws Exception {
        /* Reset the mocks. */
        reset(spiderConfigService);

        /* Create instance to test. */
        toTest = toTestProvider.get();
    }

    /**
     * It should throw {@link NullPointerException} when url is null.
     *
     * @throws Exception on error.
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void testIsUrlInScopeWhenNull() throws Exception {
        /* Make the call. */
        toTest.isUrlInScope(null);
    }

    /**
     * It should return false when URL is a mailto link.
     *
     * @throws Exception on error.
     */
    @Test
    public void testIsUrlInScopeWhenMailToUrl() throws Exception {
        /* Make the call. */
        boolean actual = toTest.isUrlInScope("mailto:advising@cc.gatech.edu");

        /* Verify results. */
        assertFalse(actual);
    }

    /**
     * It should return true when host filter is disabled.
     *
     * @throws Exception on error.
     */
    @Test
    public void testIsUrlInScopeWhenFilterDisabled() throws Exception {
        /* Train the mocks. */
        when(spiderConfigService.isHostFilterEnabled())
                .thenReturn(false);

        /* Make the call. */
        boolean actual = toTest.isUrlInScope("http://example.com/");

        /* Verify results. */
        assertTrue(actual);
    }

    /**
     * It should return false when host filter is enabled and URL does not match hosts.
     *
     * @throws Exception on error.
     */
    @Test
    public void testIsUrlInScopeWhenFilterEnabledNoMatch() throws Exception {
        /* Train the mocks. */
        when(spiderConfigService.isHostFilterEnabled())
                .thenReturn(true);

        when(spiderConfigService.getHostFilterAllowedHostFilters())
                .thenReturn(ALLOWED_HOST_FILTERS);

        /* Make the call. */
        boolean actual = toTest.isUrlInScope("https://mckoon.net");

        /* Verify results. */
        assertFalse(actual);
    }

    /**
     * It should return true when host filter is enabled and URL starts with "https://cc.gatech.edu/".
     *
     * @throws Exception on error.
     */
    @Test
    public void testIsUrlInScopeWhenFilterEnabledFirstMatch() throws Exception {
        /* Train the mocks. */
        when(spiderConfigService.isHostFilterEnabled())
                .thenReturn(true);

        when(spiderConfigService.getHostFilterAllowedHostFilters())
                .thenReturn(ALLOWED_HOST_FILTERS);

        /* Make the call. */
        boolean actual = toTest.isUrlInScope("https://cc.gatech.edu/research#superfooter");

        /* Verify results. */
        assertTrue(actual);
    }

    /**
     * It should return true when host filter is enabled and URL is "https://www.cc.gatech.edu/".
     *
     * @throws Exception on error.
     */
    @Test
    public void testIsUrlInScopeWhenFilterEnabledSecondMatch() throws Exception {
        /* Train the mocks. */
        when(spiderConfigService.isHostFilterEnabled())
                .thenReturn(true);

        when(spiderConfigService.getHostFilterAllowedHostFilters())
                .thenReturn(ALLOWED_HOST_FILTERS);

        /* Make the call. */
        boolean actual = toTest.isUrlInScope("http://www.cc.gatech.edu/");

        /* Verify results. */
        assertTrue(actual);
    }

    /**
     * It should return true when host filter is enabled and URL is "https://intranet.cc.gatech.edu/".
     *
     * @throws Exception on error.
     */
    @Test
    public void testIsUrlInScopeWhenFilterEnabledThirdMatch() throws Exception {
        /* Train the mocks. */
        when(spiderConfigService.isHostFilterEnabled())
                .thenReturn(true);

        when(spiderConfigService.getHostFilterAllowedHostFilters())
                .thenReturn(ALLOWED_HOST_FILTERS);

        /* Make the call. */
        boolean actual = toTest.isUrlInScope("https://intranet.cc.gatech.edu/");

        /* Verify results. */
        assertTrue(actual);
    }

    /**
     * It should return false when invalid URL.
     *
     * throws Exception on error.
     */
    @Test
    public void testIsUrlInScopeWhenUrlInvalid() throws Exception {
        /* Train the mocks. */
        when(spiderConfigService.isHostFilterEnabled())
                .thenReturn(true);

        when(spiderConfigService.getHostFilterAllowedHostFilters())
                .thenReturn(ALLOWED_HOST_FILTERS);

        /* Make the call. */
        boolean actual = toTest.isUrlInScope("mailto:venkat%@cc.gatech.edu");

        /* Verify results. */
        assertFalse(actual);
    }
}
