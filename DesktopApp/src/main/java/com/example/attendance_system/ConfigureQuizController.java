package com.example.attendance_system;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConfigureQuizController {
    @FXML
    private VBox containers;

    @FXML
    private ComboBox<String> courseBox;

    @FXML
    private ComboBox<String> passwordOptions;

    @FXML
    private ComboBox<String> numQuestions;

    @FXML
    private DatePicker quizDate;

    @FXML
    private TableView<ObservableList<String>> quizTableView;

    @FXML
    private TableColumn<ObservableList<String>, String> quizIdColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> dateColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> classIdColumn;

    private ObservableList<ObservableList<String>> quizData = FXCollections.observableArrayList();


    private ObservableList<String> questions;

    private int selectedQuestionsCount; // Counter to keep track of selected questions

    private String amountQuestions;

    private String selectedCourse;

    private int passwordId;

    private Date date;

    private List<Integer> selectedQuestionIds; // List to store selected question IDs

    int professorId = ProfessorManager.getInstance().getProfessorId();
    //int professorId = 1;

    public void initialize() {
        // Initialize TableColumn cell value factories
        quizIdColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0)));
        dateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1)));
        classIdColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2)));

        fetchQuizInfo();
        ComboBoxUtils.populateComboBox(courseBox, professorId);
        //ComboBoxUtils.populatePasswordsComboBox(passwordOptions, selectedCourse);
        questions = FXCollections.observableArrayList();
        selectedQuestionIds = new ArrayList<>();
    }

    private void fetchQuizInfo() {
        try (Connection connection = DatabaseManagerUtils.getConnection()) {
            String sql = "SELECT ca.config_atten_id, ca.date, c.class_id " +
                    "FROM configure_attendance ca " +
                    "JOIN passwords p ON ca.password_id = p.password_ID " +
                    "JOIN classes c ON p.class_id = c.class_id " +
                    "WHERE c.professor_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, professorId);
            ResultSet resultSet = statement.executeQuery();

            // Clear existing data
            quizData.clear();

            System.out.println(resultSet);

            // Add fetched data to quizData
            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(resultSet.getString("config_atten_id"));
                row.add(resultSet.getString("date"));
                row.add(resultSet.getString("class_id"));
                System.out.println("Content " + row);
                quizData.add(row);
            }

            // Populate data into TableView
            quizTableView.setItems(quizData);

            resultSet.close();
            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void showQuestions() {
        questions.clear(); // Clear existing questions
        containers.getChildren().clear(); // Clear existing checkboxes
        selectedQuestionsCount = 0; // Reset selected questions count
        selectedQuestionIds.clear(); // Clear selected question IDs

        // Store the selected course
        selectedCourse = courseBox.getValue();
        System.out.println(selectedCourse);

        // Populate the passwordOptions ComboBox based on the selected class_id
        ComboBoxUtils.populatePasswordsComboBox(passwordOptions, selectedCourse);

        // Connect to database
        try(Connection connection = DatabaseManagerUtils.getConnection()) {
            // Prepare SQL statement to retrieve questions and their IDs
            String sql = "SELECT question_id, question FROM question WHERE class_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, selectedCourse);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int questionId = resultSet.getInt("question_id");
                String question = resultSet.getString("question");
                questions.add(question);
                CheckBox checkBox = new CheckBox(question); // Create checkbox with question as label
                checkBox.setOnAction(event -> handleQuestionSelection(checkBox, questionId)); // Handle checkbox selection
                containers.getChildren().add(checkBox); // Add checkbox to the VBox
            }

            // Close database connection
            DatabaseManagerUtils.closeConnection(connection);

        } catch (Exception e) {
            e.printStackTrace();
            AlertsUtils.showErrorAlert("Unable to display questions.");
        }
    }

    private void handleQuestionSelection(CheckBox checkBox, int questionId) {
        if (amountQuestions == null) {
            // Show error message if amountQuestions is not set
            AlertsUtils.showErrorAlert("Please select the number of questions first.");
            checkBox.setSelected(false);
            return;
        }

        if (checkBox.isSelected()) {
            if (selectedQuestionsCount >= Integer.parseInt(amountQuestions)) {
                checkBox.setSelected(false); // Prevent selecting more questions
            } else {
                selectedQuestionsCount++;
                selectedQuestionIds.add(questionId); // Store selected question ID
            }
        } else {
            selectedQuestionsCount--;
            selectedQuestionIds.remove(Integer.valueOf(questionId)); // Remove deselected question ID
        }
    }

    @FXML
    private void submitQuiz() {
        if (quizDate.getValue() == null || amountQuestions == null || selectedCourse == null || passwordId < 0) {
            // Show error message if any required field is not filled
            AlertsUtils.showErrorAlert("All the required fields have not been chosen");
        }

        try (Connection connection = DatabaseManagerUtils.getConnection()) {
            // Fetch class start time from classes table
            String classSql = "SELECT start_time FROM classes WHERE class_id = ?";
            PreparedStatement classStatement = connection.prepareStatement(classSql);
            classStatement.setString(1, selectedCourse);
            ResultSet classResultSet = classStatement.executeQuery();

            LocalDateTime classStartTime = null;

            if (classResultSet.next()) {
                classStartTime = classResultSet.getTimestamp("start_time").toLocalDateTime();
                System.out.println(classStartTime);
            }

            classResultSet.close();
            classStatement.close();

            if (classStartTime != null) {
                // Prepare SQL statement to insert quiz information into configure_attendance table
                String sql = "INSERT INTO configure_attendance (password_id, question_id_1, question_id_2, question_id_3, start_time, end_time, date) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, passwordId);
                statement.setObject(2, (selectedQuestionIds.size() > 0) ? selectedQuestionIds.get(0) : null);
                statement.setObject(3, (selectedQuestionIds.size() > 1) ? selectedQuestionIds.get(1) : null);
                statement.setObject(4, (selectedQuestionIds.size() > 2) ? selectedQuestionIds.get(2) : null);
                // Start time 10 minutes before class start time
                statement.setTimestamp(5, Timestamp.valueOf(classStartTime.minusMinutes(10)));
                // End time 10 minutes after class start time
                statement.setTimestamp(6, Timestamp.valueOf(classStartTime.plusMinutes(10)));
                statement.setDate(7, Date.valueOf(quizDate.getValue()));

                // Execute the statement
                statement.executeUpdate();

                // Close resources
                statement.close();

                // Show success message
                AlertsUtils.showSuccessAlert("Quiz submitted successfully!");

                fetchQuizInfo();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("SQL Error: " + e.getMessage());
            AlertsUtils.showErrorAlert("Error submitting quiz");
        }

    }

    @FXML
    private void handleDate() {
        // Store the selected course
        date = Date.valueOf(quizDate.getValue());
        System.out.println(date);
    }

    @FXML
    private void handleNumQuestions() {
        // Store the selected course
        amountQuestions = numQuestions.getValue();
        System.out.println(amountQuestions);
    }

    @FXML
    private void handlePasswordSelection() {
        // Store the selected course
        try (Connection connection = DatabaseManagerUtils.getConnection()) {
            // Prepare SQL statement to retrieve password_id based on the selected password
            String sql = "SELECT password_ID FROM passwords WHERE password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, passwordOptions.getValue());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Retrieve the password_id from the result set
                passwordId = resultSet.getInt("password_ID");
                System.out.println(passwordId);
            }
            // Close resources
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle other exceptions
        }
    }

}
