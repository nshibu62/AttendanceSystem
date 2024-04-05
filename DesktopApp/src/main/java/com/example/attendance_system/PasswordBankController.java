package com.example.attendance_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.*;

public class PasswordBankController {

    // Reference to the container VBox
    @FXML
    private VBox containers;

    // Reference to the ListView for displaying passwords
    @FXML
    private ListView<String> passwordsListView;

    // ObservableList to hold passwords for ListView
    private ObservableList<String> passwords;

    // Initialize method called when FXML is loaded
    @FXML
    private void initialize() {
        // Initialize the list view
        passwords = FXCollections.observableArrayList();
        passwordsListView.setItems(passwords);
        passwordsListView.setVisible(false);
    }

    // Event handler for adding new password container
    @FXML
    private void addContainer() {
        TextField passwordText = new TextField();
        containers.getChildren().add(passwordText);
    }

    // Event handler for showing password bank
    @FXML
    private void showPasswordBank() {
        passwords.clear(); // Clear existing passwords

        // Connect to database
        try(Connection connection = DatabaseManagerUtils.getConnection()) {

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from passwords");

            while (resultSet.next()) {
                passwords.add(resultSet.getString("password"));
            }

            // Toggle visibility of the list view
            passwordsListView.setVisible(!passwordsListView.isVisible());

            // Close database connection
            DatabaseManagerUtils.closeConnection(connection);

        } catch (Exception e) {
            e.printStackTrace();
            AlertsUtils.showErrorAlert("Unable to display password bank.");
            //System.out.println("unsuccessful");
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

        // Connect to database
        try(Connection connection = DatabaseManagerUtils.getConnection()) {
            String sql = "INSERT INTO passwords (password) VALUES (?)";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, password);
            stmt.executeUpdate();

            // Display success alert
            AlertsUtils.showSuccessAlert("Password(s) added successfully!");

            //System.out.println("Password added to database successfully.");

            // Close database connection
            DatabaseManagerUtils.closeConnection(connection);

        } catch (SQLException e) {
            // Display fail alert
            AlertsUtils.showErrorAlert("Failed to add password(s).");
            //System.out.println(STR."Error inserting password into database: \{e.getMessage()}");
        }

    }
}