package bc.bfi.google_places.scrapers;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utf8Test {

    @Test
    public void test() {
        String fileName = "utf8_test.txt"; // Windows-safe filename
        Path filePath = Paths.get(fileName);
        String content = "Hello, 世界! Привет, мир! こんにちは世界!"; // UTF-8 characters

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write(content);
            System.out.println("File written successfully with UTF-8 encoding.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
