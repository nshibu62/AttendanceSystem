package com.example.attendance_system;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    @FXML
    private Text actiontarget; // Text field to display action feedback

    // Method to handle button click event
    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        // Update action target text
        actiontarget.setText("Sign in button pressed");

        // Load the main screen upon button click
        loadMainScreen(event);
    }

    // Method to load the main screen
    private void loadMainScreen(ActionEvent event) {
        try {
            // Load the FXML file for the main screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
            Parent mainScreenParent = loader.load();

            // Create a new scene with the main screen layout
            Scene mainScreenScene = new Scene(mainScreenParent);

            // Get the stage (window) from the event source (button)
            Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();

            // Set the main screen scene on the stage
            window.setScene(mainScreenScene);

            // Show the main screen
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
