package com.breeze.housingfinder.repository;

import com.breeze.housingfinder.scrape.RawListing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class ListingRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ListingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveRawListing(RawListing rawListing) {
        String checkIfListingExistsSql = "SELECT count(*) from HOUSING.RAW_LISTINGS L WHERE L.key = ?";
        Integer listingCount = jdbcTemplate.queryForObject(checkIfListingExistsSql, Integer.class, rawListing.getKey());
        if (listingCount == 0) {
            String insertListingSql = "INSERT INTO HOUSING.RAW_LISTINGS (key, url, data) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertListingSql, rawListing.getKey(), rawListing.getUrl(), rawListing.getData());
        }
    }

}
