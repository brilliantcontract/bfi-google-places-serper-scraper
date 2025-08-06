package bc.bfi.google_places;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        "TYPE"
    };

    public CsvStorage() {
        if (!Files.exists(Paths.get("STORAGE_FILE"))) {
            try {
                createCsvFile();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Cannot write to .csv file. " + ex.getMessage(), ex);
                System.exit(1);
            }
        }
    }

    private void createCsvFile() {
        Path path = Paths.get(STORAGE_FILE);
        if (Files.exists(path)) {
            System.err.println(".csv file already exist: " + path);
            System.exit(1);
        }

        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            CSVFormat format = CSVFormat.DEFAULT.withIgnoreHeaderCase();
            try (CSVPrinter printer = new CSVPrinter(writer, format)) {
                printer.printRecord((Object[]) HEADERS);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Cannot create .csv file", ex);
            System.exit(1);
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

        Path path = Paths.get(STORAGE_FILE);
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            CSVFormat format = CSVFormat.DEFAULT;
            try (CSVPrinter printer = new CSVPrinter(writer, format)) {
                printer.printRecord(record);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Cannot write to .csv file. " + ex.getMessage(), ex);
            System.exit(1);
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
