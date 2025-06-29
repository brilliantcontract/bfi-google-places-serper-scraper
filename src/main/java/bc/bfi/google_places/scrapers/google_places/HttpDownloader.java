package bc.bfi.google_places.scrapers.google_places;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.Socket;
import java.net.SocketAddress;

public class HttpDownloader {

    public String download(String url) throws Exception {
        // SOCKS5 proxy settings
        final String proxyHost = "54.215.43.55";
        final int proxyPort = 4007;
        final String proxyUser = "";
        final String proxyPassword = "";

        // Set the default authenticator for SOCKS proxy authentication
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
            }
        });

        // Configure the custom socket factory for SOCKS5
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager() {
            protected Socket createSocket(Timeout connectTimeout) throws IOException {
                SocketAddress proxyAddr = new InetSocketAddress(proxyHost, proxyPort);
                Proxy proxy = new Proxy(Proxy.Type.SOCKS, proxyAddr);
                return new Socket(proxy);
            }
        };

        String webPage = "";
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build()) {

            // Prepare GET request
            HttpGet request = new HttpGet(new URIBuilder(url).build());

            // Execute request
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                System.out.println("Response Code: " + response.getCode());
                webPage = EntityUtils.toString(response.getEntity());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return webPage;
    }
}
