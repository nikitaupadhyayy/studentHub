package com.example.authenticationframes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.*;

/**
 * Controller for adding events in the Admin panel.
 */
public class AdminAddEventController {

    private static final String FILE_PATH = "C:\\Users\\nikiu\\Downloads\\REAL\\REAL\\src\\main\\resources\\data\\File_Events.txt"; // Ensure correct path

    @FXML private TextField eventCodeField;
    @FXML private TextField eventNameField;
    @FXML private TextField locationField;
    @FXML private TextField dateTimeField;
    @FXML private TextField capacityField;
    @FXML private TextField descriptionField;
    @FXML private TextField enrollmentField;
    @FXML private Button addEventButton;
    @FXML private Button backButton;
    @FXML private Button cancelButton;

    /**
     * Initializes the Add Event screen with button actions.
     */
    @FXML
    private void initialize() {
        System.out.println("AdminAddEventController initialized!");

        addEventButton.setOnAction(event -> addEvent());
        cancelButton.setOnAction(event -> closeWindow());
    }

    /**
     * Validates input, adds a new event to the file, and refreshes the event list.
     */
    private void addEvent() {
        // Retrieve user input
        String eventCode = eventCodeField.getText().trim();
        String eventName = eventNameField.getText().trim();
        String location = locationField.getText().trim();
        String dateTime = dateTimeField.getText().trim();
        String capacity = capacityField.getText().trim();
        String description = descriptionField.getText().trim();
        String enrollment = enrollmentField.getText().trim();

        // Validate input fields
        if (eventCode.isEmpty() || eventName.isEmpty() || location.isEmpty() ||
                dateTime.isEmpty() || capacity.isEmpty() || description.isEmpty() ||
                enrollment.isEmpty()) {
            showAlert("Error", "All fields must be filled!");
            return;
        }

        // Ensure Capacity and Enrollment are valid numbers
        if (!isNumeric(capacity) || !isNumeric(enrollment)) {
            showAlert("Error", "Capacity and Enrollment must be valid numbers!");
            return;
        }

        // Check if the file path exists
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            showAlert("Error", "Event file not found! Check the file path.");
            return;
        }

        // Format event data
        String newEventEntry = eventCode + "\t" + eventName + "\t" + location + "\t" +
                dateTime + "\t" + capacity + "\t" + description + "\t" + enrollment;

        // Confirm before saving
        boolean confirmed = confirmAction("Confirm Save", "Are you sure you want to save this event?\n\n" + newEventEntry);
        if (!confirmed) {
            return; // User canceled
        }

        // Save event to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(newEventEntry);
            writer.newLine();
            System.out.println("Event added successfully: " + newEventEntry);
            showAlert("Success", "Event added successfully!");

        } catch (IOException e) {
            showAlert("Error", "Failed to save event: " + e.getMessage());
            return;
        }

        // Refresh event view
        refreshEventView();

        // Close the window
        closeWindow();
    }

    /**
     * Refreshes the Admin Event View so new events appear immediately.
     */
    private void refreshEventView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("com/example/real/adminEventView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) addEventButton.getScene().getWindow(); // Get current window
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to reload event view: " + e.getMessage());
        }
    }

    /**
     * Checks if a string contains only numbers.
     */
    private boolean isNumeric(String str) {
        return str.matches("\\d+"); // Only digits allowed
    }

    /**
     * Displays an alert box with a given title and message.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a confirmation dialog before saving.
     */
    private boolean confirmAction(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().isPresent(); // Returns true if user confirms
    }

    @FXML
    private void handleBackButton() {
        try {
            // Load the AdminEventView.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminEventView.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) backButton.getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any errors (e.g., show an alert)
        }
    }
    /**
     * Closes the current Add Event window.
     */
    private void closeWindow() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }
}
