package net.mckoon.spider.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.google.inject.AbstractModule;
import com.palominolabs.metrics.guice.MetricsInstrumentationModule;

/**
 * Guice module for the metrics package.
 */
public class MetricsGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        MetricRegistry metricRegistry = new MetricRegistry();

        bind(MetricRegistry.class).toInstance(metricRegistry);

        install(
                MetricsInstrumentationModule.builder()
                        .withMetricRegistry(metricRegistry)
                        .build()
        );

        bind(Slf4jReporter.class).toProvider(Slf4jReporterProvider.class).asEagerSingleton();
    }

}
