import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private LocalDate dueDate;
    private boolean complete;

    /** 
     * @param n   task name
     * @param d   description
     * @param dd  due date string in “yyyy-MM-dd”
     */
    public Task(String n, String d, String dd) {
        this.name = n;
        this.description = d;
        this.dueDate = LocalDate.parse(dd);
        this.complete = false;
    }
    

    @Override
    public String toString() {
        return name + " (due " + dueDate + ")" + (complete ? " ✓" : "") + "\n  " + description;
    }

    // Getters
    /**
     * Returns the name of the task.
     * @return the name of the task
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the task.
     * @return the description of the task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the due date of the task.
     * @return the due date of the task
     */
    public LocalDate getDate() {
        return dueDate;
    }

    /**
     * Returns whether the task is complete.
     * @return true if the task is complete, false otherwise
     */
    public boolean isComplete() {
        return complete;
    }

    // Setters
    /**
     * Sets the name of the task.
     * @param name the new name of the task
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the description of the task.
     * @param description the new description of the task
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the due date of the task.
     * @param dueDate the new due date of the task
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Marks the task as complete.
     */
    public void markComplete() {
        complete = true;
    }

    /**
     * Marks the task as incomplete.
     */
    public void markIncomplete() {
        complete = false;
    }


}
