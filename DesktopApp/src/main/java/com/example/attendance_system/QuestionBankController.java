package com.example.attendance_system;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class QuestionBankController {
    @FXML
    private VBox containers;

    @FXML
    private void addContainer() {
        HBox questionContainer = new HBox();
        HBox AContainer = new HBox();
        HBox BContainer = new HBox();
        HBox CContainer = new HBox();
        HBox DContainer = new HBox();

        Label question = new Label("Question");
        TextField questionText = new TextField();
        Label optionA = new Label("Option A");
        TextField answerA = new TextField();
        Label optionB = new Label("Option B");
        TextField answerB = new TextField();
        Label optionC = new Label("Option C");
        TextField answerC = new TextField();
        Label optionD = new Label("Option D");
        TextField answerD = new TextField();

        ComboBox<String> answerBox = new ComboBox<>();

        answerBox.getItems().addAll("A","B","C","D");
        answerBox.setValue("Answer");

        questionContainer.getChildren().addAll(question, questionText);
        AContainer.getChildren().addAll(optionA, answerA);
        BContainer.getChildren().addAll(optionB, answerB);
        CContainer.getChildren().addAll(optionC, answerC);
        DContainer.getChildren().addAll(optionD, answerD);

        containers.getChildren().addAll(questionContainer, AContainer, BContainer, CContainer, DContainer, answerBox);
    }
}
