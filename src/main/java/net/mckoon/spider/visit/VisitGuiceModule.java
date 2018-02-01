package net.mckoon.spider.visit;

import com.google.inject.AbstractModule;

/**
 * Guice module for the visit package.
 */
public class VisitGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(JsoupConnectionService.class).to(DefaultJsoupConnectionService.class);
        bind(VisitService.class).to(JsoupVisitService.class);
    }

}
