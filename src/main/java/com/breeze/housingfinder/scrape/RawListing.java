package com.breeze.housingfinder.scrape;

import com.breeze.housingfinder.util.AbstractValueObject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RawListing extends AbstractValueObject {

    private final String key;
    private final String url;
    private final String title;
    private final String publishedDate;
    private final String description;

    @JsonCreator
    public RawListing(
            @JsonProperty("key") String key,
            @JsonProperty("url") String url,
            @JsonProperty("title") String title,
            @JsonProperty("publishedDate") String publishedDate,
            @JsonProperty("description") String description) {
        this.key = key;
        this.url = url;
        this.title = title;
        this.publishedDate = publishedDate;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getDescription() {
        return description;
    }
}
