package bc.bfi.google_places.scrapers.serpapi;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicHeader;

import javax.json.Json;
import javax.json.JsonObject;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.net.URIBuilder;

class HttpDownloader {

    private static final String API_KEY = "896d0da522ed0f7c64c77421d0035734d8640a56b26fb1066b57883b24e32955";
    private static final String PLACES_URL = "https://serpapi.com/search";
    private static final Logger LOGGER = Logger.getLogger(HttpDownloader.class.getName());

    String searchPlaces(String query, Integer pageNumber, String googleDomain, String country, String language) {
        URI uri = generateUriRequest(query, pageNumber, googleDomain, country, language);

        return sendGetRequest(uri);
    }

    private URI generateUriRequest(String query, Integer pageNumber, String googleDomain, String country, String language) {
        URI uri = null;
        try {
            uri = new URIBuilder(PLACES_URL)
                    .addParameter("type", "search")
                    .addParameter("no_cache", "true")
                    .addParameter("engine", "google_local")
                    .addParameter("api_key", API_KEY)
                    .addParameter("q", query)
                    .addParameter("google_domain", googleDomain)
                    .addParameter("gl", country)
                    .addParameter("hl", language)
                    .addParameter("start", Integer.toString((pageNumber - 1) * 20)) // Convert page number to number of offset positions.
                    .build();
        } catch (URISyntaxException ex) {
            LOGGER.log(Level.SEVERE, "Cannot create URI for serpapi.com service.", ex);
            JOptionPane.showMessageDialog(null, "Cannot create URI for serpapi.com service.", "Error", JOptionPane.ERROR_MESSAGE);
            throw new IllegalStateException("Cannot create URI for serpapi.com service.", ex);
        }

        LOGGER.log(Level.INFO, "Requesting URI: {0}", uri);

        return uri;
    }

    private String sendGetRequest(URI uri) {
        try (CloseableHttpClient httpClient = HttpClients.custom().build()) {

            HttpGet request = new HttpGet(uri);
            request.addHeader(new BasicHeader("Content-Type", "application/json"));

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, StandardCharsets.UTF_8);
                }
            } catch (ParseException ex) {
                LOGGER.log(Level.SEVERE, "API call to " + uri + " failed", ex);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "API call to " + uri + " failed", ex);
        }

        return null;
    }
}
