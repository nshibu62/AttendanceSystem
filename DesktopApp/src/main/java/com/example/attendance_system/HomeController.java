package com.example.attendance_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomeController {
    @FXML
    private ComboBox<String> professorComboBox;

    @FXML
    private TextField professorNameField;

    @FXML
    private TextField professorIdField;

    @FXML
    private Text actiontarget;

    @FXML
    private Label professorNameLabel;

    @FXML
    private Label professorIdLabel;

    @FXML
    private Button submitButton;

    public void initialize() {
        ComboBoxUtils.populateProfessorsComboBox(professorComboBox);
    }

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        String selectedProfessor = professorComboBox.getValue();
        if (selectedProfessor != null && !selectedProfessor.isEmpty()) {
            //actiontarget.setText("Sign in button pressed for Professor: " + selectedProfessor);
            // Implement your logic for signing in here
            try(Connection connection = DatabaseManagerUtils.getConnection()) {
                // Prepare a statement to retrieve the ID
                String sql = "SELECT professor_id FROM professors WHERE name = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, selectedProfessor);

                ResultSet resultSet = statement.executeQuery();

                // Check if a result is found
                if (resultSet.next()) {
                    int professorId = resultSet.getInt("professor_id");
                    ProfessorManager.getInstance().setProfessorId(professorId);
                    System.out.println(ProfessorManager.getInstance().getProfessorId());
                    System.out.println("Professor ID: " + professorId);
                } else {
                    actiontarget.setText("Professor not found.");
                }

                statement.close();
                connection.close();

                actiontarget.setText("Sign in button pressed");
                loadMainScreen(event);
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle exceptions appropriately
            }
        } else {
            actiontarget.setText("Please select a professor.");
        }
    }

    @FXML
    protected void handleAddProfessorButtonAction() {
        boolean isVisible = professorNameField.isVisible();
        if (!isVisible) {
            toggleProfessorFieldsVisibility();
            //actiontarget.setText("Fields enabled for adding professor.");
            submitButton.setVisible(true); // Make the submit button visible
        } else {
            actiontarget.setText("Please click 'Submit' to add the professor.");
        }
    }

    @FXML
    protected void handleSubmitProfessorButtonAction() {
        String newProfessorName = professorNameField.getText();
        String newProfessorIdString = professorIdField.getText();

        if (!newProfessorName.isEmpty() && !newProfessorIdString.isEmpty()) {
            try {
                int newProfessorId = Integer.parseInt(newProfessorIdString);
                try (Connection connection = DatabaseManagerUtils.getConnection()) {
                    String sql = "INSERT INTO professors (professor_id, name) VALUES (?, ?)";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setInt(1, newProfessorId);
                    statement.setString(2, newProfessorName);
                    statement.executeUpdate();

                    statement.close();
                    connection.close();

                    // Clear fields after successful insertion
                    professorNameField.clear();
                    professorIdField.clear();
                    actiontarget.setText("Professor added successfully.");

                    // Toggle fields back to invisible
                    toggleProfessorFieldsVisibility();
                    submitButton.setVisible(false);
                    ComboBoxUtils.populateProfessorsComboBox(professorComboBox);
                }
            } catch (NumberFormatException e) {
                actiontarget.setText("Error: Professor ID must be an integer.");
            } catch (SQLException e) {
                e.printStackTrace();
                actiontarget.setText("Error: Failed to add professor.");
            }
        } else {
            actiontarget.setText("Please enter both name and ID.");
        }
    }

    @FXML
    protected void toggleProfessorFieldsVisibility() {
        boolean isVisible = professorNameField.isVisible();
        professorNameField.setVisible(!isVisible);
        professorIdField.setVisible(!isVisible);
        professorNameLabel.setVisible(!isVisible);
        professorIdLabel.setVisible(!isVisible);
    }

    private void loadMainScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
            Parent mainScreenParent = loader.load();
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
            window.setScene(mainScreenScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
