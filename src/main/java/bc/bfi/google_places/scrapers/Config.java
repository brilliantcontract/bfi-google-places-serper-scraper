package bc.bfi.google_places.scrapers;

public class Config {

    private String country;
    private String language;
    private String location;

    public void load(String country, String language, String location) {
        this.country = country;
        this.language = language;
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public String getLanguage() {
        return language;
    }

    public String getLocation() {
        return location;
    }

}
