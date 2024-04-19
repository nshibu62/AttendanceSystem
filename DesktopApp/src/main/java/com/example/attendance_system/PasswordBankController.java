package com.example.attendance_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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

    int professorId = ProfessorManager.getInstance().getProfessorId();
    //int professorId = 1;
    //int professorId = 2;

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
        // Create a new HBox to contain both password text field and class_id combo box
        HBox container = new HBox();

        // Create a TextField for the password
        TextField passwordText = new TextField();

        // Create a ComboBox for selecting class_id
        ComboBox<String> classIdComboBox = new ComboBox<>();
        ComboBoxUtils.populateComboBox(classIdComboBox, professorId);

        // Add TextField and ComboBox to the container
        container.getChildren().addAll(passwordText, classIdComboBox);

        // Add the container to the containers VBox
        containers.getChildren().add(container);
    }

    // Event handler for showing password bank
    @FXML
    private void showPasswordBank() {
        passwords.clear(); // Clear existing passwords

        // Connect to database
        try(Connection connection = DatabaseManagerUtils.getConnection()) {
            // Prepare SQL statement to retrieve passwords
            String sql = "SELECT passwords.password FROM passwords " +
                    "INNER JOIN classes ON passwords.class_id = classes.class_id " +
                    "WHERE classes.professor_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, professorId);

            ResultSet resultSet = statement.executeQuery();

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
        // Iterate through each container added
        for (javafx.scene.Node node : containers.getChildren()) {
            if (node instanceof HBox) {
                HBox container = (HBox) node;
                TextField passwordField = (TextField) container.getChildren().get(0);
                ComboBox<String> classIdComboBox = (ComboBox<String>) container.getChildren().get(1);

                // Get password and class_id from TextField and ComboBox
                String password = passwordField.getText().trim();
                String classId = classIdComboBox.getValue();

                // Insert password and class_id into the database if they are not empty
                if (!password.isEmpty() && classId != null && !classId.isEmpty()) {
                    insertPasswordIntoDatabase(password, classId);
                }
            }
        }
    }

    @FXML
    private void insertPasswordIntoDatabase(String password, String classId) {

        // Connect to database
        try (Connection connection = DatabaseManagerUtils.getConnection()) {
            String sql = "INSERT INTO passwords (password, class_id) VALUES (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, password);
            stmt.setString(2, classId);
            stmt.executeUpdate();

            // Display success alert
            AlertsUtils.showSuccessAlert("Password(s) added successfully!");

            // Close database connection
            DatabaseManagerUtils.closeConnection(connection);

        } catch (SQLException e) {
            // Display fail alert
            AlertsUtils.showErrorAlert("Failed to add password(s).");
            e.printStackTrace();
        }

    }
}
