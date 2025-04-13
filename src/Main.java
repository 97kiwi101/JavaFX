// Created by Morgan Kaponga for Assignment10

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

public class Main extends Application 
{
    private ToDoList currentList;
    private VBox checkBoxDisplay;
    private TextField taskNameInput;
    private TextArea taskDescriptionInput;

    public void updateCheckBoxDisplay()
    {
        checkBoxDisplay.getChildren().clear();
        for (Task task : currentList) 
        {
            CheckBox checkBox = new CheckBox(task.getName());
            checkBox.setSelected(task.isComplete());

            Label descriptionLabel = new Label(task.toString());
            checkBoxDisplay.getChildren().addAll(checkBox, descriptionLabel);

            // Set the event handler for each checkbox
            checkBox.setOnAction(new CheckBoxHandler(task, descriptionLabel));
            VBox taskBox = new VBox(5, checkBox, descriptionLabel);

        if (task.isComplete())// when the update the UI is called to update then it will check if its completed or not
        {
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(new ButtonClickHandler(task)); //calls the new class with the task at had so it can be deleted
            taskBox.getChildren().add(deleteButton);
        }

        checkBoxDisplay.getChildren().add(taskBox);


        }
    }

    //when it gets called it will remove the class and update the UI again
    class ButtonClickHandler implements EventHandler<ActionEvent> 
    {
        private Task taskToDelete;

        public ButtonClickHandler(Task taskToDelete){
            this.taskToDelete = taskToDelete;
        }

        public void handle(ActionEvent event){

                    currentList.remove(taskToDelete);
                    updateCheckBoxDisplay();
                }
            };



    public ToDoList getSampleData()
    {
        ToDoList myList = new ToDoList();
        Task myTask = new Task("Buy Groceries","eggs, bread, milk");
        myTask.markComplete();
        Task myOtherTask = new Task("Finish Assignment 9 in CS 67","I need to think of an idea for a subclass");
        Task myThirdTask = new Task("Send resume to career services","send to get feedback before applying for interships");

        myList.addTask(myTask);
        myList.addTask(myOtherTask);
        myList.addTask(myThirdTask);

        return myList;
    }
    

    @Override
    public void start(Stage stage) 
    {
        currentList = getSampleData();
    
        checkBoxDisplay = new VBox(10); // VBox with spacing between elements
        // Add padding to the VBox (Insets are top, right, bottom, left)
        checkBoxDisplay.setPadding(new Insets(10, 10, 10, 20)); // 20px padding on the left, 10px on the other sides
        
        updateCheckBoxDisplay();
        ScrollPane scrollableCheckBoxDisplay = new ScrollPane(checkBoxDisplay);
        

        // Create the input area at the bottom
        taskNameInput = new TextField();
        taskNameInput.setPromptText("Enter task name");

        taskDescriptionInput = new TextArea();
        taskDescriptionInput.setPromptText("Enter task description");

        Button addButton = new Button("Add Task");
        addButton.setOnAction(new AddTaskHandler());

        // Create a VBox for the input area
        VBox inputArea = new VBox(5, taskNameInput, taskDescriptionInput, addButton);
        inputArea.setPadding(new Insets(10, 10, 10, 20));

        // Create a root container that holds both the task list and the input area
        VBox root = new VBox(scrollableCheckBoxDisplay,inputArea);
        Scene scene = new Scene(root, 640, 480);
        
        stage.setScene(scene);
        stage.show();
    }

    
    public static void main(String[] args) 
    {
        launch();
    }

    private class CheckBoxHandler implements EventHandler<ActionEvent> 
    {
        private Task task;
        private Label descriptionLabel;

        public CheckBoxHandler(Task task, Label descriptionLabel) 
        {
            this.task = task;
            this.descriptionLabel = descriptionLabel;
        }

        @Override
        public void handle(ActionEvent event) 
        {
            CheckBox checkBox = (CheckBox) event.getSource();
            if (checkBox.isSelected()) 
            {
                task.markComplete();
                updateCheckBoxDisplay();//when the user checks it off the delete button will now appear
            } else 
            {
                task.markIncomplete();
                updateCheckBoxDisplay();//when the user checks it off the delete button will now appear
            }
            // Update the label to reflect the task status
            descriptionLabel.setText(task.toString());
        }
    }

    private class AddTaskHandler implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event) 
        {
            Task newTask = new Task(taskNameInput.getText(),taskDescriptionInput.getText());
            currentList.add(newTask);
            updateCheckBoxDisplay();
        }

    }



}