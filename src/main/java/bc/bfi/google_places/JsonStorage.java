package bc.bfi.google_places;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class JsonStorage {

    private static final Path JSON_DIRECTORY = Paths.get("json");
    private static final Logger LOGGER = Logger.getLogger(JsonStorage.class.getName());

    public JsonStorage() {
        // Check if JSON directory is exist.
        if (!Files.isDirectory(JSON_DIRECTORY)) {
            try {
                Files.createDirectories(JSON_DIRECTORY);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Cannot create the JSON directory.", ex);
                JOptionPane.showMessageDialog(null, "Cannot create the JSON directory.", "Error", JOptionPane.ERROR_MESSAGE);
                throw new IllegalStateException("Cannot create the JSON directory.", ex);
            }
        }
    }

    public void save(String json) {
        // Generate timestamp and random number for filename
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        int randomNum1 = new Random().nextInt(10000);
        int randomNum2 = new Random().nextInt(10000);
        int randomNum3 = new Random().nextInt(10000);
        String fileName = timestamp + "_" + randomNum1 + "_" + randomNum2 + "_" + randomNum3 + ".json";
        Path filePath = JSON_DIRECTORY.resolve(fileName);

        // Save JSON string to file
        try {
            Files.write(filePath, json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE_NEW);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Cannot save .json file.", ex);
            JOptionPane.showMessageDialog(null, "Cannot save .json file.", "Error", JOptionPane.ERROR_MESSAGE);
            throw new IllegalStateException("Cannot save .json file.", ex);
        }
    }

}
