package com.example.attendance_system;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Utility class for displaying alerts.
 */
public class AlertsUtils {

    /**
     * Displays a success alert with the specified message.
     * @param message The message to be displayed in the alert.
     */
    public static void showSuccessAlert(String message) {
        showAlert(AlertType.INFORMATION, "Success", message);
    }

    /**
     * Displays an error alert with the specified message.
     * @param message The message to be displayed in the alert.
     */
    public static void showErrorAlert(String message) {
        showAlert(AlertType.ERROR, "Error", message);
    }

    /**
     * Internal method to display an alert with the specified type, title, and message.
     * @param alertType The type of alert (e.g., INFORMATION, ERROR).
     * @param title The title of the alert window.
     * @param message The message to be displayed in the alert.
     */
    private static void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
