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
    private VBox containers; // Container for checkboxes

    @FXML
    private ComboBox<String> courseBox; // ComboBox for selecting courses

    @FXML
    private ComboBox<String> passwordOptions; // ComboBox for selecting passwords

    @FXML
    private ComboBox<String> numQuestions; // ComboBox for selecting number of questions

    @FXML
    private DatePicker quizDate; // DatePicker for selecting quiz date

    @FXML
    private TableView<ObservableList<String>> quizTableView; // TableView for displaying quizzes

    @FXML
    private TableColumn<ObservableList<String>, String> quizIdColumn; // TableColumn for quiz ID

    @FXML
    private TableColumn<ObservableList<String>, String> dateColumn; // TableColumn for quiz date

    @FXML
    private TableColumn<ObservableList<String>, String> classIdColumn; // TableColumn for class ID

    private ObservableList<ObservableList<String>> quizData = FXCollections.observableArrayList(); // Data for quiz TableView

    private ObservableList<String> questions; // List of questions

    private int selectedQuestionsCount; // Counter to keep track of selected questions

    private String amountQuestions; // Number of questions to be selected

    private String selectedCourse; // Selected course

    private int passwordId; // Selected password ID

    private Date date; // Selected date

    private List<Integer> selectedQuestionIds; // List to store selected question IDs

    int professorId = 1; // ID of the professor

    public void initialize() {
        // Initialize TableColumn cell value factories
        quizIdColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0)));
        dateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1)));
        classIdColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2)));

        fetchQuizInfo(); // Fetch quiz information from the database
        ComboBoxUtils.populateComboBox(courseBox, professorId); // Populate course ComboBox
        questions = FXCollections.observableArrayList(); // Initialize questions list
        selectedQuestionIds = new ArrayList<>(); // Initialize selected question IDs list
    }

    // Fetch quiz information from the database
    private void fetchQuizInfo() {
        try (Connection connection = DatabaseManagerUtils.getConnection()) {
            // SQL query to fetch quiz information
            String sql = "SELECT ca.config_atten_id, ca.date, c.class_id " +
                    "FROM configure_attendance ca " +
                    "JOIN passwords p ON ca.password_id = p.password_ID " +
                    "JOIN classes c ON p.class_id = c.class_id " +
                    "WHERE c.professor_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, professorId); // Set professor ID parameter
            ResultSet resultSet = statement.executeQuery(); // Execute query

            // Clear existing data
            quizData.clear();

            // Iterate through the result set and add data to quizData
            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(resultSet.getString("config_atten_id"));
                row.add(resultSet.getString("date"));
                row.add(resultSet.getString("class_id"));
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

    // Method to display questions based on selected course
    @FXML
    public void showQuestions() {
        questions.clear(); // Clear existing questions
        containers.getChildren().clear(); // Clear existing checkboxes
        selectedQuestionsCount = 0; // Reset selected questions count
        selectedQuestionIds.clear(); // Clear selected question IDs

        // Store the selected course
        selectedCourse = courseBox.getValue();

        // Populate the passwordOptions ComboBox based on the selected class_id
        ComboBoxUtils.populatePasswordsComboBox(passwordOptions, selectedCourse);

        // Connect to database
        try(Connection connection = DatabaseManagerUtils.getConnection()) {
            // Prepare SQL statement to retrieve questions and their IDs
            String sql = "SELECT question_id, question FROM question WHERE class_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, selectedCourse);

            ResultSet resultSet = statement.executeQuery();

            // Iterate through the result set and populate checkboxes with questions
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

    // Method to handle selection of questions
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

    // Method to submit a quiz
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
            }

            classResultSet.close();
            classStatement.close();

            if (classStartTime != null) {
                // Prepare SQL statement to insert quiz information into configure_attendance table
                String sql = "INSERT INTO configure_attendance (password_id, question_id_1, question_id_2, question_id_3, start_time, end_time, date) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1
