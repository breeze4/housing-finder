package com.breeze.housingfinder.scrape;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.net.URL;

public class Scraper {

    private static final String TARGET_URL = "http://seattle.craigslist.org/search/see/apa?" +
            "min_price=1200" +
            "&max_price=2600" +
            "&bedrooms=2" +
            "&housing_type=9" +
            "&format=rss";

    public static void main(String[] args) {

    }

    private void scrapeListings() {
        try {
            URL feedUrl = new URL(TARGET_URL);

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            System.out.println(feed);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR: " + ex.getMessage());
        }
    }
}
