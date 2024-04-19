package com.example.attendance_system;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * Controller class for the question bank UI.
 */
public class QuestionBankController {
    @FXML
    private VBox containers;

    @FXML
    private ComboBox<String> courseBox;

    private String selectedCourse; // Variable to store the selected course
    int professorId = ProfessorManager.getInstance().getProfessorId();
    //int professorId = 1;
    //int professorId = 2;

    /**
     * Initializes the controller.
     * Fetches course names from the database and populates the ComboBox.
     */
    @FXML
    private void initialize() {
        // Fetch course names from the database and populate the ComboBox
        ComboBoxUtils.populateComboBox(courseBox, professorId);
    }

    /**
     * Adds a new question container.
     * Creates UI components for a new question and adds them to the main container.
     */
    @FXML
    private void addContainer() {
        // Creating UI components for a new question
        VBox questionSet = new VBox(); // VBox to contain all components for one question
        HBox questionContainer = new HBox();
        HBox AContainer = new HBox();
        HBox BContainer = new HBox();
        HBox CContainer = new HBox();
        HBox DContainer = new HBox();

        // Labels and text fields for question and options
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

        // Adding components to containers
        questionContainer.getChildren().addAll(question, questionText);
        AContainer.getChildren().addAll(optionA, answerA);
        BContainer.getChildren().addAll(optionB, answerB);
        CContainer.getChildren().addAll(optionC, answerC);
        DContainer.getChildren().addAll(optionD, answerD);

        // Adding spacing between each set of containers
        questionSet.setSpacing(10); // Adjust as needed

        // Adding components to the main container
        questionSet.getChildren().addAll(questionContainer, AContainer, BContainer, CContainer, DContainer, answerBox);
        containers.getChildren().addAll(questionSet);
    }

    /**
     * Submits all questions.
     * Extracts data from UI components for each question set and submits to the database.
     * Clears all containers and resets after submission.
     */
    @FXML
    private void submitQuestions() {
        // Loop through each question set
        for (Node node : containers.getChildren()) {
            if (node instanceof VBox) {
                VBox questionSet = (VBox) node;
                // Retrieve data from the question set
                String questionText = ((TextField) ((HBox) questionSet.getChildren().get(0)).getChildren().get(1)).getText();
                String optionA = ((TextField) ((HBox) questionSet.getChildren().get(1)).getChildren().get(1)).getText();
                String optionB = ((TextField) ((HBox) questionSet.getChildren().get(2)).getChildren().get(1)).getText();
                String optionC = ((TextField) ((HBox) questionSet.getChildren().get(3)).getChildren().get(1)).getText();
                String optionD = ((TextField) ((HBox) questionSet.getChildren().get(4)).getChildren().get(1)).getText();
                String correctAnswer = ((ComboBox<String>) questionSet.getChildren().get(5)).getValue();

                // Submit the question
                submitQuestion(questionText, optionA, optionB, optionC, optionD, correctAnswer);
            }
        }

        // Clear all containers and reset
        containers.getChildren().clear();
        courseBox.getSelectionModel().clearSelection();
    }


    /**
     * Submits a single question to the database.
     * Prepares and executes the SQL statement for inserting the question data.
     * Shows success or error message based on insertion status.
     *
     * @param questionText   The text of the question.
     * @param optionA        Option A.
     * @param optionB        Option B.
     * @param optionC        Option C.
     * @param optionD        Option D.
     * @param correctAnswer  The correct answer.
     */
    @FXML
    private void submitQuestion(String questionText, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        // Establishing connection to the database
        try (Connection connection = DatabaseManagerUtils.getConnection()) {

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
                    AlertsUtils.showErrorAlert("Failed to add question(s).");
                    // Optionally, you can show an error message to the user
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles the selection of a course from the ComboBox.
     * Stores the selected course in a variable.
     * Clears all containers and resets to one empty container.
     */
    @FXML
    private void handleCourseSelection() {
        // Store the selected course
        selectedCourse = courseBox.getValue();

        // Clear all containers and reset to one empty container
        containers.getChildren().clear();
    }


}
