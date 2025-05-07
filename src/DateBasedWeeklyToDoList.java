import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * DateBasedWeeklyToDoList organizes tasks into a 7-day window,
 * starting from a given start date. It supports querying tasks by day,
 * retrieving all tasks in the week, and identifying overdue tasks.
 */
public class DateBasedWeeklyToDoList implements Serializable {
    /**
     * Varables
     */
    private static final long serialVersionUID = 1L;
    private LinkedHashMap<LocalDate, List<Task>> weekMap = new LinkedHashMap<>(); //https://www.geeksforgeeks.org/linkedhashmap-class-in-java/

    /**
     * Constructs a weekly view of tasks.
     * 
     * Initializes the map with keys for each of the next seven days,
     * then assigns each task from allTasks to its corresponding day list
     * if it falls within the window [startDate, startDate + 7 days).
     *
     * @param allTasks  the complete list of tasks to filter
     * @param startDate the first date of the 7-day window
     */
    public DateBasedWeeklyToDoList(List<Task> allTasks, LocalDate startDate) {
        // Initialize entries for each day in the 7-day span
        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            weekMap.put(date, new ArrayList<>());
        }

        // Determine the exclusive end date of the window
        LocalDate endExclusive = startDate.plusDays(7);

        // Distribute tasks into the appropriate day bucket
        for (Task t : allTasks) {
            LocalDate d = t.getDate();
            // Include only if within [startDate, endExclusive)
            if (!d.isBefore(startDate) && d.isBefore(endExclusive)) {
                weekMap.get(d).add(t);
            }
        }
    }

    /**
     * Provides an unmodifiable view of the week-to-tasks mapping.
     *
     * @return a map from each LocalDate in the 7-day window to its tasks
     */
    public Map<LocalDate, List<Task>> getWeekMap() {
        return Collections.unmodifiableMap(weekMap);
    }

    /**
     * Returns tasks that are overdue relative to the provided 'today' date.
     *
     * @param allTasks the complete list of tasks to check
     * @param today    the reference date for determining overdue status
     * @return a list of Task objects whose due date is before today
     */
    public static List<Task> getOverdue(List<Task> allTasks, LocalDate today) {
        List<Task> overdue = new ArrayList<>();
        for (Task t : allTasks) {
            if (t.getDate().isBefore(today)) {
                overdue.add(t);
            }
        }
        return overdue;
    }

    /**
     * Returns a formatted string representing tasks for each day in the week.
     *<p>
     * Days with no tasks are indicated.
     *
     * @return multi-line string with date headings and task bullet points
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<LocalDate, List<Task>> e : weekMap.entrySet()) {
            sb.append(e.getKey()).append(":\n");
            List<Task> tasks = e.getValue();
            if (tasks.isEmpty()) {
                sb.append("  (no tasks)\n");
            } else {
                for (Task t : tasks) {
                    sb.append("  • ").append(t).append("\n");
                }
            }
        }
        return sb.toString();
    }

    /**
     * Flattens all task lists in the 7-day span into a single list.
     *
     * @return a List containing every Task scheduled in the week window
     */
    public List<Task> allWeekTasks() {
        List<Task> flat = new ArrayList<>();
        for (List<Task> list : weekMap.values()) {
            flat.addAll(list);
        }
        return flat;
    }
}