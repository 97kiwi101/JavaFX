import java.time.LocalDate;

/**
 * A more topical and relevant subset of the ToDoList.
 */
public class DailyToDoList {
    private ToDoList toDoList;
    private ToDoList fullToDoList;
    private LocalDate day;

    // Constructor
    /**
     * Constructs a new DailyToDoList for a specific day.
     * @param t the name of the list you are separating the daily list from
     * @param day the day you want the tasks for
     */
    public DailyToDoList(ToDoList t, LocalDate day) {
        this.day = day;
        fullToDoList = t;
        toDoList = new ToDoList();
        for (Task task : t.getTasks()) {
            if (task.getDate().isEqual(day)) {
                toDoList.addTask(task);
            }
        }
    }

    /**
     * Builds the internal daily to-do list based on the provided full to-do list and day.
     * @param t the full to-do list
     * @param day2 the day for which tasks are to be filtered
     */
    private void internalBuilder(ToDoList t, LocalDate day2) {
        toDoList.clear();
        for (Task task : t.getTasks()) {
            if (task.getDate().isEqual(day)) {
                toDoList.addTask(task);
            }
        }
    }

    // Getter
    /**
     * Returns the daily to-do list.
     * @return the daily to-do list
     */
    public ToDoList getToDoList() {
        return toDoList;
    }

    /**
     * Returns the day for which the daily to-do list is created.
     * @return the day
     */
    public LocalDate getDay() {
        return day;
    }

    // Setter
    /**
     * Sets the full to-do list and rebuilds the daily to-do list.
     * @param fullToDoList the full to-do list
     */
    public void setfullToDoList(ToDoList fullToDoList) {
        fullToDoList.clear();
        this.fullToDoList = fullToDoList;
        internalBuilder(fullToDoList, this.day);
    }

    /**
     * Sets the day and rebuilds the daily to-do list.
     * @param day the day to set
     */
    public void setDay(LocalDate day) {
        this.day = day;
        internalBuilder(this.fullToDoList, day);
    }

    // Fun random methods
    /**
     * Checks if all tasks for the day are complete.
     * @return true if all tasks are complete, false otherwise
     */
    public boolean isTheDayDone() {
        for (Task task : toDoList.getTasks()) {
            if (!task.isComplete()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a list of tasks that are not yet complete.
     * @return a to-do list of incomplete tasks
     */
    public ToDoList whatNeedsToBeDone() {
        ToDoList incompleted = new ToDoList();
        for (Task task : toDoList.getTasks()) {
            if (!task.isComplete()) {
                incompleted.addTask(task);
            }
        }
        return incompleted;
    }
}
