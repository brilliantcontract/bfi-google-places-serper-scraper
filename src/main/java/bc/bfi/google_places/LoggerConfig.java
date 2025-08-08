package bc.bfi.google_places;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfig {

    private static final int LIMIT = 10 * 1024 * 1024; // 10 MB
    private static final String FORMAT = "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS %1$Tp %2$s %4$s: %5$s%6$s%n";

    public static void configure() {
        configure("application.log", LIMIT);
    }

    public static void configure(String fileName, int limit) {
        System.setProperty("java.util.logging.SimpleFormatter.format", FORMAT);
        try {
            Logger rootLogger = Logger.getLogger("");
            for (Handler handler : rootLogger.getHandlers()) {
                handler.setFormatter(new SimpleFormatter());
            }
            FileHandler fileHandler = new FileHandler(fileName, limit, 1, true);
            fileHandler.setFormatter(new SimpleFormatter());
            rootLogger.addHandler(fileHandler);
        } catch (IOException ex) {
            Logger.getLogger(LoggerConfig.class.getName()).log(Level.SEVERE, "Failed to setup logger", ex);
        }
    }
}
