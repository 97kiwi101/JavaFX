import java.util.ArrayList;

public class ToDoList extends ArrayList<String> {
    private ArrayList<Task> tasks;

    /**
     * Initializes a new ToDoList object.
     */
    public ToDoList() {
        tasks = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder rVal = new StringBuilder();
        for (Task t : tasks) {
            rVal.append(t).append("\n");
        }
        return rVal.toString();
    }

    /**
     * Adds a task to the list.
     * @param newTask the task to add
     */
    public void addTask(Task newTask) {
        tasks.add(newTask);
    }

    /**
     * Gets the contents of the ToDoList as an array.
     * @return an ArrayList of tasks
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }
}