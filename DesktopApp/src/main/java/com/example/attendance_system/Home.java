package com.example.attendance_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Home extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("home-view.fxml"));

        Scene scene = new Scene(root, 300, 275);

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendanceDB", "root", "password");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from student");

            while (resultSet.next()) {
                System.out.println(resultSet.getString("UTD_ID"));
            }

            launch();

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}