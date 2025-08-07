package bc.bfi.google_places;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfig {

    private static final int LIMIT = 10 * 1024 * 1024; // 10 MB

    public static void configure() {
        configure("application.log", LIMIT);
    }

    public static void configure(String fileName, int limit) {
        try {
            Logger rootLogger = Logger.getLogger("");
            FileHandler fileHandler = new FileHandler(fileName, limit, 1, true);
            fileHandler.setFormatter(new SimpleFormatter());
            rootLogger.addHandler(fileHandler);
        } catch (IOException ex) {
            Logger.getLogger(LoggerConfig.class.getName()).log(Level.SEVERE, "Failed to setup logger", ex);
        }
    }
}
