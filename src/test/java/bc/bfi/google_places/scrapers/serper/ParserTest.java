package bc.bfi.google_places.scrapers.serper;

import bc.bfi.google_places.Place;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    @Test
    public void test() {
        Parser parser = new Parser();
        String jsonString = "{ \"searchParameters\": { \"q\": \"churches in Toronto\", \"type\": \"places\", \"location\": \"United States\", \"engine\": \"google\", \"gl\": \"us\" }, \"places\": [ { \"position\": 1, \"title\": \"St. Michael's Cathedral Basilica\", \"address\": \"65 Bond St, Toronto, ON M5B 1X1, Canada\", \"latitude\": 43.654921699999996, \"longitude\": -79.3774486, \"rating\": 4.8, \"ratingCount\": 2000, \"category\": \"Catholic cathedral\", \"website\": \"https://www.stmichaelscathedral.com/\", \"cid\": \"100906673614546488\" } ], \"credits\": 1 }";
        List<Place> places = parser.parse(jsonString, "churches in Toronto");
        
        for(Place place : places) {
            place.print();
        }
    }
    
}
