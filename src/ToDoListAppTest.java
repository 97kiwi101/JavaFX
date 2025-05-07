import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.*;


public class ToDoListAppTest {
    private Task task1;
    private Task task2;
    private Task task3;
    private List<Task> sampleTasks;

    @BeforeEach
    void setup() {
        // Create tasks for yesterday, today, and tomorrow
        task1 = new Task("Task1", "Desc1", LocalDate.now().toString());
        task2 = new Task("Task2", "Desc2", LocalDate.now().minusDays(1).toString());
        task3 = new Task("Task3", "Desc3", LocalDate.now().plusDays(1).toString());
        sampleTasks = Arrays.asList(task1, task2, task3);
    }

    @Test
    void testTaskConstructorAndGetters() {
        assertEquals("Task1", task1.getName());
        assertEquals("Desc1", task1.getDescription());
        assertEquals(LocalDate.now(), task1.getDate());
        assertFalse(task1.isComplete());
    }

    @Test
    void testTaskCompletionMethods() {
        assertFalse(task1.isComplete());
        task1.markComplete();
        assertTrue(task1.isComplete());
        task1.markIncomplete();
        assertFalse(task1.isComplete());
    }

    @Test
    void testTaskToString() {
        String incomplete = task1.toString();
        assertTrue(incomplete.contains("Task1"));
        assertTrue(incomplete.contains("Desc1"));
        assertFalse(incomplete.contains("✓"));

        task1.markComplete();
        String complete = task1.toString();
        assertTrue(complete.contains("✓"));
    }

    @Test
    void testToDoListAddGetAndToString() {
        ToDoList list = new ToDoList();
        assertTrue(list.getTasks().isEmpty());
        list.addTask(task1);
        assertEquals(1, list.getTasks().size());
        String repr = list.toString();
        assertTrue(repr.contains("Task1"));
    }

    @Test
    void testDailyToDoListFiltering() {
        ToDoList full = new ToDoList();
        full.addTask(task1);
        full.addTask(task2);
        DailyToDoList daily = new DailyToDoList(full, LocalDate.now());
        ToDoList filtered = daily.getToDoList();
        assertEquals(1, filtered.getTasks().size());
        assertTrue(filtered.getTasks().contains(task1));
    }

    @Test
    void testDailyToDoListCompletionAndWhatNeedsToBeDone() {
        ToDoList full = new ToDoList();
        full.addTask(task1);
        DailyToDoList daily = new DailyToDoList(full, LocalDate.now());
        assertFalse(daily.isTheDayDone());
        ToDoList needed = daily.whatNeedsToBeDone();
        assertEquals(1, needed.getTasks().size());

        task1.markComplete();
        assertTrue(daily.isTheDayDone());
        assertTrue(daily.whatNeedsToBeDone().getTasks().isEmpty());
    }

    @Test
    void testDateBasedWeeklyToDoListMapAndOverdue() {
        DateBasedWeeklyToDoList week = new DateBasedWeeklyToDoList(sampleTasks, LocalDate.now());
        Map<LocalDate, List<Task>> map = week.getWeekMap();
        assertTrue(map.get(LocalDate.now()).contains(task1));

        List<Task> overdue = DateBasedWeeklyToDoList.getOverdue(sampleTasks, LocalDate.now());
        assertTrue(overdue.contains(task2));
        assertFalse(overdue.contains(task1));
    }

    @Test
    void testWeeklyToDoListMethods() {
        List<String> days = Arrays.asList(
            LocalDate.now().minusDays(1).toString(),
            LocalDate.now().toString(),
            LocalDate.now().plusDays(1).toString()
        );
        WeeklyToDoList weekly = new WeeklyToDoList(new ArrayList<>(sampleTasks), new ArrayList<>(days));
        assertEquals(3, weekly.getWeeklyTasks().size());
        assertTrue(weekly.whatsDueOnThisDay(LocalDate.now().toString()).contains(task1));
        assertFalse(weekly.isTheWeekComplete());

        // Mark all tasks complete and verify
        task1.markComplete();
        task2.markComplete();
        task3.markComplete();
        assertTrue(weekly.isTheWeekComplete());
    }

    @Test
    void testKiwiGetDateTime() {
        assertEquals(LocalDate.now().toString(), kiwi.getDateTime());
    }
}
