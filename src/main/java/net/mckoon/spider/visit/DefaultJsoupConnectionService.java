package net.mckoon.spider.visit;

import javax.annotation.Nonnull;

import com.codahale.metrics.annotation.Timed;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import static java.util.Objects.requireNonNull;

/**
 * Default implementation of {@link JsoupConnectionService}.
 */
public class DefaultJsoupConnectionService implements JsoupConnectionService {

    @Timed
    @Override
    public Connection connect(
            @Nonnull String url
    ) {
        requireNonNull(url, "url cannot be null");
        return Jsoup.connect(url);
    }

}
