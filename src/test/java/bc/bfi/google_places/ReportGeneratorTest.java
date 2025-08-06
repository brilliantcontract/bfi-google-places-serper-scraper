package bc.bfi.google_places;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

public class ReportGeneratorTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void generatesReportWithStatistics() throws IOException {
        Path data = tempFolder.newFile("initial-.csv").toPath();
        List<String> lines = Arrays.asList(
                "GOOGLE_PLACE_CODE,NAME,FULL_ADDRESS,LATITUDE,LONGITUDE,PHONE,WEBSITE,QUERY,RATE,REVIEWS,TYPE",
                "1,Name1,Address1,40.0,-70.0,+1234567890,https://ex.com,q,4.5,100,restaurant",
                "2,,Address2,41.0,-71.0,+123-456-7890,,q,not_a_rate,101,bar",
                "3,Name3,,lat_invalid,long_invalid,abc,https://ex.com,q,3.0,invalid_reviews,"
        );
        Files.write(data, lines, StandardCharsets.UTF_8);

        Path report = Paths.get(tempFolder.getRoot().toPath().toString(), "report.csv");
        new ReportGenerator().generate(data, report);

        List<String> reportLines = Files.readAllLines(report, StandardCharsets.UTF_8);
        assertThat(reportLines.get(0), is("Name,Missing values,Missing percentage"));
        assertThat(reportLines, hasItems(
                "Total number of records in table,3,",
                "Number of records without TYPE field,1,33.33%",
                "Number of records without NAME field,1,33.33%",
                "Number of records without FULL_ADDRESS field,1,33.33%",
                "Number of records with broken data format of LATITUDE field,1,33.33%",
                "Number of records with broken data format of PHONE field,1,33.33%"
        ));
    }
}
