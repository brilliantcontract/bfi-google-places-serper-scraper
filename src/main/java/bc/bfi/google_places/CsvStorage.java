package bc.bfi.google_places;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CsvStorage {

    private static final String STORAGE_FILE = "initial-.csv";
    private static final Logger LOGGER = Logger.getLogger(CsvStorage.class.getName());

    private static final String[] HEADERS = {
        "GOOGLE_PLACE_CODE",
        "NAME",
        "FULL_ADDRESS",
        "LATITUDE",
        "LONGITUDE",
        "PHONE",
        "WEBSITE",
        "QUERY",
        "RATE",
        "REVIEWS",
        "TYPE",
        "DURATION"
    };

    public CsvStorage() {
        if (!Files.exists(Paths.get("STORAGE_FILE"))) {
            try {
                createCsvFile();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Failed to write to CSV file: " + STORAGE_FILE, ex);
                JOptionPane.showMessageDialog(null, "Cannot write to .csv file.", "Error", JOptionPane.ERROR_MESSAGE);
                throw new IllegalStateException("Cannot write to .csv file.", ex);
            }
        }
    }

    private void createCsvFile() {
        Path path = Paths.get(STORAGE_FILE);
        if (Files.exists(path)) {
            String message = "CSV file already exists: " + path;
            LOGGER.severe(message);
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            throw new IllegalStateException(message);
        }

        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            CSVFormat format = CSVFormat.DEFAULT.withIgnoreHeaderCase();
            try (CSVPrinter printer = new CSVPrinter(writer, format)) {
                printer.printRecord((Object[]) HEADERS);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to create CSV file: " + path, ex);
            JOptionPane.showMessageDialog(null, "Cannot create .csv file", "Error", JOptionPane.ERROR_MESSAGE);
            throw new IllegalStateException("Cannot create .csv file", ex);
        }
    }

    public void append(Place place) {
        List<String> record = new ArrayList<>();
        record.add(trim(place.getCid()));
        record.add(trim(place.getName()));
        record.add(trim(place.getFullAddress()));
        record.add(trim(place.getLatitude()));
        record.add(trim(place.getLongitude()));
        record.add(trim(place.getPhone()));
        record.add(trim(place.getWebsite()));
        record.add(trim(place.getQuery()));
        record.add(trim(place.getRate()));
        record.add(trim(place.getRateCounter()));
        record.add(trim(place.getType()));
        record.add(trim(place.getDuration()));

        Path path = Paths.get(STORAGE_FILE);
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            CSVFormat format = CSVFormat.DEFAULT;
            try (CSVPrinter printer = new CSVPrinter(writer, format)) {
                printer.printRecord(record);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to write to CSV file: " + path, ex);
            JOptionPane.showMessageDialog(null, "Cannot write to .csv file.", "Error", JOptionPane.ERROR_MESSAGE);
            throw new IllegalStateException("Cannot write to .csv file.", ex);
        }
    }

    public void append(final List<Place> places) {
        for (Place place : places) {
            append(place);
        }
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
