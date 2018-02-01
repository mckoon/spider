package net.mckoon.spider.visit;

import javax.annotation.Nonnull;

import org.jsoup.Connection;

public interface JsoupConnectionService {

    Connection connect(
            @Nonnull String url
    );

}
