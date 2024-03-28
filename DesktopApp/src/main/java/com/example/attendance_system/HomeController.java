package com.example.attendance_system;

import com.almasb.fxgl.entity.action.Action;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    @FXML private Text actiontarget;

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        actiontarget.setText("Sign in button pressed");
        loadMainScreen(event);
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