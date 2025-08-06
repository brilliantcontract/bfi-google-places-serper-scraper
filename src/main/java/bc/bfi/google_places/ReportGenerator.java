package bc.bfi.google_places;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Locale;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

/**
 * Generates a report with statistics about scraped places.
 */
public class ReportGenerator {

    /**
     * Generates report CSV file based on provided CSV data file.
     *
     * @param dataPath   path to CSV file with scraped data
     * @param reportPath path to output report.csv
     */
    public void generate(Path dataPath, Path reportPath) {
        int total = 0;
        int noType = 0;
        int noName = 0;
        int noFullAddress = 0;
        int noLatitude = 0;
        int noLongitude = 0;
        int noRate = 0;
        int noReviews = 0;
        int noPhone = 0;
        int noWebsite = 0;
        int brokenLatitude = 0;
        int brokenLongitude = 0;
        int brokenRate = 0;
        int brokenReviews = 0;
        int brokenPhone = 0;

        try (Reader reader = Files.newBufferedReader(dataPath, StandardCharsets.UTF_8);
                CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            for (CSVRecord record : parser) {
                total++;
                String type = record.get("TYPE").trim();
                if (type.isEmpty()) {
                    noType++;
                }
                String name = record.get("NAME").trim();
                if (name.isEmpty()) {
                    noName++;
                }
                String fullAddress = record.get("FULL_ADDRESS").trim();
                if (fullAddress.isEmpty()) {
                    noFullAddress++;
                }
                String latitude = record.get("LATITUDE").trim();
                if (latitude.isEmpty()) {
                    noLatitude++;
                } else if (!isDouble(latitude)) {
                    brokenLatitude++;
                }
                String longitude = record.get("LONGITUDE").trim();
                if (longitude.isEmpty()) {
                    noLongitude++;
                } else if (!isDouble(longitude)) {
                    brokenLongitude++;
                }
                String rate = record.get("RATE").trim();
                if (rate.isEmpty()) {
                    noRate++;
                } else if (!isDouble(rate)) {
                    brokenRate++;
                }
                String reviews = record.get("REVIEWS").trim();
                if (reviews.isEmpty()) {
                    noReviews++;
                } else if (!isInteger(reviews)) {
                    brokenReviews++;
                }
                String phone = record.get("PHONE").trim();
                if (phone.isEmpty()) {
                    noPhone++;
                } else if (!isPhone(phone)) {
                    brokenPhone++;
                }
                String website = record.get("WEBSITE").trim();
                if (website.isEmpty()) {
                    noWebsite++;
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Cannot generate report: " + ex.getMessage(), ex);
        }

        DecimalFormat pctFormat = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.US);
        pctFormat.applyPattern("0.00%");

        try (Writer writer = Files.newBufferedWriter(reportPath, StandardCharsets.UTF_8);
                CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            printer.printRecord("Name", "Filled with values", "Missing percentage");
            printer.printRecord("Total number of records in table", total, "");
            printer.printRecord("Number of records without TYPE field", noType, formatPct(noType, total, pctFormat));
            printer.printRecord("Number of records without NAME field", noName, formatPct(noName, total, pctFormat));
            printer.printRecord("Number of records without FULL_ADDRESS field", noFullAddress, formatPct(noFullAddress, total, pctFormat));
            printer.printRecord("Number of records without LATITUDE field", noLatitude, formatPct(noLatitude, total, pctFormat));
            printer.printRecord("Number of records without LONGITUDE field", noLongitude, formatPct(noLongitude, total, pctFormat));
            printer.printRecord("Number of records without RATE field", noRate, formatPct(noRate, total, pctFormat));
            printer.printRecord("Number of records without REVIEWS field", noReviews, formatPct(noReviews, total, pctFormat));
            printer.printRecord("Number of records without PHONE field", noPhone, formatPct(noPhone, total, pctFormat));
            printer.printRecord("Number of records without WEBSITE field", noWebsite, formatPct(noWebsite, total, pctFormat));
            printer.printRecord("Number of records with broken data format of LATITUDE field", brokenLatitude, formatPct(brokenLatitude, total, pctFormat));
            printer.printRecord("Number of records with broken data format of LONGITUDE field", brokenLongitude, formatPct(brokenLongitude, total, pctFormat));
            printer.printRecord("Number of records with broken data format of RATE field", brokenRate, formatPct(brokenRate, total, pctFormat));
            printer.printRecord("Number of records with broken data format of REVIEWS field", brokenReviews, formatPct(brokenReviews, total, pctFormat));
            printer.printRecord("Number of records with broken data format of PHONE field", brokenPhone, formatPct(brokenPhone, total, pctFormat));
        } catch (IOException ex) {
            throw new RuntimeException("Cannot write report: " + ex.getMessage(), ex);
        }
    }

    private boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private boolean isPhone(String value) {
        return value.matches("^\\+?[\\d\\s().-]+$") && value.replaceAll("[^\\d]", "").length() > 0;
    }

    private String formatPct(int count, int total, DecimalFormat formatter) {
        double pct = total == 0 ? 0d : (double) count / (double) total;
        return formatter.format(pct);
    }
}
