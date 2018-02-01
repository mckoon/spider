package net.mckoon.spider.index;

import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import com.codahale.metrics.annotation.Timed;
import net.mckoon.spider.config.SpiderConfigService;
import net.mckoon.spider.exception.SearchConnectionException;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

/**
 * Provides the {@link Client} for connecting to elasticsearch.
 */
public class ElasticsearchClientProvider implements Provider<Client> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SpiderConfigService spiderConfigService;

    /**
     * Injectable constructor.
     *
     * @param spiderConfigService to get search configuration.
     */
    @Inject
    public ElasticsearchClientProvider(
            @Nonnull SpiderConfigService spiderConfigService
    ) {
        this.spiderConfigService = requireNonNull(spiderConfigService);
    }

    @Timed
    @Override
    public Client get() {

        TransportAddress transportAddress = null;

        try {
            transportAddress = new TransportAddress(
                    InetAddress.getByName(
                            spiderConfigService.getSearchHost()
                    ),
                    spiderConfigService.getSearchPort()
            );

        } catch (UnknownHostException unknownHostException) {
            LOGGER.error(
                    "Cannot connect to elasticsearch",
                    unknownHostException
            );
            throw new SearchConnectionException(unknownHostException);
        }

        return new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(transportAddress);
    }

}
