package com.example.attendance_system;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AttendanceReportController {
    @FXML
    private Button uploadButton;

    @FXML
    private ComboBox<String> courseBox;

    /** Set value of professorId for testing purposes
     *  but we need to find a way to get the professorId
     *  when professor accesses desktop app
     */
    int professorId = 1;
    //int professorId = 2;

    @FXML
    private void initialize() {
        ComboBoxUtils.populateComboBox(courseBox, professorId);

        uploadButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");
            File file = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
            processFile(file);
        });
    }

    private void processFile(File file) {
        // Connect to database
        try(Connection conn = DatabaseManagerUtils.getConnection()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            String format;
            int lineNum = 1;

            // First line in file
            line = reader.readLine();
            System.out.println(line);

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

                    PreparedStatement statement = conn.prepareStatement("INSERT INTO student (class_id, UTD_ID, first_name, middle_name, last_name) VALUES (?, ?, ?, ?, ?)");
                    statement.setString(1, fields[6]);
                    statement.setString(2, fields[1]);
                    statement.setString(3, fields[2]);
                    statement.setString(4, fields[3]);
                    statement.setString(5, fields[4]);
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

            // Close database connection
            DatabaseManagerUtils.closeConnection(conn);
        }
        catch(Exception e) {
            AlertsUtils.showErrorAlert("Failed to process file.");
            e.printStackTrace();
        }
    }
}
