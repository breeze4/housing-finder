package com.breeze.housingfinder.web;

import com.breeze.housingfinder.scrape.Scraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class AppController {

    private final Scraper scraper;

    @Autowired
    public AppController(Scraper scraper) {
        this.scraper = scraper;
    }

    @RequestMapping("/scrape")
    public String index() {
        scraper.scrapeListings();
        return "finished scraping";
    }

}