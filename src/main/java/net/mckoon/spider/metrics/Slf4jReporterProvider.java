package net.mckoon.spider.metrics;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import net.mckoon.spider.config.SpiderConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

/**
 * Provider for {@link Slf4jReporter}, configuring where and how metrics are logged.
 */
public class Slf4jReporterProvider implements Provider<Slf4jReporter> {

    private final MetricRegistry metricRegistry;
    private final SpiderConfigService spiderConfigService;

    @Inject
    public Slf4jReporterProvider(
            @Nonnull MetricRegistry metricRegistry,
            @Nonnull SpiderConfigService spiderConfigService
    ) {
        this.metricRegistry = requireNonNull(metricRegistry);
        this.spiderConfigService = requireNonNull(spiderConfigService);
    }

    @Nonnull
    @Override
    public Slf4jReporter get() {
        Logger metricsLogger = LoggerFactory.getLogger("metrics");

        long reporterPeriodInSeconds = spiderConfigService.getMetricsReporterPeriod().getSeconds();

        Slf4jReporter metricsReporter = Slf4jReporter.forRegistry(metricRegistry)
                .outputTo(metricsLogger)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();

        metricsReporter.start(reporterPeriodInSeconds, TimeUnit.SECONDS);

        return metricsReporter;
    }

}
