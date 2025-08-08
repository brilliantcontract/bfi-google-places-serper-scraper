package bc.bfi.google_places;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Paths;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CsvStorageTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void trimsWhitespaceBeforeWriting() throws Exception {
        Path csvPath = Paths.get("initial-.csv");

        Field field = CsvStorage.class.getDeclaredField("STORAGE_FILE");
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        String original = (String) field.get(null);
        field.set(null, csvPath.toString());

        try {
            CsvStorage storage = new CsvStorage();
            Place place = new Place();
            place.setCid(" 123 \n");
            place.setName(" Name ");
            place.setFullAddress("\nAddress \r\n");
            place.setLatitude(" 10.1 ");
            place.setLongitude(" 20.2 \n");
            place.setPhone(" +123456 \n");
            place.setWebsite(" https://ex.com ");
            place.setQuery(" query ");
            place.setRate(" 4.5 ");
            place.setRateCounter(" 10 ");
            place.setType(" type \n");
            place.setDuration(" 1000 ");

            storage.append(place);

            try (Reader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8);
                    CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
                CSVRecord record = parser.iterator().next();
                assertThat(record.get("GOOGLE_PLACE_CODE"), is("123"));
                assertThat(record.get("NAME"), is("Name"));
                assertThat(record.get("FULL_ADDRESS"), is("Address"));
                assertThat(record.get("LATITUDE"), is("10.1"));
                assertThat(record.get("LONGITUDE"), is("20.2"));
                assertThat(record.get("PHONE"), is("+123456"));
                assertThat(record.get("WEBSITE"), is("https://ex.com"));
                assertThat(record.get("QUERY"), is("query"));
                assertThat(record.get("RATE"), is("4.5"));
                assertThat(record.get("REVIEWS"), is("10"));
                assertThat(record.get("TYPE"), is("type"));
                assertThat(record.get("DURATION"), is("1000"));
            }
        } finally {
            field.set(null, original);
            Files.delete(csvPath);
        }
    }
}
