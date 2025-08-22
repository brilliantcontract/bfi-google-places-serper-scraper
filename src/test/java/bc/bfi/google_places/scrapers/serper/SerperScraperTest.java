package bc.bfi.google_places.scrapers.serper;

import bc.bfi.google_places.Place;
import bc.bfi.google_places.scrapers.Config;
import bc.bfi.google_places.scrapers.Queries;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

public class SerperScraperTest {

    @Test
    public void shouldThrowWhenQueryEmptyExceedsLimit() {
        SerperScraper scraper = new SerperScraper(new Queries(), new Config());

        List<Place> firstBatch = buildPlaces(20);
        scraper.checkEmptyValues(firstBatch);

        List<Place> nextBatch = buildPlaces(1);
        try {
            scraper.checkEmptyValues(nextBatch);
            fail("Expected RuntimeException");
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage(), is("Serper returns many empty values for one of field."));
        }
    }

    private List<Place> buildPlaces(int count) {
        List<Place> places = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Place p = new Place();
            p.setFullAddress("addr");
            p.setLatitude("1");
            p.setLongitude("1");
            p.setName("name");
            p.setPhone("123");
            p.setRate("1");
            p.setRateCounter("1");
            p.setType("type");
            p.setCid("cid");
            p.setWebsite("site");
            places.add(p);
        }
        return places;
    }
}
