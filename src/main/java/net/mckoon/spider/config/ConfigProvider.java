package net.mckoon.spider.config;

import javax.inject.Provider;

import com.codahale.metrics.annotation.Timed;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Provider for a {@link Config} instance.
 */
public class ConfigProvider implements Provider<Config> {

    @Timed
    @Override
    public Config get() {
        return ConfigFactory.load();
    }

}
