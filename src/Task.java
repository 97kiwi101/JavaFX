import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private LocalDate dueDate;
    private boolean complete;

    /**
     * Constructs a new Task.
     *
     * @param name        the task name
     * @param description the task description
     * @param dueDate     the due date as a LocalDate
     */
    public Task(String name, String description, LocalDate dueDate) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.complete = false;
    }

    @Override
    public String toString() {
        return name + " (due " + dueDate + ")" + (complete ? " ✓" : "") + "\n  " + description;
    }

    // Getters

    /**
     * Returns the name of the task.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the due date of the task.
     */
    public LocalDate getDate() {
        return dueDate;
    }

    /**
     * Returns whether the task is complete.
     */
    public boolean isComplete() {
        return complete;
    }

    // Setters

    /**
     * Sets the name of the task.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the description of the task.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the due date of the task.
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /** Marks the task as complete. */
    public void markComplete() {
        complete = true;
    }

    /** Marks the task as incomplete. */
    public void markIncomplete() {
        complete = false;
    }
}