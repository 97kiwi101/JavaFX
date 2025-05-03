// Main.java
// Created by Morgan Kaponga for Assignment10

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

public class Main extends Application {
    private static final String FILENAME = "Object.dat";
    private List<Task> allTasks;
    private DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE;
    private TabPane tabPane;
    private LocalDate today;

    private TextField nameField;
    private TextArea descField;
    private DatePicker datePicker;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        allTasks = loadOrSeedTasks();
        today = LocalDate.now();
        stage.setTitle("ToDo List - " + dtf.format(today));

        tabPane = new TabPane();
        refreshTabs();

        // Input area for adding and deleting
        nameField = new TextField();
        nameField.setPromptText("Task name");
        descField = new TextArea();
        descField.setPromptText("Description");
        descField.setPrefRowCount(2);
        datePicker = new DatePicker();
        datePicker.setPromptText("Due date");

        Button addBtn = new Button("Add Task");
        addBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String desc = descField.getText().trim();
            LocalDate date = datePicker.getValue();
            if (!name.isEmpty() && date != null) {
                Task t = new Task(name, desc, date.toString());
                allTasks.add(t);
                saveTasks();
                refreshTabs();
                nameField.clear(); descField.clear(); datePicker.setValue(null);
            }
        });

        Button deleteBtn = new Button("Delete Completed");
        deleteBtn.setOnAction(e -> {
            allTasks.removeIf(Task::isComplete);
            saveTasks();
            refreshTabs();
        });

        HBox inputBox = new HBox(10, nameField, descField, datePicker, addBtn, deleteBtn);
        inputBox.setPadding(new Insets(10));
        HBox.setHgrow(nameField, Priority.ALWAYS);
        HBox.setHgrow(descField, Priority.ALWAYS);
        HBox.setHgrow(datePicker, Priority.NEVER);

        BorderPane root = new BorderPane();
        root.setCenter(tabPane);
        root.setBottom(inputBox);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void refreshTabs() {
        tabPane.getTabs().clear();
        tabPane.getTabs().addAll(
            createWeeklyTab("This Week", today),
            createWeeklyTab("Next Week", today.plusWeeks(1)),
            createOverdueTab("Overdue", today)
        );
    }

    private Tab createWeeklyTab(String title, LocalDate startDate) {
        DateBasedWeeklyToDoList wlist = new DateBasedWeeklyToDoList(allTasks, startDate);
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        for (Map.Entry<LocalDate, List<Task>> entry : wlist.getWeekMap().entrySet()) {
            LocalDate date = entry.getKey();
            Label dateLabel = new Label(dtf.format(date));
            vbox.getChildren().add(dateLabel);

            List<Task> tasksForDay = entry.getValue();
            if (tasksForDay.isEmpty()) {
                vbox.getChildren().add(new Label("  (no tasks)"));
            } else {
                for (Task t : tasksForDay) {
                    CheckBox cb = new CheckBox(t.getName() + " (due " + dtf.format(t.getDate()) + ")");
                    cb.setSelected(t.isComplete());
                    cb.setOnAction(e -> {
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

    private Tab createOverdueTab(String title, LocalDate todayDate) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        List<Task> overdue = DateBasedWeeklyToDoList.getOverdue(allTasks, todayDate);
        if (overdue.isEmpty()) {
            vbox.getChildren().add(new Label("(no overdue tasks)"));
        } else {
            for (Task t : overdue) {
                CheckBox cb = new CheckBox(t.getName() + " (due " + dtf.format(t.getDate()) + ")");
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
        List<Task> sample = Arrays.asList(
            new Task("Finish report", "Wrap up Q2 financials", "2025-05-01"),
            new Task("Grocery shopping", "Buy ingredients for dinner", "2025-05-08"),
            new Task("Email Prof", "Ask about midterm", "2025-04-28")
        );
        saveTasks(sample);
        return new ArrayList<>(sample);
    }

    private void saveTasks() {
        saveTasks(this.allTasks);
    }

    private void saveTasks(List<Task> tasksToSave) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(tasksToSave);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
