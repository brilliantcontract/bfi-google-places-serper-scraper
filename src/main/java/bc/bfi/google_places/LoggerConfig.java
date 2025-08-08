package bc.bfi.google_places;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerConfig {

    private static final int LIMIT = 10 * 1024 * 1024; // 10 MB

    public static void configure() {
        configure("application.log", LIMIT);
    }

    public static void configure(String fileName, int limit) {
        ShortClassNameFormatter formatter = new ShortClassNameFormatter();
        try {
            Logger rootLogger = Logger.getLogger("");
            for (Handler handler : rootLogger.getHandlers()) {
                handler.setFormatter(formatter);
            }
            FileHandler fileHandler = new FileHandler(fileName, limit, 1, true);
            fileHandler.setFormatter(formatter);
            rootLogger.addHandler(fileHandler);
        } catch (IOException ex) {
            Logger.getLogger(LoggerConfig.class.getName()).log(Level.SEVERE, "Failed to setup logger", ex);
        }
    }
}
