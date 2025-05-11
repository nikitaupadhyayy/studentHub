package com.example.authenticationframes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserCourseDescriptionController {

    private static final String FILE_PATH = "src/main/resources/data/File_Courses.txt";
    private static final String USER_DIR = "src/main/resources/data/";
    private static final String COURSE_FILE_SUFFIX = "_courses.txt";

    private String[] selectedCourseData = null;
    private String currentUserEmail;
    private static final ObservableList<String[]> enrolledCourses = FXCollections.observableArrayList();

    @FXML private VBox courseListVBox;

    @FXML private Text detailName;
    @FXML private Text detailCode;
    @FXML private Text detailLocation;
    @FXML private Text detailSection;
    @FXML private Text detailCapacity;
    @FXML private Text detailExam;
    @FXML private Text detailTeacher;
    @FXML private Text detailTime;

    @FXML private Button registerCourse;
    @FXML private Button backButton;

    @FXML
    private void initialize() {
        clearCourseDetails();        // Start with an empty detail pane
        loadCourseList();            // Load course list on the left
        registerCourse.setOnAction(event -> registerForCourse());
    }

    public void setCurrentUserEmail(String currentUserEmail) {
        this.currentUserEmail = currentUserEmail;

        if (currentUserEmail != null && !currentUserEmail.isEmpty()) {
            loadUserCourses();
        }
    }

    private void loadCourseList() {
        courseListVBox.getChildren().clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] courseData = line.split("\t");
                if (courseData.length >= 8) {
                    Pane coursePane = createCoursePane(courseData);
                    courseListVBox.getChildren().add(coursePane);
                }
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to load courses: " + e.getMessage());
        }
    }

    private Pane createCoursePane(String[] courseData) {
        Pane pane = new Pane();
        pane.setPrefHeight(50);
        pane.setPrefWidth(1100);
        pane.setStyle("-fx-border-radius: 50px; -fx-background-radius: 50px; -fx-border-style: solid; -fx-background-color: #373737;");

        Rectangle highlight = new Rectangle(1000, 50, Color.TRANSPARENT);
        pane.getChildren().add(highlight);

        Text name = new Text(courseData[0]);
        name.setLayoutX(20);
        name.setLayoutY(30);
        name.setStyle("-fx-fill: white; -fx-font-size: 16; -fx-font-family: 'Dubai';");

        pane.getChildren().add(name);

        pane.setOnMouseClicked(event -> {
            selectedCourseData = courseData;
            updateCourseDetails();
        });

        return pane;
    }

    private void updateCourseDetails() {
        if (selectedCourseData != null) {
            detailName.setText(selectedCourseData[0]);
            detailCode.setText(selectedCourseData[1]);
            detailSection.setText(selectedCourseData[2]);
            detailTime.setText(selectedCourseData[3]);
            detailCapacity.setText(selectedCourseData[4]);
            detailExam.setText("$" + selectedCourseData[5]);
            detailLocation.setText(selectedCourseData[6]);
            detailTeacher.setText(selectedCourseData[7]);

            Font font = new Font(17);
            detailName.setFont(font);
            detailCode.setFont(font);
            detailSection.setFont(font);
            detailTime.setFont(font);
            detailCapacity.setFont(font);
            detailExam.setFont(font);
            detailLocation.setFont(font);
            detailTeacher.setFont(font);
        }
    }

    private void clearCourseDetails() {
        detailName.setText("");
        detailCode.setText("");
        detailSection.setText("");
        detailTime.setText("");
        detailCapacity.setText("");
        detailExam.setText("");
        detailLocation.setText("");
        detailTeacher.setText("");
    }

    private void registerForCourse() {
        if (selectedCourseData == null) {
            showAlert("No Selection", "Please select a course first.");
            return;
        }

        if (currentUserEmail == null || currentUserEmail.isEmpty()) {
            showAlert("Error", "No user logged in.");
            return;
        }

        if (isAlreadyRegistered(selectedCourseData[1])) {
            showAlert("Already Registered", "You are already registered for this course.");
            return;
        }

        enrolledCourses.add(selectedCourseData);

        try {
            updateCourseCapacity();
            saveCourseToUserFile();
            showAlert("Success", "Successfully registered for " + selectedCourseData[0]);
        } catch (IOException e) {
            showAlert("Error", "Failed to register: " + e.getMessage());
        }
    }

    private void saveCourseToUserFile() throws IOException {
        String username = currentUserEmail.split("@")[0];
        String userCourseFileName = USER_DIR + username + COURSE_FILE_SUFFIX;

        Path path = Paths.get(USER_DIR);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userCourseFileName, true))) {
            writer.write(String.join("\t", selectedCourseData));
            writer.newLine();
        }
    }

    private boolean isAlreadyRegistered(String courseCode) {
        boolean inMemory = enrolledCourses.stream()
                .anyMatch(course -> course[1].equals(courseCode));
        if (inMemory) return true;

        if (currentUserEmail != null && !currentUserEmail.isEmpty()) {
            String username = currentUserEmail.split("@")[0];
            String userCourseFileName = USER_DIR + username + COURSE_FILE_SUFFIX;

            try (BufferedReader reader = new BufferedReader(new FileReader(userCourseFileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] courseData = line.split("\t");
                    if (courseData.length > 1 && courseData[1].equals(courseCode)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                // File might not exist yet
            }
        }

        return false;
    }

    private void updateCourseCapacity() throws IOException {
        File inputFile = new File(FILE_PATH);
        File tempFile = new File(FILE_PATH + ".tmp");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] courseData = line.split("\t");
                if (courseData.length >= 8 && courseData[1].equals(selectedCourseData[1])) {
                    String[] capacityParts = courseData[4].split("/");
                    if (capacityParts.length == 2) {
                        try {
                            int current = Integer.parseInt(capacityParts[0]);
                            int max = Integer.parseInt(capacityParts[1]);
                            if (current < max) {
                                current++;
                                courseData[4] = current + "/" + max;
                                selectedCourseData[4] = courseData[4];
                            }
                        } catch (NumberFormatException ignored) {}
                    }
                    line = String.join("\t", courseData);
                }
                writer.write(line);
                writer.newLine();
            }
        }

        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        } else {
            throw new IOException("Could not update course capacity");
        }

        loadCourseList();
    }

    private void loadUserCourses() {
        String username = currentUserEmail.split("@")[0];
        String userCourseFileName = USER_DIR + username + COURSE_FILE_SUFFIX;
        enrolledCourses.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(userCourseFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] courseData = line.split("\t");
                if (courseData.length >= 8) {
                    enrolledCourses.add(courseData);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing course file for user: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserMainMenu.fxml"));
            Parent root = loader.load();

            UserMainMenuController dashboardController = loader.getController();
            dashboardController.setCurrentUserEmail(currentUserEmail);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Failed to navigate back", e);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    public static ObservableList<String[]> getEnrolledCourses() {
        return enrolledCourses;
    }
}
