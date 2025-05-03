// DateBasedWeeklyToDoList.java
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class DateBasedWeeklyToDoList implements Serializable {
    private static final long serialVersionUID = 1L;

    // Maps each of the next 7 dates → list of tasks due that day
    private LinkedHashMap<LocalDate, List<Task>> weekMap = new LinkedHashMap<>();

    public DateBasedWeeklyToDoList(List<Task> allTasks, LocalDate startDate) {
        // build keys for next 7 days
        for (int i = 0; i < 7; i++) {
            weekMap.put(startDate.plusDays(i), new ArrayList<>());
        }
        // bucket-up
        LocalDate endExclusive = startDate.plusDays(7);
        for (Task t : allTasks) {
            LocalDate d = t.getDate();
            if (!d.isBefore(startDate) && d.isBefore(endExclusive)) {
                weekMap.get(d).add(t);
            }
        }
    }

    public Map<LocalDate,List<Task>> getWeekMap() {
        return Collections.unmodifiableMap(weekMap);
    }

    /** @return tasks whose dueDate < today */
    public static List<Task> getOverdue(List<Task> allTasks, LocalDate today) {
        List<Task> overdue = new ArrayList<>();
        for (Task t : allTasks) {
            if (t.getDate().isBefore(today)) {
                overdue.add(t);
            }
        }
        return overdue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<LocalDate, List<Task>> e : weekMap.entrySet()) {
            sb.append(e.getKey()).append(":\n");
            if (e.getValue().isEmpty()) {
                sb.append("  (no tasks)\n");
            } else {
                for (Task t : e.getValue()) {
                    sb.append("  • ").append(t).append("\n");
                }
            }
        }
        return sb.toString();
    }

    /** flatten all tasks in this 7-day span */
    public List<Task> allWeekTasks() {
        List<Task> flat = new ArrayList<>();
        for (List<Task> list : weekMap.values()) {
            flat.addAll(list);
        }
        return flat;
    }
}
