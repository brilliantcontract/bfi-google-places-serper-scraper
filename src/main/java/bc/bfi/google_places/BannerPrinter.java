package bc.bfi.google_places;

/**
 * Utility class that prints a banner to the console when the scraping
 * process has finished.
 */
public final class BannerPrinter {

    private BannerPrinter() {
        // Utility class
    }

    /**
     * The banner that indicates successful completion of the scraping process.
     */
    public static final String BANNER = String.join(System.lineSeparator(),
            "-------------------------------------- =========== --------------------------------------",
            "",
            "             SCRAPING PROCESS HAS BEEN COMPLETED SUCCESSFULLY",
            "",
            "-------------------------------------- =========== --------------------------------------");

    /**
     * Print the banner to the standard output.
     */
    public static void printBanner() {
        System.out.println(BANNER);
    }
}

