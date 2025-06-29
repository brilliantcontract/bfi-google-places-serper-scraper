package bc.bfi.google_places.scrapers.serpapi;

import bc.bfi.google_places.CsvStorage;
import bc.bfi.google_places.JsonStorage;
import bc.bfi.google_places.Place;
import bc.bfi.google_places.scrapers.Queries;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerpapiScraper {

    private static final Logger LOGGER = Logger.getLogger(SerpapiScraper.class.getName());

    private final HttpDownloader downloader = new HttpDownloader();
    private final CsvStorage csvStorage = new CsvStorage();
    private final JsonStorage jsonStorage = new JsonStorage();
    private final Parser parser = new Parser();
    private final Queries queries;

    public SerpapiScraper(Queries queries) {
        this.queries = queries;
    }

    public void startScrape() {
        LOGGER.info("Scraping strat");

        for (String location : this.queries.getQueries()) {
            scrapeLocation(location);
        }
    }

    private void scrapeLocation(String query) {
        LOGGER.log(Level.INFO, "Scrape: {0}", query);

        List<Place> places = new ArrayList<>();
        Integer pageNumber = 1;
        while (true) {
            String jsonResponse = downloader.searchPlaces(query, pageNumber, "google.com", "us", "en");
            jsonStorage.save(jsonResponse);
            places = parser.parse(jsonResponse, query);
            csvStorage.append(places);

            if (places.size() != 20) {
                break;
            }
            pageNumber += 1;
        }
    }

}
