package com.example.authenticationframes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserEnrolledClassesController {

    @FXML private VBox enrolledCoursesVBox;
    @FXML private Button backButton;
    private String currentUserEmail;

    private static final String DATA_DIR = Paths.get("src", "main", "resources", "data").toString();
    private static final String COURSE_FILE_SUFFIX = "_courses.txt";

    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
        loadEnrolledCourses();
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
            System.err.println("Error loading dashboard: " + e.getMessage());
            showErrorAlert("Failed to navigate back", e);
        }
    }

    private void loadEnrolledCourses() {
        enrolledCoursesVBox.getChildren().clear();

        if (currentUserEmail == null || currentUserEmail.isEmpty()) {
            showError("No user logged in.");
            return;
        }

        String username = currentUserEmail.split("@")[0];
        Path courseFilePath = Paths.get(DATA_DIR, username + COURSE_FILE_SUFFIX);
        File courseFile = courseFilePath.toFile();

        if (!courseFile.exists()) {
            addNoCoursesMessage();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(courseFile))) {
            String line;
            boolean hasCourses = false;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Pane coursePane = createCoursePane(line);
                    if (coursePane != null) {
                        enrolledCoursesVBox.getChildren().add(coursePane);
                        hasCourses = true;
                    }
                }
            }

            if (!hasCourses) {
                addNoCoursesMessage();
            }

        } catch (IOException e) {
            System.err.println("[ERROR] Failed to read courses: " + e.getMessage());
            showError("Failed to load your courses");
        }
    }

    private Pane createCoursePane(String courseLine) {
        String[] courseData = courseLine.split("\t");

        if (courseData.length < 8) {
            System.err.println("[WARN] Invalid course format: " + courseLine);
            return null;
        }

        Pane pane = new Pane();
        pane.setStyle("-fx-border-radius: 50px; -fx-background-radius: 50px; -fx-background-color: #373737;");
        pane.setPrefHeight(44.0);
        pane.setPrefWidth(1137.0);

        Text courseText = new Text(courseData[0] + " - " + courseData[1]); // Title + Code
        courseText.setFill(javafx.scene.paint.Color.WHITE);
        courseText.setLayoutX(24.0);
        courseText.setLayoutY(30.0);
        courseText.setFont(new Font(20.0));

        Text locationText = new Text("Room: " + courseData[2]);
        locationText.setFill(javafx.scene.paint.Color.WHITE);
        locationText.setLayoutX(340.0);
        locationText.setLayoutY(30.0);
        locationText.setFont(new Font(16.0));

        Text timeText = new Text("Time: " + courseData[3]);
        timeText.setFill(javafx.scene.paint.Color.WHITE);
        timeText.setLayoutX(510.0);
        timeText.setLayoutY(30.0);
        timeText.setFont(new Font(16.0));

        Text teacherText = new Text("Teacher: " + courseData[7]);
        teacherText.setFill(javafx.scene.paint.Color.WHITE);
        teacherText.setLayoutX(700.0);
        teacherText.setLayoutY(30.0);
        teacherText.setFont(new Font(16.0));

        pane.getChildren().addAll(courseText, locationText, timeText, teacherText);
        return pane;
    }

    private void addNoCoursesMessage() {
        Text message = new Text("You haven't enrolled in any courses yet.");
        message.setStyle("-fx-fill: white; -fx-font-size: 16;");
        enrolledCoursesVBox.getChildren().add(message);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
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
}
