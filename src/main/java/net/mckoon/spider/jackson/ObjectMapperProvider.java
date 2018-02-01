package net.mckoon.spider.jackson;

import javax.inject.Provider;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Provides a configured Jackson {@link ObjectMapper}.
 */
public class ObjectMapperProvider implements Provider<ObjectMapper> {

    @Timed
    @Override
    public ObjectMapper get() {

        /* Create instance. */
        ObjectMapper objectMapper = new ObjectMapper();

        /* Add modules. */
        objectMapper.registerModules(new GuavaModule());
        objectMapper.registerModules(new JavaTimeModule());

        /* Set configuration. */
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return objectMapper;
    }

}
