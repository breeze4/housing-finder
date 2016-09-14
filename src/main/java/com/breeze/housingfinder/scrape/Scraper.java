package com.breeze.housingfinder.scrape;

import com.breeze.housingfinder.repository.ListingRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class Scraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scraper.class);

    private static final String TARGET_URL = "http://seattle.craigslist.org/search/see/apa?" +
            "min_price=1200" +
            "&max_price=2600" +
            "&bedrooms=2" +
            "&housing_type=9" +
            "&format=rss";
    private static final Pattern KEY_PATTERN = Pattern.compile("[\\S]+?(\\d+?).html");
    private final ListingRepository listingRepository;

    @Autowired
    public Scraper(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public List<String> scrapeListings() {
        try {
            URL feedUrl = new URL(TARGET_URL);

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            List<SyndEntry> entries = feed.getEntries();
            List<RawListing> rawListings = entries.stream().map(this::scrapeListing).collect(Collectors.toList());
            List<String> savedKeys = listingRepository.saveRawListings(rawListings);
            return savedKeys;

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR: " + ex.getMessage());
        }
        return Collections.emptyList();
    }

    private RawListing scrapeListing(SyndEntry entry) {
        String url = entry.getUri();
        String key = "";
        Matcher matcher = KEY_PATTERN.matcher(url);
        if (matcher.matches()) {
            key = matcher.group(1);
        } else {
            LOGGER.error("Error while parsing URL {}, not valid", url);
        }

        String title = entry.getTitle();
        String publishedDate = entry.getPublishedDate().toString();
        String description = entry.getDescription().getValue();

        return new RawListing(key, url, title, publishedDate, description);
    }
}
