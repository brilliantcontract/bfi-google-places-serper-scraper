package bc.bfi.google_places.scrapers.serper;

import bc.bfi.google_places.Place;
import bc.bfi.google_places.CsvStorage;
import bc.bfi.google_places.JsonStorage;
import bc.bfi.google_places.scrapers.Config;
import bc.bfi.google_places.scrapers.Queries;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Verifies that SerperScraper populates duration for scraped places.
 */
public class SerperScraperTest {

    @Test
    public void setsDurationForEachPlace() throws Exception {
        Queries queries = new Queries();
        queries.loadQueries("test");
        Config config = new Config();
        config.load("us", "en", "Austin");
        SerperScraper scraper = new SerperScraper(queries, config);

        HttpDownloader downloader = mock(HttpDownloader.class);
        when(downloader.searchPlaces(anyString(), anyInt(), anyString(), anyString(), anyString()))
                .thenAnswer(invocation -> { Thread.sleep(50); return "{}"; });

        Parser parser = mock(Parser.class);
        Place place = new Place();
        when(parser.parse(anyString(), anyString())).thenReturn(Collections.singletonList(place));

        CsvStorage csv = mock(CsvStorage.class);
        JsonStorage json = mock(JsonStorage.class);

        setField(scraper, "downloader", downloader);
        setField(scraper, "parser", parser);
        setField(scraper, "csvStorage", csv);
        setField(scraper, "jsonStorage", json);

        scraper.startScrape();

        assertThat(Long.parseLong(place.getDuration()), greaterThan(0L));
    }

    private void setField(Object target, String name, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(target, value);
    }
}
