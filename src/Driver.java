// Driver.java
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Driver {
    private static final String FILENAME = "Object.dat";

    public static void main(String[] args) throws Exception {
        // 1) build or load
        File f = new File(FILENAME);
        List<Task> tasks;
        if (f.exists()) {
            tasks = load();
            System.out.println("✔ Deserialization complete.");
        } else {
            tasks = seedSampleTasks();
            upload(tasks);
            System.out.println("✔ Serialization complete.");
        }

        // 2) figure out today
        String todayStr = kiwi.getDateTime();
        LocalDate today = LocalDate.parse(todayStr);
        System.out.println("Today is: " + today + "\n");

        // 3) this week’s tasks
        DateBasedWeeklyToDoList thisWeek = 
            new DateBasedWeeklyToDoList(tasks, today);
        System.out.println("=== Tasks Due This Week ===");
        System.out.println(thisWeek);

        // 4) next week’s tasks
        DateBasedWeeklyToDoList nextWeek = 
            new DateBasedWeeklyToDoList(tasks, today.plusWeeks(1));
        System.out.println("=== Tasks Due Next Week ===");
        System.out.println(nextWeek);

        // 5) overdue tasks
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

    private static List<Task> seedSampleTasks() {
        return Arrays.asList(
            new Task("Finish report", "Wrap up Q2 financials", "2025-05-01"),
            new Task("Grocery shopping", "Buy ingredients for dinner", "2025-05-08"),
            new Task("Email Prof", "Ask about midterm",      "2025-04-28")
        );
    }

    private static void upload(List<Task> tasks) throws IOException {
        try (ObjectOutputStream oos = 
                 new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(tasks);
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Task> load() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = 
                 new ObjectInputStream(new FileInputStream(FILENAME))) {
            return (List<Task>) ois.readObject();
        }
    }
}
