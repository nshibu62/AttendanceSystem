package com.example.attendance_system;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;

public class PasswordBankController {

    @FXML
    private VBox containers;

    @FXML
    private void addContainer() {
        TextField passwordText = new TextField();
        containers.getChildren().add(passwordText);
    }
    @FXML
    private void handleSubmitButtonClicked() {
        for (javafx.scene.Node node : containers.getChildren()) {
            if (node instanceof TextField) {
                String password = ((TextField) node).getText().trim();
                if (!password.isEmpty()) {
                    insertPasswordIntoDatabase(password);
                }
            }
        }
    }

    private void insertPasswordIntoDatabase(String password) {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "password");

            String sql = "INSERT INTO passwords (password) VALUES (?)";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, password);
            stmt.executeUpdate();

            System.out.println("Password added to database successfully.");

        } catch (SQLException e) {
            System.out.println("Error inserting password into database: " + e.getMessage());
        }

    }
}

