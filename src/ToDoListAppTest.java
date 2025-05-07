import static org.junit.Assert.*;
import org.junit.Test;

import java.time.LocalDate;
import java.util.*;

public class ToDoListAppTest {
    private Task task1;
    private Task task2;
    private Task task3;
    private List<Task> sampleTasks;
    private kiwi kiwi;

    // JUnit 4 creates a fresh instance per @Test, so this constructor runs before each test
    public ToDoListAppTest() {
        task1 = new Task("Task1", "Desc1", LocalDate.now());
        task2 = new Task("Task2", "Desc2", LocalDate.now().minusDays(1));
        task3 = new Task("Task3", "Desc3", LocalDate.now().plusDays(1));
        sampleTasks = Arrays.asList(task1, task2, task3);
        kiwi = new kiwi();
    }

    @Test
    public void testTaskConstructorAndGetters() {
        assertEquals("Task1", task1.getName());
        assertEquals("Desc1", task1.getDescription());
        assertEquals(LocalDate.now(), task1.getDate());
        assertFalse(task1.isComplete());
    }

    @Test
    public void testTaskCompletionMethods() {
        assertFalse(task1.isComplete());
        task1.markComplete();
        assertTrue(task1.isComplete());
        task1.markIncomplete();
        assertFalse(task1.isComplete());
    }

    @Test
    public void testTaskToString() {
        String incomplete = task1.toString();
        assertTrue(incomplete.contains("Task1"));
        assertTrue(incomplete.contains("Desc1"));
        assertFalse(incomplete.contains("✓"));

        task1.markComplete();
        String complete = task1.toString();
        assertTrue(complete.contains("✓"));
    }

    @Test
    public void testToDoListAddGetAndToString() {
        ToDoList list = new ToDoList();
        assertTrue(list.getTasks().isEmpty());
        list.addTask(task1);
        assertEquals(1, list.getTasks().size());
        assertTrue(list.toString().contains("Task1"));
    }

    @Test
    public void testDailyToDoListFiltering() {
        ToDoList full = new ToDoList();
        full.addTask(task1);
        full.addTask(task2);
        DailyToDoList daily = new DailyToDoList(full, LocalDate.now());
        ToDoList filtered = daily.getToDoList();
        assertEquals(1, filtered.getTasks().size());
        assertTrue(filtered.getTasks().contains(task1));
    }

    @Test
    public void testDailyToDoListCompletionAndWhatNeedsToBeDone() {
        ToDoList full = new ToDoList();
        full.addTask(task1);
        DailyToDoList daily = new DailyToDoList(full, LocalDate.now());
        assertFalse(daily.isTheDayDone());
        assertEquals(1, daily.whatNeedsToBeDone().getTasks().size());

        task1.markComplete();
        assertTrue(daily.isTheDayDone());
        assertTrue(daily.whatNeedsToBeDone().getTasks().isEmpty());
    }

    @Test
    public void testDateBasedWeeklyToDoListMapAndOverdue() {
        DateBasedWeeklyToDoList week = new DateBasedWeeklyToDoList(sampleTasks, LocalDate.now());
        Map<LocalDate, List<Task>> map = week.getWeekMap();
        assertTrue(map.get(LocalDate.now()).contains(task1));

        List<Task> overdue = DateBasedWeeklyToDoList.getOverdue(sampleTasks, LocalDate.now());
        assertTrue(overdue.contains(task2));
        assertFalse(overdue.contains(task1));
    }

    @Test
    public void testWeeklyToDoListMethods() {
        // 1) Prepare a three-day window as LocalDate
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate today     = LocalDate.now();
        LocalDate tomorrow  = LocalDate.now().plusDays(1);
        List<LocalDate> days = Arrays.asList(yesterday, today, tomorrow);

        // 2) Create one Task per day
        Task task1 = new Task("Task 1","kiwi" , yesterday);
        Task task2 = new Task("Task 2", "kiwi" , today);
        Task task3 = new Task("Task 3", "kiwi" , tomorrow);
        List<Task> sampleTasks = Arrays.asList(task1, task2, task3);

        // 3) Build the weekly list
        WeeklyToDoList weekly = new WeeklyToDoList(
            new ArrayList<Task>(sampleTasks),
            new ArrayList<LocalDate>(days)
        );

        // 4) Expect one bucket per day
        assertEquals(
            "Should have exactly 3 daily buckets for yesterday, today, and tomorrow",
            3,
            weekly.getWeeklyTasks().size()
        );

        // 5) Initially the week is NOT complete (each bucket has one incomplete task)
        assertFalse(
            "Week should NOT be complete before marking any tasks",
            weekly.isTheWeekComplete()
        );

        // 6) Mark all tasks complete
        task1.markComplete();
        task2.markComplete();
        task3.markComplete();

        // 7) Now the week should be complete
        assertTrue(
            "Week should be complete after marking all tasks as complete",
            weekly.isTheWeekComplete()
        );
    }

    @Test
    public void testKiwiGetDateTime() {
        assertEquals(LocalDate.now().toString(), kiwi.getDateTime());
    }
}