package bc.bfi.google_places.scrapers.serper;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicHeader;

import javax.json.Json;
import javax.json.JsonObject;

class HttpDownloader {

    private static final String API_KEY = "0881769d8ef5e996ba41abc92ddb186b00d1a9b1";
    private static final String PLACES_URL = "https://google.serper.dev/places";
    private static final Logger LOGGER = Logger.getLogger(HttpDownloader.class.getName());

    String searchPlaces(String query, Integer pageNumber, String location, String country, String language) {
        String jsonQuery = generateJsonRequest(query, pageNumber, location, country, language);

        return sendPostRequest(PLACES_URL, jsonQuery);
    }

    private String generateJsonRequest(String query, Integer pageNumber, String location, String country, String language) {
        JsonObject json = Json.createObjectBuilder()
                .add("q", query)
                .add("location", location)
                .add("gl", country)
                .add("hl", language)
                .add("page", pageNumber)
                .build();

        String jsonString = json.toString();

        System.out.println("JSON request to service: " + jsonString);

        return jsonString;
    }

    private String sendPostRequest(String url, String jsonBody) {
        try (CloseableHttpClient httpClient = HttpClients.custom().build()) {
            HttpPost request = new HttpPost(url);
            request.addHeader(new BasicHeader("X-API-KEY", API_KEY));
            request.addHeader(new BasicHeader("Content-Type", "application/json"));
            request.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, StandardCharsets.UTF_8);
                }
            } catch (ParseException ex) {
                LOGGER.log(Level.SEVERE, "Exception during API call.", ex);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Exception during API call.", ex);
        }

        return null;
    }
}
