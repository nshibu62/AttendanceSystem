package com.example.attendance_system;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class ConfigureQuizController {
    @FXML
    private ComboBox<String> courseBox;

    /** Set value of professorId for testing purposes
     *  but we need to find a way to get the professorId
     *  when professor accesses desktop app
     */
    int professorId = 1;

    public void initialize() {
        ComboBoxUtils.populateComboBox(courseBox, professorId);
    }

}
