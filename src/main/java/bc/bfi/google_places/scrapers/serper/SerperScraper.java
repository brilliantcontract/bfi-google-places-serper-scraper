package bc.bfi.google_places.scrapers.serper;

import bc.bfi.google_places.CsvStorage;
import bc.bfi.google_places.JsonStorage;
import bc.bfi.google_places.Place;
import bc.bfi.google_places.scrapers.Config;
import bc.bfi.google_places.scrapers.Queries;
import java.util.ArrayList;
import java.util.List;
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

    public SerperScraper(final Queries queries, final Config config) {
        this.queries = queries;
        this.config = config;
    }

    public void startScrape() {
        LOGGER.info("Scraping strat");
        LOGGER.log(Level.INFO, "Total queries: {0}", this.queries.getQueries().size());

        for (String location : this.queries.getQueries()) {
            scrapeLocation(location);
        }
    }

    private void scrapeLocation(String query) {
        LOGGER.log(Level.INFO, "Scrape: {0}", query);

        List<Place> places = new ArrayList<>();
        Integer pageNumber = 1;
        while (true) {
            String jsonResponse = downloader.searchPlaces(query, pageNumber, config.getLocation(), config.getCountry(), config.getLanguage());
            jsonStorage.save(jsonResponse);
            places = parser.parse(jsonResponse, query);
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

}
