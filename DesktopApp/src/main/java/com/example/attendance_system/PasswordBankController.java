package com.example.attendance_system;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class PasswordBankController {
    @FXML
    private VBox containers;

    @FXML
    private void addContainer() {
        TextField passwordText = new TextField();
        containers.getChildren().add(passwordText);
    }
}
