package net.mckoon.spider.frontier;

import javax.annotation.Nonnull;

public interface UrlService {

    /**
     * If the crawler is limited to specific hosts, verify that the URL meets the requirements.
     *
     * @param url to verify is in scope.
     * @return true if the URL meets requirements, otherwise false.
     */
    boolean isUrlInScope(
            @Nonnull String url
    );

}
