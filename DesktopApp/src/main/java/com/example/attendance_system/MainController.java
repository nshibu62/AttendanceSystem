package com.example.attendance_system;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class MainController {
    @FXML
    private StackPane contentPane;

    @FXML
    void loadAttendance() {
        loadScreen("AttendanceReport.fxml");
    }

    @FXML
    void loadQuestionBank() {
        loadScreen("QuestionBank.fxml");
    }

    @FXML
    void loadPasswordBank() {
        loadScreen("PasswordBank.fxml");
    }

    @FXML
    void loadConfigureQuiz() {
        loadScreen("ConfigureQuiz.fxml");
    }

    private void loadScreen(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent screen = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(screen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
