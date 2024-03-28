package com.example.attendance_system;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.*;

public class PasswordBankController {
    @FXML
    private VBox containers;

    @FXML
    private void addContainer() {
        TextField passwordText = new TextField();
        containers.getChildren().add(passwordText);
    }

    @FXML
    private void showPasswordBank() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendanceDB", "root", "Icecream123");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from passwords");

            while (resultSet.next()) {
                System.out.println(resultSet.getString("password"));
            }

        }
        catch(Exception e) {
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
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendanceDB", "root", "Icecream123");

            String sql = "INSERT INTO passwords (password) VALUES (?)";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, password);
            stmt.executeUpdate();

            System.out.println("Password added to database successfully.");

            // Show success alert
            Alert successAlert = new Alert(AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Password added to database successfully.");
            successAlert.showAndWait();

        } catch (SQLException e) {
            System.out.println("Error inserting password into database: " + e.getMessage());

            // Show error alert
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Error inserting password into database: " + e.getMessage());
            errorAlert.showAndWait();
        }

    }

}
