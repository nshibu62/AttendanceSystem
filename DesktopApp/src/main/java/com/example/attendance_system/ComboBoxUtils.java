package com.example.attendance_system;

import javafx.scene.control.ComboBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ComboBoxUtils {
    public static void populateComboBox(ComboBox<String> comboBox, int professorId) {
        // Connect to the database and populate the ComboBox based on professorId
        try (Connection connection = DatabaseManagerUtils.getConnection()) {
            String query = "SELECT class_id FROM classes WHERE professor_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, professorId);
            ResultSet resultSet = preparedStatement.executeQuery();

            comboBox.getItems().clear(); // Clear existing items
            while (resultSet.next()) {
                comboBox.getItems().add(resultSet.getString("class_id"));
            }

        } catch (SQLException e) {
            // Handle database errors
            System.err.println("SQL Error: " + e.getMessage());
            AlertsUtils.showErrorAlert("unable to connect to database");
            e.printStackTrace();
        }
    }

    public static void populatePasswordsComboBox(ComboBox<String> comboBox, String classSelected) {
        // Connect to the database and populate the ComboBox based on classID
        try (Connection connection = DatabaseManagerUtils.getConnection()) {
            String query = "SELECT password FROM passwords WHERE class_id = ? ";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, classSelected);
            ResultSet resultSet = preparedStatement.executeQuery();

            comboBox.getItems().clear(); // Clear existing items
            while (resultSet.next()) {
                comboBox.getItems().add(resultSet.getString("password"));
            }

        } catch (SQLException e) {
            // Handle database errors
            System.err.println("SQL Error: " + e.getMessage());
            AlertsUtils.showErrorAlert("unable to connect to database");
            e.printStackTrace();
        }
    }
}

    public static void populateProfessorsComboBox(ComboBox<String> comboBox) {
        // Connect to the database and populate the ComboBox based on professorId
        try (Connection connection = DatabaseManagerUtils.getConnection()) {
            String query = "SELECT name FROM professors";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            comboBox.getItems().clear(); // Clear existing items
            while (resultSet.next()) {
                comboBox.getItems().add(resultSet.getString("name"));
            }

        } catch (SQLException e) {
            // Handle database errors
            System.err.println("SQL Error: " + e.getMessage());
            AlertsUtils.showErrorAlert("unable to connect to database");
            e.printStackTrace();
        }
    }
