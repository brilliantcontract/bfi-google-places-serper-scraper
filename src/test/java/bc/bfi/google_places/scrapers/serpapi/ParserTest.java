package bc.bfi.google_places.scrapers.serpapi;

import bc.bfi.google_places.Place;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    @Test
    public void test() throws IOException {
        Parser parser = new Parser();
        String jsonString = new String(Files.readAllBytes(Paths.get("test/serpapi-response.json")), StandardCharsets.UTF_8);
        List<Place> places = parser.parse(jsonString, "churches in Rhode Island");
        
        for(Place place : places) {
            place.print();
            System.out.println("\n---------------------------------\n");
        }
    }
    
}
