package net.mckoon.spider.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;

/**
 * Guice module for the jackson package.
 */
public class JacksonGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class);
    }

}
