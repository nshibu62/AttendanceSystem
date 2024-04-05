package com.example.attendance_system;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Time;

/**
 * Controller class for adding a class.
 */
public class AddClassController {
    @FXML
    private TextField classNameId;

    @FXML
    private TextField classId;

    @FXML
    private TextField startTimeId;

    @FXML
    private TextField endTimeId;

    @FXML
    private TextField startDateId;

    @FXML
    private TextField endDateId;

    int professorId = 1;
    //int professorId = 2;

    /**
     * Handles adding a class.
     */
    @FXML
    private void addClass() {
        // Connect to database
        try(Connection connection = DatabaseManagerUtils.getConnection()) {
            // Prepare insert statement into class table
            String insert = "INSERT INTO classes (class_id, class_name, start_time, end_time, start_date, end_date, professor_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insert);

            // Set values from text fields to the prepared statement
            preparedStatement.setString(1, classId.getText());
            preparedStatement.setString(2, classNameId.getText());
            preparedStatement.setTime(3, Time.valueOf(startTimeId.getText()));
            preparedStatement.setTime(4, Time.valueOf(endTimeId.getText()));
            preparedStatement.setDate(5, Date.valueOf(startDateId.getText()));
            preparedStatement.setDate(6, Date.valueOf(endDateId.getText()));

            /*
             * Set professor_id to 1 for testing purposes but
             * ideally we should retrieve professor_id from user
             * who's using desktop app
             */
            preparedStatement.setInt(7, professorId);

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();

            // Check if class was added successfully
            if (rowsAffected > 0) {
                AlertsUtils.showSuccessAlert("Class added successfully!");
            } else {
                AlertsUtils.showErrorAlert("Failed to add class1.");
                System.out.println("error");
            }


            // Close resources
            preparedStatement.close();

            // Close database connection
            DatabaseManagerUtils.closeConnection(connection);

        } catch (Exception e) {
            System.err.println("SQL Error: " + e.getMessage());
            AlertsUtils.showErrorAlert("Failed to add class2.");
            e.printStackTrace();
        }
    }
}

