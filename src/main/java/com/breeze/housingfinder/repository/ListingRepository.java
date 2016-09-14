package com.breeze.housingfinder.repository;

import com.breeze.housingfinder.scrape.RawListing;
import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ListingRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListingRepository.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public ListingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
    }

    public RawListing loadRawListing(String key) {
        String querySql = "SELECT * FROM HOUSING.RAW_LISTINGS L where L.key = ?";
        return jdbcTemplate.queryForObject(querySql, RawListing.class, key);
    }

    public void saveRawListing(RawListing rawListing) {
        String checkIfListingExistsSql = "SELECT count(*) from HOUSING.RAW_LISTINGS L WHERE L.key = ?";
        Integer listingCount = jdbcTemplate.queryForObject(checkIfListingExistsSql, Integer.class, rawListing.getKey());
        if (listingCount == 0) {
            String insertListingSql = "INSERT INTO HOUSING.RAW_LISTINGS (key, url, title, publishedDate, description) " +
                    "VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(insertListingSql, rawListing.getKey(), rawListing.getUrl(), rawListing.getTitle(),
                    rawListing.getPublishedDate(), rawListing.getDescription());
        }
    }

    public List<String> saveRawListings(List<RawListing> rawListings) {
        // gather list of keys
        List<String> allKeys = rawListings.stream().map(RawListing::getKey).collect(Collectors.toList());
        // query for any keys that exist in the table (select from where in (..))
        String getKeysAlreadySavedSql = "SELECT key from HOUSING.RAW_LISTINGS L where L.key in (:keys)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("keys", allKeys);
        List<String> existingKeysList = namedJdbcTemplate.queryForList(getKeysAlreadySavedSql, parameters, String.class);
        Set<String> existingKeysSet = ImmutableSet.copyOf(existingKeysList);
        // filter those keys/entries out from the list of entries to be saved
        List<RawListing> listingsToBeAdded = rawListings.stream()
                .filter((listing) -> !existingKeysSet.contains(listing.getKey()))
                .collect(Collectors.toList());
        // save the rest
        String insertListingSql = "INSERT INTO HOUSING.RAW_LISTINGS (key, url, title, publishedDate, description) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(insertListingSql, listingsToBeAdded, listingsToBeAdded.size(),
                (ps, listing) -> {
                    ps.setString(1, listing.getKey());
                    ps.setString(2, listing.getUrl());
                    ps.setString(3, listing.getTitle());
                    ps.setString(4, listing.getPublishedDate());
                    ps.setString(5, listing.getDescription());
                });
        return listingsToBeAdded.stream().map(RawListing::getKey).collect(Collectors.toList());
    }

}
