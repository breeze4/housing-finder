package com.breeze.housingfinder.parse;

import com.breeze.housingfinder.util.AbstractValueObject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ParsedListing extends AbstractValueObject {

    private final String key;
    private final String url;
    private final String title;
    private final String publishedDate;
    private final String postBody;
    private final String mapLinkUrl;
    private final String address;

    @JsonCreator
    public ParsedListing(
            @JsonProperty("key") String key,
            @JsonProperty("url") String url,
            @JsonProperty("title") String title,
            @JsonProperty("publishedDate") String publishedDate,
            @JsonProperty("postBody") String postBody,
            @JsonProperty("mapLinkUrl") String mapLinkUrl,
            @JsonProperty("address") String address) {
        this.key = key;
        this.url = url;
        this.title = title;
        this.publishedDate = publishedDate;
        this.postBody = postBody;
        this.mapLinkUrl = mapLinkUrl;
        this.address = address;
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

    public String getPostBody() {
        return postBody;
    }

    public String getMapLinkUrl() {
        return mapLinkUrl;
    }

    public String getAddress() {
        return address;
    }
}
