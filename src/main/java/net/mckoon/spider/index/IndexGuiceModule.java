package net.mckoon.spider.index;

import com.google.inject.AbstractModule;
import org.elasticsearch.client.Client;

/**
 * Guice module for the index package.
 */
public class IndexGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Client.class).toProvider(ElasticsearchClientProvider.class);
        bind(IndexService.class).to(ElasticsearchIndexService.class);
    }

}
