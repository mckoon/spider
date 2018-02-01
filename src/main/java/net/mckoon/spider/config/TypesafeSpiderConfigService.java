package net.mckoon.spider.config;

import java.time.Duration;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import com.codahale.metrics.annotation.Timed;
import com.typesafe.config.Config;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of {@link SpiderConfigService} backed by a {@link Config} instance.
 */
public class TypesafeSpiderConfigService implements SpiderConfigService {

    private final List<String> hostFilterAllowedHostFilters;
    private final boolean hostFilterEnabled;
    private final Duration metricsReporterPeriod;
    private final List<String> seedUrls;
    private final String searchHost;
    private final int searchPort;
    private final String searchIndexName;
    private final String searchDocumentType;

    /**
     * Injectable constructor.
     *
     * @param config the {@link Config} instance containing all configuration.
     */
    @Inject
    public TypesafeSpiderConfigService(
            @Nonnull Config config
    ) {
        requireNonNull(config);

        hostFilterAllowedHostFilters = config.getStringList("spider.frontier.hostFilter.allowedHostFilters");
        hostFilterEnabled = config.getBoolean("spider.frontier.hostFilter.enabled");
        metricsReporterPeriod = config.getDuration("spider.metrics.reporter.period");
        seedUrls = config.getStringList("spider.frontier.seedUrls");
        searchHost = config.getString("spider.search.host");
        searchPort = config.getInt("spider.search.port");
        searchIndexName = config.getString("spider.search.indexName");
        searchDocumentType = config.getString("spider.search.documentType");
    }

    @Timed
    @Nonnull
    @Override
    public List<String> getHostFilterAllowedHostFilters() {
        return hostFilterAllowedHostFilters;
    }

    @Timed
    @Override
    public boolean isHostFilterEnabled() {
        return hostFilterEnabled;
    }

    @Timed
    @Nonnull
    @Override
    public List<String> getSeedUrls() {
        return seedUrls;
    }

    @Timed
    @Nonnull
    @Override
    public Duration getMetricsReporterPeriod() {
        return metricsReporterPeriod;
    }

    @Timed
    @Nonnull
    @Override
    public String getSearchHost() {
        return searchHost;
    }

    @Timed
    @Override
    public int getSearchPort() {
        return searchPort;
    }

    @Timed
    @Nonnull
    @Override
    public String getSearchIndexName() {
        return searchIndexName;
    }

    @Timed
    @Nonnull
    @Override
    public String getSearchDocumentType() {
        return searchDocumentType;
    }

}
