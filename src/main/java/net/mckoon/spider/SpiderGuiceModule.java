package net.mckoon.spider;

import com.google.inject.AbstractModule;
import net.mckoon.spider.config.ConfigGuiceModule;
import net.mckoon.spider.console.ConsoleGuiceModule;
import net.mckoon.spider.crawl.CrawlGuiceModule;
import net.mckoon.spider.frontier.FrontierGuiceModule;
import net.mckoon.spider.index.IndexGuiceModule;
import net.mckoon.spider.jackson.JacksonGuiceModule;
import net.mckoon.spider.metrics.MetricsGuiceModule;
import net.mckoon.spider.visit.VisitGuiceModule;

/**
 * Guice module for the spider application.
 */
public class SpiderGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new ConfigGuiceModule());
        install(new ConsoleGuiceModule());
        install(new CrawlGuiceModule());
        install(new FrontierGuiceModule());
        install(new IndexGuiceModule());
        install(new JacksonGuiceModule());
        install(new MetricsGuiceModule());
        install(new VisitGuiceModule());
    }

}
