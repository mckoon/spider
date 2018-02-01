package net.mckoon.spider.visit;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import com.codahale.metrics.annotation.Timed;
import net.mckoon.spider.WebPage;
import net.mckoon.spider.exception.PageLoadException;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of {@link VisitService} that uses Jsoup to fetch and parse the HTML pages.
 */
public class JsoupVisitService implements VisitService {

    private static final String LINK_SELECTOR = "a[href]";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final JsoupConnectionService jsoupConnectionService;

    /**
     * Injectable constructor.
     *
     * @param jsoupConnectionService for obtaining Jsoup {@link Connection} instances.
     */
    @Inject
    public JsoupVisitService(
            @Nonnull JsoupConnectionService jsoupConnectionService
    ) {
        this.jsoupConnectionService = requireNonNull(jsoupConnectionService);
    }

    @Timed
    @Nonnull
    @Override
    public WebPage visit(
            @Nonnull String location
    ) {
        LOGGER.info("visiting URL: {}", location);

        requireNonNull(location, "location cannot be null");

        Document document;
        try {
            document = jsoupConnectionService.connect(location).get();
        } catch (IOException ioException) {
            throw new PageLoadException(ioException);
        }

        Elements elements = document.select(LINK_SELECTOR);
        Set<String> links = new HashSet<>();

        for (Element element : elements) {
            links.add(
                    element.attr("abs:href")
            );
        }

        return WebPage.Builder.builder()
                .withAccessTime(Instant.now())
                .withLinks(links)
                .withLocation(document.location())
                .withTitle(document.title())
                .withText(document.text())
                .build();
    }

}
