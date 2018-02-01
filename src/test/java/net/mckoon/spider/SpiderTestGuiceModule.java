package net.mckoon.spider;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import net.mckoon.spider.config.SpiderConfigService;
import net.mckoon.spider.crawl.CrawlService;
import net.mckoon.spider.frontier.FrontierService;
import net.mckoon.spider.frontier.UrlService;
import net.mckoon.spider.index.IndexService;
import net.mckoon.spider.visit.JsoupConnectionService;
import net.mckoon.spider.visit.VisitService;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.jsoup.Connection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Guice module for the unit-testing environment.
 */
public class SpiderTestGuiceModule extends AbstractModule {

    private static final Instant INSTANT = Instant.now();
    private static final String LINK1 = "https://www.cc.gatech.edu/schools/";
    private static final String LINK2 = "https://www.cc.gatech.edu/content/academics";
    private static final String LOCATION = "https://www.cc.gatech.edu/";
    private static final String TEXT = "This is the text.";
    private static final String TITLE = "This is the title.";

    @Mock
    private ActionFuture<DeleteIndexResponse> deleteIndexResponseActionFuture;

    @Mock
    private AdminClient adminClient;

    @Mock
    private Client client;

    @Mock
    private Connection connection;

    @Mock
    private CrawlService crawlService;

    @Mock
    private DeleteIndexResponse deleteIndexResponse;

    @Mock
    private Document document;

    @Mock
    private FrontierService frontierService;

    @Mock
    private IndexRequestBuilder indexRequestBuilder;

    @Mock
    private IndexResponse indexResponse;

    @Mock
    private IndexService indexService;

    @Mock
    private IndicesAdminClient indicesAdminClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private SpiderConfigService spiderConfigService;

    @Mock
    private UrlService urlService;

    @Mock
    private VisitService visitService;

    @Mock
    private JsoupConnectionService jsoupConnectionService;

    public SpiderTestGuiceModule() {
    }

    @Override
    protected void configure() {
        initMocks(this);

        /* Bind interfaces to mocks. */
        bind(new TypeLiteral<ActionFuture<DeleteIndexResponse>>() {}).toInstance(deleteIndexResponseActionFuture);
        bind(AdminClient.class).toInstance(adminClient);
        bind(Client.class).toInstance(client);
        bind(Connection.class).toInstance(connection);
        bind(CrawlService.class).toInstance(crawlService);
        bind(DeleteIndexResponse.class).toInstance(deleteIndexResponse);
        bind(Document.class).toInstance(document);
        bind(FrontierService.class).toInstance(frontierService);
        bind(IndexRequestBuilder.class).toInstance(indexRequestBuilder);
        bind(IndexResponse.class).toInstance(indexResponse);
        bind(IndexService.class).toInstance(indexService);
        bind(IndicesAdminClient.class).toInstance(indicesAdminClient);
        bind(ObjectMapper.class).toInstance(objectMapper);
        bind(SpiderConfigService.class).toInstance(spiderConfigService);
        bind(UrlService.class).toInstance(urlService);
        bind(VisitService.class).toInstance(visitService);
        bind(JsoupConnectionService.class).toInstance(jsoupConnectionService);
    }

    /**
     * Provides an instance of {@link Elements} for the testing environment.
     *
     * @param webPage the test environment {@link WebPage} instance.
     * @return the {@link Elements} instance.
     */
    @Provides
    private Elements provideElements(
            @Nonnull WebPage webPage
    ) {
        List<Element> elementList = new ArrayList<>();

        for (String link : webPage.getLinks()) {
            Attributes attributes = new Attributes();
            attributes.put(
                    "href",
                    link
            );
            Element element = new Element(
                    Tag.valueOf("a"),
                    "",
                    attributes
            );
            elementList.add(element);
        }

        return new Elements(elementList);
    }

    /**
     * Provides an instance of {@link WebPage} for the testing environment.
     *
     * @return the {@link WebPage} instance.
     */
    @Provides
    private WebPage provideWebPage() {

        Set<String> links = ImmutableSet.of(
                LINK1,
                LINK2
        );

        return WebPage.Builder.builder()
                .withAccessTime(INSTANT)
                .withLinks(links)
                .withLocation(LOCATION)
                .withText(TEXT)
                .withTitle(TITLE)
                .build();
    }

}
