package net.mckoon.spider.index;

import javax.inject.Inject;
import javax.inject.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.mckoon.spider.SpiderTestGuiceModule;
import net.mckoon.spider.WebPage;
import net.mckoon.spider.config.SpiderConfigService;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * Unit tests for {@link ElasticsearchIndexService}.
 */
@Guice(modules = SpiderTestGuiceModule.class)
public class ElasticsearchIndexServiceTest {

    private static final String SEARCH_INDEX_NAME = "the-index-name";
    private static final String SEARCH_DOCUMENT_TYPE = "the-document-type";
    private static final String WEB_PAGE_JSON = "web-page-json";

    @Inject
    private AdminClient adminClient;

    @Inject
    private Client client;

    @Inject
    private DeleteIndexResponse deleteIndexResponse;

    @Inject
    private ActionFuture<DeleteIndexResponse> deleteIndexResponseActionFuture;

    @Inject
    private IndexRequestBuilder indexRequestBuilder;

    @Inject
    private IndexResponse indexResponse;

    @Inject
    private IndicesAdminClient indicesAdminClient;

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private SpiderConfigService spiderConfigService;

    @Inject
    private WebPage webPage;

    @Inject
    private Provider<ElasticsearchIndexService> toTestProvider;

    private ElasticsearchIndexService toTest;

    /**
     * Set up tests.
     *
     * @throws Exception on error.
     */
    @BeforeMethod
    public void setUp() throws Exception {
        /* Reset the mocks. */
        reset(
                adminClient,
                client,
                deleteIndexResponse,
                deleteIndexResponseActionFuture,
                indexRequestBuilder,
                indexResponse,
                indicesAdminClient,
                objectMapper,
                spiderConfigService
        );

        /* Create instance to test. */
        toTest = toTestProvider.get();
    }

    /**
     * It should send a {@link DeleteIndexRequest} and close the client.
     *
     * @throws Exception on error.
     */
    @Test
    public void testClearAll() throws Exception {
        /* Train the mocks. */
        when(spiderConfigService.getSearchIndexName()).thenReturn(SEARCH_INDEX_NAME);
        when(client.admin()).thenReturn(adminClient);
        when(adminClient.indices()).thenReturn(indicesAdminClient);
        when(indicesAdminClient.delete(any(DeleteIndexRequest.class))).thenReturn(deleteIndexResponseActionFuture);
        when(deleteIndexResponseActionFuture.actionGet()).thenReturn(deleteIndexResponse);

        /* Make the call. */
        toTest.clearAll();

        /* Verify results. */
        verify(client).close();
        verify(deleteIndexResponseActionFuture).actionGet();
    }

    /**
     * It should serialize the {@link WebPage}, send the index request, and close the client.
     *
     * @throws Exception on error.
     */
    @Test
    public void testIndex() throws Exception {
        /* Set up test. */

        /* Train the mocks. */
        when(spiderConfigService.getSearchIndexName()).thenReturn(SEARCH_INDEX_NAME);
        when(spiderConfigService.getSearchDocumentType()).thenReturn(SEARCH_DOCUMENT_TYPE);
        when(client.prepareIndex(SEARCH_INDEX_NAME, SEARCH_DOCUMENT_TYPE)).thenReturn(indexRequestBuilder);
        when(objectMapper.writeValueAsString(webPage)).thenReturn(WEB_PAGE_JSON);
        when(indexRequestBuilder.setSource(WEB_PAGE_JSON, XContentType.JSON)).thenReturn(indexRequestBuilder);
        when(indexRequestBuilder.get()).thenReturn(indexResponse);

        /* Make the call. */
        toTest.index(webPage);

        /* Verify results. */
        verify(client).close();
        verify(indexRequestBuilder).setSource(WEB_PAGE_JSON, XContentType.JSON);
        verify(indexRequestBuilder).get();
    }

}
