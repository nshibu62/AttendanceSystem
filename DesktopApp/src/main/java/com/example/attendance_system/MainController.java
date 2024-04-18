package com.example.attendance_system;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class MainController {
    @FXML
    private StackPane contentPane; // StackPane to hold loaded screens

    // Method to load the attendance report screen
    @FXML
    void loadAttendance() {
        loadScreen("AttendanceReport.fxml");
    }

    // Method to load the question bank screen
    @FXML
    void loadQuestionBank() {
        loadScreen("QuestionBank.fxml");
    }

    // Method to load the password bank screen
    @FXML
    void loadPasswordBank() {
        loadScreen("PasswordBank.fxml");
    }

    // Method to load the configure quiz screen
    @FXML
    void loadConfigureQuiz() {
        loadScreen("ConfigureQuiz.fxml");
    }

    // Method to load the add class screen
    @FXML
    void loadAddClass() {
        loadScreen("AddClass.fxml");
    }

    // Method to load a screen based on the provided FXML file
    private void loadScreen(String fxmlFile) {
        try {
            // Load the FXML file for the specified screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent screen = loader.load();

            // Clear the content pane and add the loaded screen
            contentPane.getChildren().clear();
            contentPane.getChildren().add(screen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
