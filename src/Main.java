import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Main JavaFX Application class for the ToDo List GUI.
 * <p>
 * Provides tabs for viewing tasks due this week, next week, and overdue tasks.
 * Users can add new tasks, and delete completed tasks via the interface.
 */
public class Main extends Application {
    /**
     * vars
     */
    private static final String FILENAME = "Object.dat"; //Filename used for serializing the task list to disk.
    private List<Task> allTasks;//Imemory list of all Task objects loaded or created.
    private DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE;// Formatter for displaying dates in ISO_LOCAL_DATE format (yyyy-MM-dd).
    private TabPane tabPane;//The main TabPane containing weekly and overdue task tabs.
    private LocalDate today;//LocalDate representing today's date for filtering tasks.

    // UI components for task creation
    private TextField nameField;
    private TextArea descField;
    private DatePicker datePicker;

    /**
     * Standard JavaFX main method to launch the application.
     * 
     */
    public static void main(String[] args) {
        launch();  // Launches the JavaFX application
    }

    /**
     * Initializes and shows the primary stage.
     * 
     *   Loads or seeds tasks from file.
     *   Sets up UI components and layout.
     *   Defines event handlers for adding and deleting tasks.
     *   Displays the stage.
     *
     * @param stage the primary stage provided by JavaFX
     */
    @Override
    public void start(Stage stage) {
        // Load existing tasks or create sample tasks
        allTasks = loadOrSeedTasks();
        today = LocalDate.now();  // Capture today's date
        stage.setTitle("ToDo List - " + dtf.format(today));

        // Create and populate the tab pane
        tabPane = new TabPane();
        refreshTabs();  // Build tabs for this week, next week, overdue

        // Set up input fields for new tasks
        nameField = new TextField();
        nameField.setPromptText("Task name");

        descField = new TextArea();
        descField.setPromptText("Description");
        descField.setPrefRowCount(2);

        datePicker = new DatePicker();
        datePicker.setPromptText("Due date");

        // Button to add a new task
        Button addBtn = new Button("Add Task");
        addBtn.setOnAction(e -> {//https://www.geeksforgeeks.org/lambda-expressions-java-8/
            String name = nameField.getText().trim();
            String desc = descField.getText().trim();
            LocalDate date = datePicker.getValue();
            // Only add if name and date are provided
            if (!name.isEmpty() && date != null) {
                Task t = new Task(name, desc, date.toString());
                allTasks.add(t);
                saveTasks();          // Persist updated list
                refreshTabs();        // Refresh UI
                // Clear input fields
                nameField.clear();
                descField.clear();
                datePicker.setValue(null);
            }
        });

        // Button to delete all completed tasks
        Button deleteBtn = new Button("Delete Completed");
        deleteBtn.setOnAction(e -> {allTasks.removeIf(Task::isComplete);saveTasks();refreshTabs();});

        // Layout for input controls
        HBox inputBox = new HBox(10, nameField, descField, datePicker, addBtn, deleteBtn);
        inputBox.setPadding(new Insets(10));
        HBox.setHgrow(nameField, Priority.ALWAYS);
        HBox.setHgrow(descField, Priority.ALWAYS);

        // Root layout for the scene
        BorderPane root = new BorderPane();
        root.setCenter(tabPane);     // Main content
        root.setBottom(inputBox);    // Input area

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();                // Display the UI
    }

    /**
     * Rebuilds the TabPane with updated task lists.
     * Includes tabs for "This Week", "Next Week", and "Overdue".
     */
    private void refreshTabs() {
        tabPane.getTabs().clear();
        // Add a weekly tab for the current week
        tabPane.getTabs().addAll(
            createWeeklyTab("This Week", today),
            createWeeklyTab("Next Week", today.plusWeeks(1)),
            createOverdueTab("Overdue", today)
        );
    }

    /**
     * Creates a tab showing tasks for a 7-day window starting at startDate.
     * Each task is represented by a CheckBox to mark completion.
     *
     * @param title     the tab label
     * @param startDate the first date of the week window
     * @return a non-closable Tab with tasks listed
     */
    private Tab createWeeklyTab(String title, LocalDate startDate) {
        DateBasedWeeklyToDoList wlist = new DateBasedWeeklyToDoList(allTasks, startDate);
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        // Iterate each day in the week map
        for (Map.Entry<LocalDate, List<Task>> entry : wlist.getWeekMap().entrySet()) {
            LocalDate date = entry.getKey();
            Label dateLabel = new Label(dtf.format(date));
            vbox.getChildren().add(dateLabel);

            List<Task> tasksForDay = entry.getValue();
            if (tasksForDay.isEmpty()) {
                vbox.getChildren().add(new Label("  (no tasks)"));
            } else {
                // Add a CheckBox for each task
                for (Task t : tasksForDay) {
                    CheckBox cb = new CheckBox(t.getName() + ": " + t.getDescription() + " (due " + dtf.format(t.getDate()) + ")");
                    cb.setSelected(t.isComplete());
                    cb.setOnAction(e -> {
                        // Toggle task completion and refresh
                        if (cb.isSelected()) t.markComplete(); else t.markIncomplete();
                        saveTasks();
                        refreshTabs();
                    });
                    vbox.getChildren().add(cb);
                }
            }
        }

        ScrollPane sp = new ScrollPane(vbox);
        Tab tab = new Tab(title, sp);
        tab.setClosable(false);
        return tab;
    }

    /**
     * Creates a tab listing all overdue tasks relative to todayDate.
     * Similar in structure to weekly tabs but only for tasks before today.
     *
     * @param title     the tab label (e.g., "Overdue")
     * @param todayDate reference date for overdue calculation
     * @return a non-closable Tab with overdue tasks
     */
    private Tab createOverdueTab(String title, LocalDate todayDate) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        List<Task> overdue = DateBasedWeeklyToDoList.getOverdue(allTasks, todayDate);
        if (overdue.isEmpty()) {
            vbox.getChildren().add(new Label("(no overdue tasks)"));
        } else {
            for (Task t : overdue) {
                CheckBox cb = new CheckBox(t.getName() + ": " + t.getDescription() + " (due " + dtf.format(t.getDate()) + ")");
                cb.setSelected(t.isComplete());
                cb.setOnAction(e -> {
                    if (cb.isSelected()) t.markComplete(); else t.markIncomplete();
                    saveTasks();
                    refreshTabs();
                });
                vbox.getChildren().add(cb);
            }
        }

        ScrollPane sp = new ScrollPane(vbox);
        Tab tab = new Tab(title, sp);
        tab.setClosable(false);
        return tab;
    }

    /**
     * Loads existing tasks from file or seeds with sample tasks.
     * If deserialization fails or file absent, creates and saves sample data.
     *
     * @return List of Task objects to populate the UI
     */
    @SuppressWarnings("unchecked")
    private List<Task> loadOrSeedTasks() {
        File file = new File(FILENAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (List<Task>) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Seed default tasks if no file found
        List<Task> sample = Arrays.asList(
            new Task("Finish report", "Wrap up Q2 financials", "2025-05-01"),
            new Task("Grocery shopping", "Buy ingredients for dinner", "2025-05-08"),
            new Task("Email Prof", "Ask about midterm", "2025-04-28")
        );
        saveTasks(sample);
        return new ArrayList<>(sample);
    }

    /**
     * Saves the current allTasks list to disk via serialization.
     */
    private void saveTasks() {
        saveTasks(this.allTasks);
    }

    /**
     * Serializes the provided task list to the designated FILENAME.
     *
     * @param tasksToSave the List<Task> to write out
     */
    private void saveTasks(List<Task> tasksToSave) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(tasksToSave);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
