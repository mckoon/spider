package net.mckoon.spider.crawl;

import com.google.inject.AbstractModule;

/**
 * Guice module for the crawl package.
 */
public class CrawlGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CrawlService.class).to(DefaultCrawlService.class);
    }

}
