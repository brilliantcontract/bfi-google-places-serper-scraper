package bc.bfi.google_places.scrapers.serpapi;

import bc.bfi.google_places.scrapers.serper.*;
import bc.bfi.google_places.Place;
import java.util.ArrayList;
import java.util.List;
import javax.json.*;
import java.io.StringReader;

class Parser {

    List<Place> parse(String json, String query) {
        List<Place> places = new ArrayList<>();

        // Parse JSON
        JsonObject jsonObject;
        try (JsonReader jsonReader = Json.createReader(new StringReader(json))) {
            jsonObject = jsonReader.readObject();
        }

        // Convert to a list of JSON objects
        JsonArray placesArray = jsonObject.getJsonArray("local_results");
        if (placesArray == null) {
            return places;
        }

        for (JsonValue value : placesArray) {
            Place place = new Place();
            JsonObject jsonPlace = value.asJsonObject();

            place.setName(readString("title", jsonPlace));
            place.setFullAddress(readString("address", jsonPlace));
            place.setLatitude(readNumber("latitude", jsonPlace.getJsonObject("gps_coordinates")));
            place.setLongitude(readNumber("longitude", jsonPlace.getJsonObject("gps_coordinates")));
            place.setRate(readNumber("rating", jsonPlace));
            place.setRateCounter(readNumber("reviews", jsonPlace));
            place.setType(readString("type", jsonPlace));
            place.setWebsite(readString("website", jsonPlace.getJsonObject("links")));
            place.setCid(readString("place_id", jsonPlace));
            place.setPhone(readString("phone", jsonPlace));
            place.setQuery(query);

            places.add(place);
        }

        return places;
    }

    private String readString(String key, JsonObject jsonPlace) {
        String value = "";

        try {
            value = jsonPlace.getString(key);
        } catch (Exception ignore) {
        }

        return value;
    }

    private String readNumber(String key, JsonObject jsonPlace) {
        String value = "";

        try {
            value = jsonPlace
                    .getJsonNumber(key)
                    .bigDecimalValue()
                    .toPlainString();
        } catch (Exception ignore) {
        }

        return value;
    }
}
