package bc.bfi.google_places.scrapers.serper;

import bc.bfi.google_places.CsvStorage;
import bc.bfi.google_places.JsonStorage;
import bc.bfi.google_places.Place;
import bc.bfi.google_places.scrapers.Config;
import bc.bfi.google_places.scrapers.Queries;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerperScraper {

    private static final Logger LOGGER = Logger.getLogger(SerperScraper.class.getName());

    private final HttpDownloader downloader = new HttpDownloader();
    private final CsvStorage csvStorage = new CsvStorage();
    private final JsonStorage jsonStorage = new JsonStorage();
    private final Parser parser = new Parser();
    private final Queries queries;
    private final Config config;

    private final Map<String, Integer> emptyCounters = new HashMap<>();
    private final Map<String, Integer> emptyLimits = new HashMap<>();

    public SerperScraper(final Queries queries, final Config config) {
        this.queries = queries;
        this.config = config;

        emptyLimits.put("fullAddress", 50);
        emptyLimits.put("latitude", 30);
        emptyLimits.put("longitude", 30);
        emptyLimits.put("name", 30);
        emptyLimits.put("phone", 100);
        emptyLimits.put("query", 20);
        emptyLimits.put("rate", 50);
        emptyLimits.put("rateCounter", 50);
        emptyLimits.put("type", 30);
        emptyLimits.put("cid", 30);
        emptyLimits.put("website", 100);

        for (String key : emptyLimits.keySet()) {
            emptyCounters.put(key, 0);
        }
    }

    public void startScrape() {
        LOGGER.info("Scraping started");
        LOGGER.log(Level.INFO, "Total queries: {0}", this.queries.getQueries().size());

        for (String location : this.queries.getQueries()) {
            scrapeLocation(location);
        }
    }

    private void scrapeLocation(String query) {
        LOGGER.log(Level.INFO, "Scraping query: {0}", query);

        List<Place> places = new ArrayList<>();
        Integer pageNumber = 1;
        while (true) {
            String jsonResponse = downloader.searchPlaces(query, pageNumber, config.getLocation(), config.getCountry(), config.getLanguage());
            jsonStorage.save(jsonResponse);
            places = parser.parse(jsonResponse, query);
            checkEmptyValues(places);
            csvStorage.append(places);

            if (places.size() != 10) {
                break;
            }
            if (pageNumber >= 19) {
                break;
            }
            pageNumber += 1;
        }
    }

    void checkEmptyValues(List<Place> places) {
        for (Place place : places) {
            increment("fullAddress", place.getFullAddress());
            increment("latitude", place.getLatitude());
            increment("longitude", place.getLongitude());
            increment("name", place.getName());
            increment("phone", place.getPhone());
            increment("query", place.getQuery());
            increment("rate", place.getRate());
            increment("rateCounter", place.getRateCounter());
            increment("type", place.getType());
            increment("cid", place.getCid());
            increment("website", place.getWebsite());
        }
    }

    private void increment(String field, String value) {
        if (value == null || value.trim().isEmpty()) {
            int count = emptyCounters.get(field) + 1;
            emptyCounters.put(field, count);
            if (count > emptyLimits.get(field)) {
                LOGGER.severe("Serper returns many empty values for one of field.");
                throw new RuntimeException("Serper returns many empty values for one of field.");
            }
        } else {
            emptyCounters.put(field, 0);
        }
    }

}
