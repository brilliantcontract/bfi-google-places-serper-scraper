package bc.bfi.google_places.scrapers.google_places;

import bc.bfi.google_places.CsvStorage;
import bc.bfi.google_places.Place;
import bc.bfi.google_places.scrapers.Queries;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GooglePlaceScraper {

    private static final Logger LOGGER = Logger.getLogger(GooglePlaceScraper.class.getName());

    private final ChromeDownloader downloader = new ChromeDownloader();
    private final CsvStorage csvStorage = new CsvStorage();
    private final Parser parser = new Parser();
    private final Queries queries;

    public GooglePlaceScraper(Queries queries) {
        this.queries = queries;
    }

    public void startScrape() {
        LOGGER.info("Scraping strat");

        for (String location : this.queries.getQueries()) {
            scrapeLocation(location);
        }
    }

    private void scrapeLocation(String query) {
        downloader.createDriver();
        LOGGER.log(Level.INFO, "Scrape: {0}", query);

        downloader.load(query);

        while (true) {
            // Go through all 10 places shown on the page.
            while (downloader.loadNextPlaceProfile()) {
                String webPage = downloader.fetchWebPage();
                Place place = parser.parsePlaceProfile(webPage, downloader.getPlaceCard());
                csvStorage.append(place);
            }
            
            if(downloader.hasNextPage()){
               downloader.loadNextPage(); 
            } else {
                break;
            }
        }

        downloader.quit();
    }

}
