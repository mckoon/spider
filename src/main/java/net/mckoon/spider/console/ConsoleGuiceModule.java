package net.mckoon.spider.console;

import com.google.inject.AbstractModule;

/**
 * Guice module for the console package.
 */
public class ConsoleGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ConsoleApplicationService.class).to(DefaultConsoleApplicationService.class);
    }

}
