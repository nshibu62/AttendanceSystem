package com.example.attendance_system;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;

public class AttendanceReportController {
    @FXML
    private Button uploadButton;

    @FXML
    private void initialize() {
        uploadButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");
            File file = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
            processFile(file);
        });
    }

    private void processFile(File file) {
        try {

            // Connect to MySQL database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "password");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            String format;
            int lineNum = 1;

            // First line in file
            line = reader.readLine();

            // File in Coursebook format
            if (line.contains("Coursebook")) {
                format = "Coursebook";
            }
            else {
                format = "eLearning";
            }

            System.out.println(format);

            while ((line = reader.readLine()) != null) {

                //Process Coursebook Format
                if (format.equals("Coursebook") && lineNum > 2) {
                    String[] fields = line.split("\\t+");

                    for (String e: fields) {
                        System.out.println(e);
                    }

                    PreparedStatement statement = conn.prepareStatement("INSERT INTO student (UTD_ID, first_name, middle_name, last_name) VALUES (?, ?, ?, ?)");
                    statement.setString(1, fields[1]);
                    statement.setString(2, fields[2]);
                    statement.setString(3, fields[3]);
                    statement.setString(4, fields[4]);
                    statement.executeUpdate();
                }


                // Process eLearning Format
                if (format.equals("eLearning") && lineNum > 0) {
                    String[] fields = line.split("\\t+");

                    for (int i = 0; i < fields.length; i++) {
                        // Remove the outside quotes from each element
                        fields[i] = fields[i].replaceAll("\"", "");
                    }


                    if (fields.length > 1) {
                        PreparedStatement statement = conn.prepareStatement("INSERT INTO student (UTD_ID, first_name, last_name) VALUES (?, ?, ?)");
                        statement.setString(1, fields[3]);
                        statement.setString(2, fields[1]);
                        statement.setString(3, fields[0]);
                        statement.executeUpdate();
                    }
                }

                lineNum++;
            }

            // Close resources
            reader.close();
            conn.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
