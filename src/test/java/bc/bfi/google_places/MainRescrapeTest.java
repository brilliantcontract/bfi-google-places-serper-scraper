package bc.bfi.google_places;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Comparator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import org.junit.Ignore;

public class MainRescrapeTest {

    @Before
    public void setUp() throws IOException {
        System.setProperty("java.awt.headless", "true");
        // Prepare json directory with a sample file
        Path jsonDir = Paths.get("json");
        if (!Files.exists(jsonDir)) {
            Files.createDirectory(jsonDir);
        }
        String json = "{\n" +
                "  \"searchParameters\": {\"q\": \"query\"},\n" +
                "  \"places\": [{\n" +
                "    \"title\": \"Name1\",\n" +
                "    \"address\": \"Address1\",\n" +
                "    \"latitude\": 10.0,\n" +
                "    \"longitude\": 20.0,\n" +
                "    \"rating\": 4.5,\n" +
                "    \"ratingCount\": 10,\n" +
                "    \"category\": \"type1\",\n" +
                "    \"website\": \"https://ex.com\",\n" +
                "    \"cid\": \"cid1\",\n" +
                "    \"phoneNumber\": \"+123456\"\n" +
                "  }]\n" +
                "}";
        Files.write(jsonDir.resolve("data.json"), json.getBytes(StandardCharsets.UTF_8));
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get("initial-.csv"));
        Files.deleteIfExists(Paths.get("report-.csv"));
        Path jsonDir = Paths.get("json");
        if (Files.exists(jsonDir)) {
            Files.walk(jsonDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(java.io.File::delete);
        }
    }

    @Ignore
    @Test
    public void generatesReportAndLogsCompletion() throws Exception {
        Main main = new Main();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        // invoke the private action method
        java.lang.reflect.Method method = Main.class.getDeclaredMethod("jButtonRescrapeActionPerformed", java.awt.event.ActionEvent.class);
        method.setAccessible(true);
        method.invoke(main, new Object[]{null});

        System.setOut(originalOut);

        Path reportPath = Paths.get("report-.csv");
        assertThat(Files.exists(reportPath), is(true));
        List<String> lines = Files.readAllLines(reportPath, StandardCharsets.UTF_8);
        assertThat(lines.get(0), is("Name,Missing values,Missing percentage"));
        assertThat(out.toString(), containsString("Re-processing completed"));
    }
}
