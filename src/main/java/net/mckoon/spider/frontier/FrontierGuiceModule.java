package net.mckoon.spider.frontier;

import com.google.inject.AbstractModule;

/**
 * Guice module for the frontier package.
 */
public class FrontierGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(FrontierService.class).to(BreadthFirstFrontierService.class).asEagerSingleton();
        bind(UrlService.class).to(DefaultUrlService.class);
    }

}
