package net.mckoon.spider.index;

import java.lang.invoke.MethodHandles;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.mckoon.spider.WebPage;
import net.mckoon.spider.config.SpiderConfigService;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.IndexNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Elasticsearch implementation of {@link IndexService}.
 */
public class ElasticsearchIndexService implements IndexService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Provider<Client> clientProvider;
    private final ObjectMapper objectMapper;
    private final SpiderConfigService spiderConfigService;

    /**
     * Injectable constructor.
     *
     * @param clientProvider to get a search client instance.
     * @param objectMapper to convert a {@link WebPage} into JSON for indexing.
     * @param spiderConfigService to get search configuration.
     */
    @Inject
    public ElasticsearchIndexService(
            Provider<Client> clientProvider,
            ObjectMapper objectMapper,
            SpiderConfigService spiderConfigService
    ) {
        this.clientProvider = clientProvider;
        this.objectMapper = objectMapper;
        this.spiderConfigService = spiderConfigService;
    }

    @Timed
    @Override
    public void clearAll() {
        LOGGER.info("Clearing all search index data.");

        String searchIndexName = spiderConfigService.getSearchIndexName();

        try (Client client = clientProvider.get()) {
            DeleteIndexResponse deleteIndexResponse = client
                    .admin()
                    .indices()
                    .delete(
                            new DeleteIndexRequest(searchIndexName)
                    )
                    .actionGet();

            LOGGER.debug("Search index {} cleared. Response: {}", searchIndexName, deleteIndexResponse);

        } catch (IndexNotFoundException indexNotFoundException) {
            LOGGER.info("Search index {} not found, so not cleared.", searchIndexName);
        }

    }

    @Timed
    @Override
    public void index(
            @Nonnull WebPage webPage
    ) {
        LOGGER.debug("Indexing page {}", webPage);

        String searchIndexName = spiderConfigService.getSearchIndexName();
        String searchDocumentType = spiderConfigService.getSearchDocumentType();

        try (Client client = clientProvider.get()) {
            IndexResponse indexResponse = client.prepareIndex(
                    searchIndexName,
                    searchDocumentType
            ).setSource(
                    objectMapper.writeValueAsString(webPage),
                    XContentType.JSON
            ).get();

            LOGGER.debug("Indexed webPage {}. Response: {}", webPage, indexResponse);

        } catch (JsonProcessingException jsonProcessingException) {
            LOGGER.error("Exception indexing WebPage: {}", webPage, jsonProcessingException);
        }

        LOGGER.debug("Completed indexing page {}", webPage);
    }

}
