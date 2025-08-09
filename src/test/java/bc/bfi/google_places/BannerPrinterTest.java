package bc.bfi.google_places;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BannerPrinterTest {

    @Test
    public void printBannerOutputsExpectedMessage() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));
        try {
            BannerPrinter.printBanner();
        } finally {
            System.setOut(original);
        }
        String expected = BannerPrinter.BANNER + System.lineSeparator();
        assertThat(out.toString(), is(expected));
    }
}

