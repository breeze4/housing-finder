package com.breeze.housingfinder.scrape;

import com.breeze.housingfinder.repository.ListingRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.List;

public class Scraper {

    private static final String TARGET_URL = "http://seattle.craigslist.org/search/see/apa?" +
            "min_price=1200" +
            "&max_price=2600" +
            "&bedrooms=2" +
            "&housing_type=9" +
            "&format=rss";
    private final ListingRepository listingRepository;

    @Autowired
    public Scraper(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public static void main(String[] args) {
        Scraper scraper = new Scraper(null);
        scraper.scrapeListings();
    }

    private void scrapeListings() {
        try {
            URL feedUrl = new URL(TARGET_URL);

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            List<SyndEntry> entries = feed.getEntries();
            for(SyndEntry entry : entries) {
                System.out.println(entry);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR: " + ex.getMessage());
        }
    }
}
