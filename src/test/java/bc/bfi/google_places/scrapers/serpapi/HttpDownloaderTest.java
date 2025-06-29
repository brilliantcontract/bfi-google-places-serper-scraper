package bc.bfi.google_places.scrapers.serpapi;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

public class HttpDownloaderTest {
    
    @Ignore
    @Test
    public void test() {
        HttpDownloader downloader = new HttpDownloader();
        String jsonResponse = downloader.searchPlaces("churches in Rhode Island", 1, "google.com", "us", "en");
        System.out.println("Response: " + jsonResponse);
    }
    
}
