package com.breeze.housingfinder.parse;

import com.breeze.housingfinder.scrape.RawListing;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Parser {

    private static final String BODY_SECTION_SELECTOR = "section.body";
    private static final String GALLERY_SECTION_SELECTOR = "figure.iw";
    private static final String MAP_SECTION_SELECTOR = ".userbody>.mapAndAttrs";
    private static final String ADDRESS_SELECTOR = MAP_SECTION_SELECTOR + ".mapaddress";

    public Parser() {

    }

    public ParsedListing parseListing(RawListing rawListing) {
        String url = rawListing.getUrl();
        try {
            Document doc = Jsoup.connect(url).get();
            // gather the image IDs
            // get the base image URL pattern for this post:
            // https://images.craigslist.org/<extra_gunk>_<img_id>_600x450.jpg
            // build image URLs for embedding in email

            // get the map and attributes
            //

            // get the post body
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
