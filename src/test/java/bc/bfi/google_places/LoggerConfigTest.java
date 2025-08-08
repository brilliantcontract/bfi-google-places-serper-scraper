package bc.bfi.google_places;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;

public class LoggerConfigTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void logFileDoesNotExceedLimit() throws IOException {
        Path logPath = tempFolder.getRoot().toPath().resolve("test.log");
        LoggerConfig.configure(logPath.toString(), 1024);

        Logger logger = Logger.getLogger(LoggerConfigTest.class.getName());
        for (int i = 0; i < 1000; i++) {
            logger.info("Test message " + i);
        }

        for (Handler handler : Logger.getLogger("").getHandlers()) {
            if (handler instanceof FileHandler) {
                handler.close();
                Logger.getLogger("").removeHandler(handler);
            }
        }

        assertThat(Files.size(logPath), lessThanOrEqualTo(1024L));
    }

    @Test
    public void logsAreWrittenOnSingleLine() throws IOException {
        Path logPath = tempFolder.getRoot().toPath().resolve("single.log");
        LoggerConfig.configure(logPath.toString(), 1024);

        Logger logger = Logger.getLogger(LoggerConfigTest.class.getName());
        logger.info("Test message");

        List<String> lines = Files.readAllLines(logPath);
        assertThat(lines, hasSize(1));
        assertThat(lines.get(0), containsString("INFO: Test message"));
    }

    @Test
    public void logLineContainsSimpleClassName() throws IOException {
        Path logPath = tempFolder.getRoot().toPath().resolve("classname.log");
        LoggerConfig.configure(logPath.toString(), 1024);

        Logger logger = Logger.getLogger(LoggerConfigTest.class.getName());
        logger.info("Simple message");

        List<String> lines = Files.readAllLines(logPath);
        assertThat(lines, hasSize(1));
        assertThat(lines.get(0), containsString("LoggerConfigTest logLineContainsSimpleClassName INFO: Simple message"));
        assertThat(lines.get(0), not(containsString("bc.bfi.google_places.LoggerConfigTest")));
    }
}
