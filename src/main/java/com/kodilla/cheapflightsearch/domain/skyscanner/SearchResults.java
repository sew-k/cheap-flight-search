package com.kodilla.cheapflightsearch.domain.skyscanner;

import java.util.List;
import java.util.Map;

public class SearchResults {
    private String sessionToken;

    private String status;

    private Map<String, Itinerary> sortingOptions;

    private SearchResults(String sessionToken, String status, Map<String, Itinerary> sortingOptions) {
        this.sessionToken = sessionToken;
        this.status = status;
        this.sortingOptions = sortingOptions;
    }

    public static class SearchResultsBuilder {
        private String sessionToken;

        private String status;

        private Map<String, Itinerary> sortingOptions;

        public SearchResultsBuilder sessionToken(String sessionToken) {
            this.sessionToken = sessionToken;
            return this;
        }
        public SearchResultsBuilder status(String status) {
            this.status = status;
            return this;
        }
        public SearchResultsBuilder sortingOptions(Map<String, Itinerary> sortingOptions) {
            this.sortingOptions = sortingOptions;
            return this;
        }
        public SearchResults build() {              //TODO validation
            return new SearchResults(sessionToken, status, sortingOptions);
        }
    }
}
