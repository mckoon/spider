package net.mckoon.spider.config;

import com.google.inject.AbstractModule;
import com.typesafe.config.Config;

/**
 * Guice module for the config package.
 */
public class ConfigGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Config.class).toProvider(ConfigProvider.class).asEagerSingleton();
        bind(SpiderConfigService.class).to(TypesafeSpiderConfigService.class);
    }

}
