import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Driver class for the ToDoList application.
 * 
 * Responsible for loading or seeding tasks, serializing/deserializing the task list,
 * and displaying today's, this week's, next week's, and overdue tasks.
 */
public class Driver {
    /**
     * vars
     */
    private static final String FILENAME = "Object.dat";

    /**
     * Application entry point.
     * 
     *   Load existing tasks from file, or seed and save sample tasks.
     *   Determine today's date.
     *   Display tasks due this week.
     *   Display tasks due next week.
     *   Display any overdue tasks.
     * 
     * @throws Exception if file IO or parsing fails
     */
    public static void main(String[] args) throws Exception {
        // 1 Build or load the task list from persistent storage
        File f = new File(FILENAME);
        List<Task> tasks;
        if (f.exists()) {
            // Deserialize previously saved tasks
            tasks = load();
            System.out.println("✔ Deserialization complete.");
        } else {
            // Seed sample tasks and save them to file
            tasks = seedSampleTasks();
            upload(tasks);
            System.out.println("✔ Serialization complete.");
        }

        // 2 Determine today's date using a helper (kiwi) and parse to LocalDate
        String todayStr = kiwi.getDateTime(); // e.g., "2025-05-06"
        LocalDate today = LocalDate.parse(todayStr);
        System.out.println("Today is: " + today + "\n");

        // 3 Display tasks due in the current week
        DateBasedWeeklyToDoList thisWeek =
            new DateBasedWeeklyToDoList(tasks, today);
        System.out.println("=== Tasks Due This Week ===");
        System.out.println(thisWeek);

        // 4 Display tasks due in the next week
        DateBasedWeeklyToDoList nextWeek =
            new DateBasedWeeklyToDoList(tasks, today.plusWeeks(1));
        System.out.println("=== Tasks Due Next Week ===");
        System.out.println(nextWeek);

        // 5 Identify and display overdue tasks
        List<Task> overdue =
            DateBasedWeeklyToDoList.getOverdue(tasks, today);
        System.out.println("=== Overdue Tasks ===");
        if (overdue.isEmpty()) {
            System.out.println("(none)");
        } else {
            for (Task t : overdue) {
                System.out.println("• " + t);
            }
        }
    }

    /**
     * Creates a small list of sample tasks for initial use.
     *
     * @return a List of Task objects with predefined titles, descriptions, and dates
     */
    private static List<Task> seedSampleTasks() {
        return Arrays.asList(
            new Task("Finish report", "Wrap up Q2 financials", "2025-05-01"),
            new Task("Grocery shopping", "Buy ingredients for dinner", "2025-05-08"),
            new Task("Email Prof", "Ask about midterm",      "2025-04-28")
        );
    }

    /**
     * Serializes and writes the given task list to disk.
     *
     * @param tasks the list of Task objects to serialize
     * @throws IOException if an I/O error occurs during writing
     */
    private static void upload(List<Task> tasks) throws IOException {
        try (ObjectOutputStream oos =
                 new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(tasks);
        }
    }

    /**
     * Reads and deserializes the task list from disk.
     *
     * @return the deserialized List of Task objects
     * @throws IOException if an I/O error occurs during reading
     * @throws ClassNotFoundException if the Task class cannot be resolved
     */
    @SuppressWarnings("unchecked")
    private static List<Task> load() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois =
                 new ObjectInputStream(new FileInputStream(FILENAME))) {
            return (List<Task>) ois.readObject();
        }
    }
}
