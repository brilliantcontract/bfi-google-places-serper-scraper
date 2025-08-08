package bc.bfi.google_places;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Formatter that prints only the simple class name in log messages.
 */
public class ShortClassNameFormatter extends Formatter {

    private static final String FORMAT = "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS %1$Tp %2$s %4$s: %5$s%6$s%n";

    @Override
    public synchronized String format(LogRecord record) {
        String source;
        if (record.getSourceClassName() != null) {
            String className = record.getSourceClassName();
            int lastDot = className.lastIndexOf('.');
            if (lastDot >= 0) {
                className = className.substring(lastDot + 1);
            }
            source = className;
            if (record.getSourceMethodName() != null) {
                source += " " + record.getSourceMethodName();
            }
        } else {
            source = record.getLoggerName();
        }

        String throwable = "";
        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            record.getThrown().printStackTrace(pw);
            pw.close();
            throwable = sw.toString();
        }

        return String.format(
                FORMAT,
                new Date(record.getMillis()),
                source,
                record.getLoggerName(),
                record.getLevel().getLocalizedName(),
                formatMessage(record),
                throwable
        );
    }
}

