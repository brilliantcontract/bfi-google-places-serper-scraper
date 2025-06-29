package bc.bfi.google_places.scrapers.google_places;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ChromeDownloader {

    private static final Logger LOGGER = Logger.getLogger(ChromeDownloader.class.getName());
    //private static final String URL_PREFIX = "https://www.google.com/search?q=";
    private static final String URL_PREFIX = "https://54.215.43.55:4002/proxy/load?key=my-little-secret&only-proxy-provider=zyte.com&url=https://www.google.com/search?q=";
    private static final String URL_LOCATION_SUFIX = "&hl=en&gl=us";
    private static final String URL_SUFIX = "&sca_esv=4808aced13c4ef62&biw=1920&bih=955&tbm=lcl&sxsrf=AHTn8zq30HfdCASL1p4IpIUV9K8m7Y4B4Q%3A1740678003470&ei=c6PAZ7mpHIrIwPAP1_HdgQs&ved=0ahUKEwj5rsLwsuSLAxUKJBAIHdd4N7AQ4dUDCAs&uact=5&oq=churches+Belleville%2C+Rhode+Island&gs_lp=Eg1nd3Mtd2l6LWxvY2FsIiFjaHVyY2hlcyBCZWxsZXZpbGxlLCBSaG9kZSBJc2xhbmQyBRAhGKABSLQIUOgFWOgFcAB4AJABAJgBmQGgAaQCqgEDMC4yuAEDyAEA-AEC-AEBmAICoAK4AsICBBAjGCfCAggQABiABBiiBMICCBAAGKIEGIkFmAMAiAYBkgcDMC4yoAfKBg&sclient=gws-wiz-local#rlfi=hd:;si:3027795955536530662,l,CiFjaHVyY2hlcyBCZWxsZXZpbGxlLCBSaG9kZSBJc2xhbmRIrN7exeWAgIAIWjIQABABGAAYAhgDIiBjaHVyY2hlcyBiZWxsZXZpbGxlIHJob2RlIGlzbGFuZCoECAMQAJIBGW5vbl9kZW5vbWluYXRpb25hbF9jaHVyY2iaASRDaGREU1VoTk1HOW5TMFZKUTBGblNVUmhhMHR5UTNSblJSQUKqAVYQASoMIghjaHVyY2hlcygAMh4QASIamWH8MpICmPXrJLRvgL7909MBFSuCk5zp1HMyJBACIiBjaHVyY2hlcyBiZWxsZXZpbGxlIHJob2RlIGlzbGFuZPoBBAhDECo;mv:[[42.0188431,-71.2714495],[41.360321,-71.6709212]]";

    private final PageContentExtractor extractor = new PageContentExtractor();
    private ChromeDriver chrome;
    private Queue<WebElement> placeCards = new LinkedList<>();
    private WebElement placeCard;
    private String query = "";

    public void load(String query) {
        this.query = query;

        final String url = URL_PREFIX + this.query + URL_LOCATION_SUFIX + URL_SUFIX;
        loadPage(url);
    }

    private void loadPage(final String url) {
        chrome.navigate().to(url);

        waitPageFullLoading(chrome);

        this.placeCards = new LinkedList<>(this.chrome.findElementsByCssSelector("div[jsaction=\"rcuQ6b:npT2md;e3EWke:kN9HDb\"]"));
    }

    public String fetchWebPage() {
        String webPage = "";
        try {
            webPage = extractor.gainDynamic(chrome, Boolean.FALSE);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        return webPage;
    }

    private void waitPageFullLoading(ChromeDriver chrome) {
        WebDriverWait wait = new WebDriverWait(chrome, 10);

        try {
            WebElement ignoreElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("div.rllt__details")
                    )
            );
        } catch (TimeoutException ex) {
            System.err.println("Loading web page timeout exception. " + ex.getMessage());
        }

        try {
            Thread.sleep(1_500);
        } catch (InterruptedException ex) {
            LOGGER.log(Level.SEVERE, "Page loading was interrupted.", ex);
        }
    }

    public void createDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        //options.addArguments("--proxy-server=socks5://3.85.180.106:1080");

        // By pass bot detection.
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-blink-features");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--allow-insecure-localhost");

        this.chrome = new ChromeDriver(options);
    }

    public void quit() {
        if (this.chrome == null) {
            return;
        }

        this.chrome.close();
    }

    boolean loadNextPlaceProfile() {
        if (this.placeCards.isEmpty()) {
            return false;
        }

        this.placeCard = this.placeCards.poll();
        placeCard.findElement(By.cssSelector("div.rlfl__tls div.rllt__details")).click();
        sleep(2_000);

        return true;
    }

    public WebElement getPlaceCard() {
        return placeCard;
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(ChromeDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    boolean hasNextPage() {
        return !this.chrome.findElementsByCssSelector("div[jscontroller=\"vhJCnf\"] a#pnnext").isEmpty();
    }

    void loadNextPage() {
        //final String url = URL_PREFIX + this.query + URL_LOCATION_SUFIX + URL_SUFIX + ";start:20";
        //loadPage(url);
        this.chrome.findElementsByCssSelector("div[jscontroller=\"vhJCnf\"] a#pnnext").get(0).click();
        sleep(5_000);
        this.placeCards = new LinkedList<>(this.chrome.findElementsByCssSelector("div[jsaction=\"rcuQ6b:npT2md;e3EWke:kN9HDb\"]"));
    }
}
