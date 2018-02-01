package net.mckoon.spider.crawl;

/**
 * Service for crawling web pages in the crawl frontier, and processing them.
 */
public interface CrawlService {

    /**
     * Iteratively visits pages in the frontier, adding newly encountered URLs to the frontier, and indexing the pages.
     */
    void crawl();

}
