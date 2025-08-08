package bc.bfi.google_places;

public class Place {

    private String fullAddress = "";
    private String latitude = "";
    private String longitude = "";
    private String name = "";
    private String permanentlyClosed = "";
    private String phone = "";
    private String query = "";
    private String rate = "";
    private String rateCounter = "";
    private String type = "";
    private String socialLinks = "";
    private String cid = "";
    private String website = "";
    private String duration = "";

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermanentlyClosed() {
        return permanentlyClosed;
    }

    public void setPermanentlyClosed(String permanentlyClosed) {
        this.permanentlyClosed = permanentlyClosed;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRateCounter() {
        return rateCounter;
    }

    public void setRateCounter(String rateCounter) {
        this.rateCounter = rateCounter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSocialLinks() {
        return socialLinks;
    }

    public void setSocialLinks(String socialLinks) {
        this.socialLinks = socialLinks;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void print() {
        System.out.println("Full address: " + fullAddress);
        System.out.println("Latitude: " + latitude);
        System.out.println("Longitude: " + longitude);
        System.out.println("Name: " + name);
        System.out.println("Permanently closed: " + permanentlyClosed);
        System.out.println("Phone: " + phone);
        System.out.println("Query: " + query);
        System.out.println("Rate: " + rate);
        System.out.println("Rate counter: " + rateCounter);
        System.out.println("Type: " + type);
        System.out.println("Social links: " + socialLinks);
        System.out.println("CID: " + cid);
        System.out.println("Website: " + website);
        System.out.println("Duration: " + duration);
    }

}
