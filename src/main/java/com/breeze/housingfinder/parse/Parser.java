package com.breeze.housingfinder.parse;

import com.breeze.housingfinder.scrape.RawListing;
import com.breeze.housingfinder.scrape.Scraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static javafx.beans.binding.Bindings.select;

public class Parser {

    private static final Logger LOGGER = LoggerFactory.getLogger(Parser.class);

    private static final String BODY_SECTION_SELECTOR = "section.body";
    private static final String GALLERY_SECTION_SELECTOR = "figure.iw";
    private static final String MAP_SECTION_SELECTOR = ".userbody>.mapAndAttrs";
    private static final String ADDRESS_SELECTOR = "div.mapaddress";
    private static final String MAP_LINK_SELECTOR = ".mapaddress a";
    private static final String POST_BODY_SELECTOR = "#postingbody";

    private static final Pattern IMG_URL_PATTERN = Pattern.compile("" +
            "https://images.craigslist.org/(\\w+?)_(\\w+?)_(\\w+?).jpg");
    private static final String IMG_URL_FORMAT_STR = "https://images.craigslist.org/%s_%s_%s.jpg";

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, " +
            "like Gecko) Chrome/52.0.2743.116 Safari/537.36";
    private static final String REFERRER = "http://www.google.com";

    public Parser() {

    }

    public ParsedListing parseListing(RawListing rawListing) {
        String url = rawListing.getUrl();
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(USER_AGENT).referrer(REFERRER).get();

            // get the map and attributes
            Elements mapAndAttrs = doc.select(MAP_SECTION_SELECTOR);
            Element addressElement = mapAndAttrs.select(ADDRESS_SELECTOR).first();
            String address = addressElement.text();
            Element mapLinkElement = mapAndAttrs.select(MAP_LINK_SELECTOR).first();
            String mapLinkUrl = mapLinkElement.attr("href");

            // get the post body
            Element postBody = doc.select(POST_BODY_SELECTOR).first();
            String postBodyText = postBody.text();

            ParsedListing parsedListing = new ParsedListing(rawListing.getKey(), rawListing.getUrl(),
                    rawListing.getTitle(), rawListing.getPublishedDate(), postBodyText, mapLinkUrl, address);
            return parsedListing;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void parseImages(Document doc) {
        // gather the image IDs
        Element imageGallery = doc.select(GALLERY_SECTION_SELECTOR).first();
        // get the base image URL pattern for this post:
        // https://images.craigslist.org/<extra_gunk>_<img_id>_600x450.jpg
        Elements firstImage = imageGallery.select("div.swipe-wrap img");
        String imgSrcUrl = firstImage.attr("src");
        String imgUrlPrefixGroup = "ERROR";
        String imgIdGroup = "ERROR";
        String imgDimsGroup = "ERROR";
        Matcher matcher = IMG_URL_PATTERN.matcher(imgSrcUrl);
        if (matcher.matches()) {
            imgUrlPrefixGroup = matcher.group(2);
            imgIdGroup = matcher.group(3);
            imgDimsGroup = matcher.group(4);
        } else {
            LOGGER.error("could not parse img URL {}", imgSrcUrl);
        }
        List<Element> thumbnailLinks = imageGallery.select("#id>a");
        List<String> imageIds = thumbnailLinks.stream()
                .map(element -> element.attr("data-imgid"))
                .collect(Collectors.toList());
        // build image URLs for embedding in email
    }
}
