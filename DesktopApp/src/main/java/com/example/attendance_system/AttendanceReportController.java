package com.example.attendance_system;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;

public class AttendanceReportController {

    @FXML
    private Button uploadButton;

    @FXML
    private ComboBox<String> courseBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button generateReportButton;

    @FXML
    private TableView<AttendanceRecord> attendanceTableView;

    @FXML
    private TableColumn<AttendanceRecord, String> utdIdColumn;

    @FXML
    private TableColumn<AttendanceRecord, String> firstNameColumn;

    @FXML
    private TableColumn<AttendanceRecord, String> middleNameColumn;

    @FXML
    private TableColumn<AttendanceRecord, String> lastNameColumn;

    @FXML
    private TableColumn<AttendanceRecord, String> studentStatusColumn;

    @FXML
    private TableColumn<AttendanceRecord, String> response1Column;

    @FXML
    private TableColumn<AttendanceRecord, String> response2Column;

    @FXML
    private TableColumn<AttendanceRecord, String> response3Column;


    @FXML
    private TableColumn<AttendanceRecord, String> classIdColumn;

    int professorId = ProfessorManager.getInstance().getProfessorId();
    // int professorId = 1;

    @FXML
    private void initialize() {
        System.out.println("initialize() method called.");

        // Populate courseBox ComboBox with courses related to the professorId
        ComboBoxUtils.populateComboBox(courseBox, professorId);

        // Initialize columns
        utdIdColumn.setCellValueFactory(new PropertyValueFactory<>("utdId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        middleNameColumn.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        studentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("studentStatus"));
        response1Column.setCellValueFactory(new PropertyValueFactory<>("response1"));
        response2Column.setCellValueFactory(new PropertyValueFactory<>("response2"));
        response3Column.setCellValueFactory(new PropertyValueFactory<>("response3"));
        classIdColumn.setCellValueFactory(new PropertyValueFactory<>("classId"));

        // Action event handler for uploadButton
        uploadButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");
            File file = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
            processFile(file);
        });

        // Action event handler for generateReportButton
        generateReportButton.setOnAction(event -> {
            System.out.println("Generate Report button clicked.");
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate != null) {
                displayAttendanceReport(selectedDate);
            } else {
                AlertsUtils.showErrorAlert("Please select a date.");
            }
        });
    }

    // Process the uploaded file
    private void processFile(File file) {
        // Connect to database
        try (Connection conn = DatabaseManagerUtils.getConnection()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            String format;
            int lineNum = 1;

            // Read and identify file format
            line = reader.readLine();
            System.out.println(line);

            // Determine if the file is in Coursebook or eLearning format
            if (line.contains("Coursebook")) {
                format = "Coursebook";
            } else {
                format = "eLearning";
            }

            System.out.println(format);

            while ((line = reader.readLine()) != null) {

                // Process Coursebook Format
                if (format.equals("Coursebook") && lineNum > 2) {
                    String[] fields = line.split("\\t+");

                    for (String e : fields) {
                        System.out.println(e);
                    }

                    // Insert student data into the database
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
                        // Insert student data into the database
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
        } catch (Exception e) {
            AlertsUtils.showErrorAlert("Failed to process file.");
            e.printStackTrace();
        }
    }

    // Generate and display attendance report
    @FXML
    private void generateReport() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            displayAttendanceReport(selectedDate);
        } else {
            AlertsUtils.showErrorAlert("Please select a date.");
        }
    }

    // Write data to CSV file
    private void writeDataToCSV(String fileName, StringBuilder data) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        writer.write(data.toString());
        writer.close();
    }

    // Display the attendance report for a specific date
    private void displayAttendanceReport(LocalDate date) {
        System.out.println("Displaying attendance report for date: " + date);

        StringBuilder reportData = new StringBuilder(); // StringBuilder to store data

        try (Connection conn = DatabaseManagerUtils.getConnection()) {
            // Prepare SQL statement to retrieve attendance records and associated class_id for the selected date
            String sql = "SELECT a.*, s.first_name, s.middle_name, s.last_name, q.class_id\n" +
                    "FROM attendance a\n" +
                    "INNER JOIN configure_attendance c ON a.config_atten_id = c.config_atten_id\n" +
                    "INNER JOIN question q ON c.question_id_1 = q.question_id\n" +
                    "INNER JOIN student s ON a.UTD_ID = s.UTD_ID\n" +
                    "WHERE a.date_Atten = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setDate(1, Date.valueOf(date));
            ResultSet resultSet = statement.executeQuery();

            // Populate TableView
            while (resultSet.next()) {
                attendanceTableView.getItems().add(new AttendanceRecord(
                        resultSet.getString("UTD_ID"),
                        resultSet.getString("first_name"),
                        resultSet.getString("middle_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("student_status"),
                        resultSet.getString("response1"),
                        resultSet.getString("response2"),
                        resultSet.getString("response3"),
                        resultSet.getString("class_id")
                ));
            }

            // Close resources
            resultSet.close();
            statement.close();

            // Close database connection
            DatabaseManagerUtils.closeConnection(conn);
        } catch (Exception e) {
            AlertsUtils.showErrorAlert("Failed to generate report.");
            e.printStackTrace();
        }

        // Write report data to a CSV file
        try {
            writeDataToCSV("attendance_report.csv", reportData);
        } catch (IOException e) {
            AlertsUtils.showErrorAlert("Failed to write report data to CSV file.");
            e.printStackTrace();
        }
    }
}

