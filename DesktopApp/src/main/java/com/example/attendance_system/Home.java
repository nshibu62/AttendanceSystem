package com.example.attendance_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Home extends Application {
    // This method is automatically called when the application starts
    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file that describes the UI layout
        Parent root = FXMLLoader.load(getClass().getResource("home-view.fxml"));

        // Create a new scene with the root layout, specifying its width and height
        Scene scene = new Scene(root, 300, 275);

        // Set the title of the stage (window)
        stage.setTitle("Login");

        // Set the scene for the stage
        stage.setScene(scene);

        // Display the stage
        stage.show();
    }

    // The main method is the entry point of the JavaFX application
    public static void main(String[] args) {
        // Launch the application
        launch();
    }
}
