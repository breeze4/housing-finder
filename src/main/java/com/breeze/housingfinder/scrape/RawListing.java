package com.breeze.housingfinder.scrape;

import com.breeze.housingfinder.util.AbstractValueObject;

public class RawListing extends AbstractValueObject {

    private final String key;
    private final String url;
    private final String data;

    public RawListing(String key, String url, String data) {
        this.key = key;
        this.url = url;
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public String getUrl() {
        return url;
    }

    public String getData() {
        return data;
    }
}
