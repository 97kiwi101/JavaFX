import java.time.LocalDate; //https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html
import java.time.format.DateTimeFormatter; //https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html

/**
 * Utility class for retrieving the current date in ISO format.
 * 
 * Provides a simple method to get today's date as a formatted string.
 */
public class kiwi {
    /**
     * Returns today's date formatted as "yyyy-MM-dd" using the system clock.
     *
     * @return a string representing today's date in ISO_LOCAL_DATE format
     */
    public static String getDateTime() {
        // Obtain the current date from the system clock
        LocalDate today = LocalDate.now();
        // Format the date in ISO_LOCAL_DATE (e.g., "2025-05-06")
        return today.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}