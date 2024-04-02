package com.example.attendance_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.*;

public class PasswordBankController {
    @FXML
    private VBox containers;

    @FXML
    private ListView<String> passwordsListView;

    private ObservableList<String> passwords;

    @FXML
    private void initialize() {
        // Initialize the list view
        passwords = FXCollections.observableArrayList();
        passwordsListView.setItems(passwords);
        passwordsListView.setVisible(false);
    }

    @FXML
    private void addContainer() {
        TextField passwordText = new TextField();
        containers.getChildren().add(passwordText);
    }

    @FXML
    private void showPasswordBank() {
        passwords.clear(); // Clear existing passwords
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "password");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from passwords");

            while (resultSet.next()) {
                passwords.add(resultSet.getString("password"));
            }

            // Toggle visibility of the list view
            passwordsListView.setVisible(!passwordsListView.isVisible());




        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("unsuccessful");
        }
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

    @FXML
    private void insertPasswordIntoDatabase(String password) {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendanceDB", "root", "password");

            String sql = "INSERT INTO passwords (password) VALUES (?)";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, password);
            stmt.executeUpdate();

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Passwords added successfully.");
            successAlert.showAndWait();

            System.out.println("Password added to database successfully.");

        } catch (SQLException e) {
            System.out.println("Error inserting password into database: " + e.getMessage());
        }

    }
}
