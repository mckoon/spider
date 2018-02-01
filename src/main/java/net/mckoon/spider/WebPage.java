package net.mckoon.spider;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static java.util.Objects.requireNonNull;

/**
 * Domain object representing attributes for an HTML page at a URL.
 */
@Immutable
public final class WebPage {

    private final Instant accessTime;
    private final Set<String> links;
    private final String location;
    private final String text;
    private final String title;

    /**
     * Private constructor used by builder.
     *
     * @param accessTime the {@link Instant} when this web page was downloaded.
     * @param links the {@link Set} of links to other web pages contained in this page.
     * @param location the URL address string from which this page was loaded.
     * @param text the combined text for the entire page content.
     * @param title the string for the page's title.
     */
    private WebPage(
            @Nonnull Instant accessTime,
            @Nonnull Set<String> links,
            @Nonnull String location,
            @Nonnull String text,
            @Nonnull String title
    ) {
        this.accessTime = requireNonNull(accessTime);
        requireNonNull(links);
        this.links = ImmutableSet.copyOf(links);
        this.location = requireNonNull(location);
        this.text = requireNonNull(text);
        this.title = requireNonNull(title);
    }

    public Instant getAccessTime() {
        return accessTime;
    }

    public Set<String> getLinks() {
        return links;
    }

    public String getLocation() {
        return location;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * A builder for constructing a {@link WebPage}.
     */
    public static class Builder {

        private Instant accessTime;
        private Set<String> links = new HashSet<>();
        private String location;
        private String text;
        private String title;

        /**
         * Creates a new builder from an existing {@link WebPage}.
         *
         * @param webPage the {@link WebPage} from which to copy data.
         * @return the new builder.
         */
        public static Builder from(
                @Nonnull WebPage webPage
        ) {
            return builder()
                    .withAccessTime(webPage.getAccessTime())
                    .withLinks(webPage.getLinks())
                    .withLocation(webPage.getLocation())
                    .withText(webPage.getText())
                    .withTitle(webPage.getTitle());
        }

        /**
         * Creates a new {@link WebPage} builder.
         *
         * @return the new builder.
         */
        public static Builder builder() {
            return new Builder();
        }

        public Instant getAccessTime() {
            return accessTime;
        }

        /**
         * Sets the accessTime and returns the builder.
         *
         * @param accessTime the accessTime to set.
         * @return the builder.
         */
        public Builder withAccessTime(
                @Nullable Instant accessTime
        ) {
            this.accessTime = accessTime;
            return this;
        }

        public Set<String> getLinks() {
            return links;
        }

        /**
         * Sets the links and returns the builder.
         *
         * @param links the non-null links to set.
         * @return the builder.
         */
        public Builder withLinks(
                @Nonnull Set<String> links
        ) {
            this.links = requireNonNull(links);
            return this;
        }

        public String getLocation() {
            return location;
        }

        /**
         * Sets the location and returns the builder.
         *
         * @param location the location to set.
         * @return the builder.
         */
        public Builder withLocation(
                @Nullable String location
        ) {
            this.location = location;
            return this;
        }

        public String getText() {
            return text;
        }

        /**
         * Sets the text and returns the builder.
         *
         * @param text the text to set.
         * @return the builder.
         */
        public Builder withText(
                @Nullable String text
        ) {
            this.text = text;
            return this;
        }

        public String getTitle() {
            return title;
        }

        /**
         * Sets the title and returns the builder.
         *
         * @param title the title to set.
         * @return the builder.
         */
        public Builder withTitle(
                @Nullable String title
        ) {
            this.title = title;
            return this;
        }

        /**
         * Creates a new {@link WebPage} from the builder's properties.
         *
         * @return the new {@link WebPage}.
         */
        public WebPage build() {
            return new WebPage(
                    getAccessTime(),
                    getLinks(),
                    getLocation(),
                    getText(),
                    getTitle()
            );
        }

        @Override
        public boolean equals(Object o) {
            return EqualsBuilder.reflectionEquals(this, o);
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

    }

}
