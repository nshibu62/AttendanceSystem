package com.example.attendance_system;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;


public class QuestionBankController {
    @FXML
    private VBox containers;

    @FXML
    private ComboBox<String> courseBox;

    private String selectedCourse; // Variable to store the selected course
    //int professorId = 2;

    @FXML
    private void initialize() {
        // Fetch course names from the database and populate the ComboBox
        try (Connection connection = DatabaseManagerUtils.getConnection()) {
            String sql = "SELECT class_id FROM classes";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String course_id = resultSet.getString("class_id");
                    courseBox.getItems().add(course_id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add listener to handle selection change
        courseBox.setOnAction(event -> handleCourseSelection());
    }

    @FXML
    private void addContainer() {
        // Creating UI components for a new question
        HBox questionContainer = new HBox();
        HBox AContainer = new HBox();
        HBox BContainer = new HBox();
        HBox CContainer = new HBox();
        HBox DContainer = new HBox();

        Label question = new Label("Question");
        TextField questionText = new TextField();
        Label optionA = new Label("Option A");
        TextField answerA = new TextField();
        Label optionB = new Label("Option B");
        TextField answerB = new TextField();
        Label optionC = new Label("Option C");
        TextField answerC = new TextField();
        Label optionD = new Label("Option D");
        TextField answerD = new TextField();

        ComboBox<String> answerBox = new ComboBox<>();
        answerBox.getItems().addAll("A", "B", "C", "D");
        answerBox.setValue("Answer");

        // Adding components to their respective containers
        questionContainer.getChildren().addAll(question, questionText);
        AContainer.getChildren().addAll(optionA, answerA);
        BContainer.getChildren().addAll(optionB, answerB);
        CContainer.getChildren().addAll(optionC, answerC);
        DContainer.getChildren().addAll(optionD, answerD);

        // Adding components to the main container
        containers.getChildren().addAll(questionContainer, AContainer, BContainer, CContainer, DContainer, answerBox);

        // Adding submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> submitQuestion());
        containers.getChildren().add(submitButton);
    }

    private void submitQuestion() {
        // Extracting data from UI components
        String questionText = ((TextField) ((HBox) containers.getChildren().get(0)).getChildren().get(1)).getText();
        String optionA = ((TextField) ((HBox) containers.getChildren().get(1)).getChildren().get(1)).getText();
        String optionB = ((TextField) ((HBox) containers.getChildren().get(2)).getChildren().get(1)).getText();
        String optionC = ((TextField) ((HBox) containers.getChildren().get(3)).getChildren().get(1)).getText();
        String optionD = ((TextField) ((HBox) containers.getChildren().get(4)).getChildren().get(1)).getText();
        String correctAnswer = ((ComboBox<String>) containers.getChildren().get(5)).getValue();

        // Establishing connection to the database
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "password")) {

            // Preparing SQL statement
            String sql = "INSERT INTO question (class_id, question, option_1, option_2, option_3, option_4, answer) " +
                    "VALUES ((SELECT class_id FROM classes WHERE class_id = ?), ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // Setting parameters
                statement.setString(1, selectedCourse);
                statement.setString(2, questionText);
                statement.setString(3, optionA);
                statement.setString(4, optionB);
                statement.setString(5, optionC);
                statement.setString(6, optionD);
                statement.setString(7, correctAnswer);

                // Executing the statement
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    AlertsUtils.showSuccessAlert("Question(s) added successfully!");
                } else {
                    AlertsUtils.showErrorAlert("Failed to add password(s).");
                    // Optionally, you can show an error message to the user
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());

        }
    }

    private void handleCourseSelection() {
        selectedCourse = courseBox.getValue(); // Store the selected course
    }


}