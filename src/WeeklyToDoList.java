// WeeklyToDoList.java
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class WeeklyToDoList implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<ArrayList<Task>> weeklyTasks;
    private ArrayList<Task> listOfDailyTasks;
    private ArrayList<String> days;
        // Constructor
        /**
         * Constructs a new WeeklyToDoList.
         * @param listOfDailyTasks the list of daily tasks
         * @param days the list of days
         */
        public WeeklyToDoList(ArrayList<Task> listOfDailyTasks, ArrayList<String> days) {
            this.weeklyTasks = new ArrayList<>();
            this.days = days;
            this.listOfDailyTasks = listOfDailyTasks;

            // Makes the week the size of the days list
            for (int i = 0; i < days.size(); i++) {
                weeklyTasks.add(new ArrayList<>());
            }

            // Fills in the tasks for that day
            for (Task task : listOfDailyTasks) {
                LocalDate taskDate = task.getDate();
                int dayIndex = days.indexOf(taskDate);
                if (dayIndex != -1) {
                    weeklyTasks.get(dayIndex).add(task);
                }
            }
        }

        /**
         * Builds the internal weekly to-do list based on the provided list of daily tasks and days.
         * @param listOfDailyTasks the list of daily tasks
         * @param days the list of days
         */
        private void internalBuilder(ArrayList<Task> listOfDailyTasks, ArrayList<String> days) {
            this.weeklyTasks.clear();

            // Makes the week the size of the days list
            for (int i = 0; i < days.size(); i++) {
                weeklyTasks.add(new ArrayList<>());
            }

            // Fills in the tasks for that day
            for (Task task : listOfDailyTasks) {
                LocalDate taskDate = task.getDate();
                int dayIndex = days.indexOf(taskDate);
                if (dayIndex != -1) {
                    weeklyTasks.get(dayIndex).add(task);
                }
            }
        }

        // Getters
        /**
         * Returns the weekly tasks.
         * @return the weekly tasks
         */
        public ArrayList<ArrayList<Task>> getWeeklyTasks() {
            return weeklyTasks;
        }

        /**
         * Returns the days.
         * @return the days
         */
        public ArrayList<String> getDays() {
            return days;
        }

        // Setters
        /**
         * Sets the list of daily tasks and rebuilds the weekly to-do list.
         * @param listOfDailyTasks the list of daily tasks
         */
        public void setlistOfDailyTasks(ArrayList<Task> listOfDailyTasks) {
            this.listOfDailyTasks = listOfDailyTasks;
            internalBuilder(listOfDailyTasks, this.days);
        }

        /**
         * Sets the days and rebuilds the weekly to-do list.
         * @param days the days to set
         */
        public void setDays(ArrayList<String> days) {
            this.days = days;
            internalBuilder(this.listOfDailyTasks, days);
        }

        // Fun random methods
        /**
         * Returns the tasks due on a specific day.
         * @param day the day to check
         * @return the tasks due on the specified day
         */
        public ArrayList<Task> whatsDueOnThisDay(String day) {
            int index = days.indexOf(day);
            if (index != -1) {
                return weeklyTasks.get(index);
            }
            return new ArrayList<>();
        }

        /**
         * Checks if all tasks for the week are complete.
         * @return true if all tasks are complete, false otherwise
         */
        public boolean isTheWeekComplete() {
            for (ArrayList<Task> dailyTasks : weeklyTasks) {
                for (Task task : dailyTasks) {
                    if (!task.isComplete()) {
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < days.size(); i++) {
            String day = days.get(i);
            sb.append(day).append(":\n");
            ArrayList<Task> tasksForDay = weeklyTasks.get(i);
            if (tasksForDay.isEmpty()) {
                sb.append("  (no tasks)\n");
            } else {
                for (Task t : tasksForDay) {
                    sb.append("  - ").append(t.getName())
                      .append(" [").append(t.getDate()).append("]")
                      .append(t.isComplete() ? " ✓" : "")
                      .append("\n");
                }
            }
        }
        return sb.toString();
    }
    }