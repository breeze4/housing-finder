package com.breeze.housingfinder.web;

import com.breeze.housingfinder.scrape.Scraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
public class AppController {

    private final Scraper scraper;

    @Autowired
    public AppController(Scraper scraper) {
        this.scraper = scraper;
    }

    @RequestMapping("/scrape")
    public List<String> index() {
        List<String> savedListingKeys = scraper.scrapeListings();
        return savedListingKeys;
    }

}