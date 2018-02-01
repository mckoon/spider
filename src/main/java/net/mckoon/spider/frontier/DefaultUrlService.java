package net.mckoon.spider.frontier;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import com.codahale.metrics.annotation.Timed;
import net.mckoon.spider.config.SpiderConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

/**
 * Default implementation of {@link UrlService}.
 */
public class DefaultUrlService implements UrlService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SpiderConfigService spiderConfigService;

    /**
     * Injectable constructor.
     *
     * @param spiderConfigService to get configuration.
     */
    @Inject
    public DefaultUrlService(
            @Nonnull SpiderConfigService spiderConfigService
    ) {
        this.spiderConfigService = requireNonNull(spiderConfigService);
    }

    @Timed
    @Override
    public boolean isUrlInScope(
            @Nonnull String url
    ) {
        requireNonNull(url);

        boolean inScope = false;

        List<String> allowedHostFilters = spiderConfigService.getHostFilterAllowedHostFilters();

        String host = null;

        try {
            URI uri = URI.create(url);
            host = uri.getHost();
        } catch (Exception exception) {
            LOGGER.warn("URL failed to parse: {}", url, exception);
        }

        if (host == null) {
            LOGGER.warn("Host for URL is null: {}", url);
            inScope = false;

        } else if (spiderConfigService.isHostFilterEnabled()) {
            for (String allowedHostFilter : allowedHostFilters) {
                if (host.contains(allowedHostFilter)) {
                    inScope = true;
                }
            }
        } else {
            /* Filter disabled; all in scope. */
            inScope = true;
        }

        return inScope;
    }

}
