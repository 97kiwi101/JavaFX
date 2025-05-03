// Kiwi.java  (renamed to follow conventions)
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class kiwi {
    /** @return today’s date as “yyyy-MM-dd” */
    public static String getDateTime() {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
