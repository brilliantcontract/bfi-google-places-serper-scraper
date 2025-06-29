package bc.bfi.google_places.scrapers.google_places;

import bc.bfi.google_places.Place;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Parser {

    private Document doc;

    public Place parsePlaceProfile(final String webPage, final WebElement placeCard) {
        Place place = new Place();

        this.doc = Jsoup.parse(webPage);

        place.setQuery(fetchText("title").replace(" - Google Search", ""));

        // Header.
        place.setName(fetchText("div.SPZz6b > h2"));
        place.setRate(fetchText("div.kp-header span.yi40Hd.YrbPuc"));
        place.setRateCounter(fetchText("div.kp-header span.RDApEe.YrbPuc"));
        place.setType(fetchText("div.kp-header span.YhemCb"));
        place.setWebsite(fetchLink("div.zhZ3gf.zYjZSc a.n1obkb.mI8Pwc"));

        // Footer.
        place.setFullAddress(fetchText("div.i4J0ge span.FoJoyf:contains(Address) + span"));
        place.setPhone(fetchText("div.i4J0ge span.FoJoyf:contains(Phone) + span"));
        place.setSocialLinks(fetchLinks("div.CQKTwc.wDYxhc g-link > a"));

        // <div style="display:none" data-id="2740744457321929683" data-lat="41.4205563" data-lng="-71.7894252" class="rllt__mi"></div>
        WebElement element = placeCard.findElement(By.cssSelector("div.rllt__mi"));
        if(element != null) {
            String chunk = element.getAttribute("outerHTML");
            place.setCid(chunk.replaceAll(".+data-id=\"", "").replaceAll("\".+", ""));
            place.setLatitude(chunk.replaceAll(".+data-lat=\"", "").replaceAll("\".+", ""));
            place.setLongitude(chunk.replaceAll(".+data-lng=\"", "").replaceAll("\".+", ""));
        }
        
        place.setPermanentlyClosed(fetchText(""));

        return place;
    }

    private String fetchText(String cssSelector) {
        if (cssSelector == null || cssSelector.isEmpty()) {
            return "";
        }

        Element element = this.doc.selectFirst(cssSelector);
        if (element == null) {
            return "";
        }

        return element.text();
    }

    private String fetchLink(String cssSelector) {
        if (cssSelector == null || cssSelector.isEmpty()) {
            return "";
        }

        Element element = this.doc.selectFirst(cssSelector);
        if (element == null) {
            return "";
        }

        return element.attr("href");
    }

    private String fetchLinks(String cssSelector) {
        if (cssSelector == null || cssSelector.isEmpty()) {
            return "";
        }

        Elements elementLinks = this.doc.select(cssSelector);
        if (elementLinks.isEmpty()) {
            return "";
        }

        String links = "";

        for (Element elementLink : elementLinks) {
            links += elementLink.attr("href") + "|";
        }

        if (!links.isEmpty()) {
            links = links.substring(0, links.length() - 2);
        }

        return links;
    }

}
