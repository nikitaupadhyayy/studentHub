package com.example.authenticationframes;

import com.example.real.utils.SceneLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Controller for the Admin Event View, allowing admins to view, search, and add events.
 */
public class AdminEventViewController {

    private static final String FILE_PATH = "/data/File_Events.txt"; // Relative to resources folder

    @FXML private VBox eventListContainer;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button addEventButton;
    @FXML private Button backButton;

    /**
     * Initializes the event view, loads events, and sets up button actions.
     */
    @FXML
    private void initialize() {
        System.out.println("AdminEventViewController initialized.");

        // Load existing events
        loadEvents();

        // Attach event listeners
        addEventButton.setOnAction(event -> handleaddEventButton());
        searchButton.setOnAction(event -> searchEvents());
        backButton.setOnAction(event -> handleaddEventButton());
    }

    private void loadEvents() {
        eventListContainer.getChildren().clear();

        InputStream inputStream = getClass().getResourceAsStream(FILE_PATH);
        if (inputStream == null) {
            showAlert("Error", "Event file not found!");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");

                if (parts.length < 7) continue; // Ensure valid event format

                // Extract event details
                String eventCode = parts[0];
                String eventName = parts[1];
                String location = parts[2];
                String dateTime = parts[3];
                String capacity = parts[4];
                String description = parts[5];
                String enrollment = parts[6];

                // Create event pane
                Pane eventPane = createEventPane(eventCode, eventName, location, dateTime, capacity, description, enrollment);
                eventListContainer.getChildren().add(eventPane);
            }

            System.out.println("Events successfully loaded.");
        } catch (IOException e) {
            System.err.println("Error loading events: " + e.getMessage());
            showAlert("Error", "Failed to load events: " + e.getMessage());
        }
    }

    /**
     * Creates a formatted event display pane.
     */
    private Pane createEventPane(String eventCode, String eventName, String location, String dateTime, String capacity, String description, String enrollment) {
        Pane pane = new Pane();
        pane.setPrefHeight(44);
        pane.setPrefWidth(1109);
        pane.setStyle("-fx-border-radius: 50px; -fx-background-radius: 50px; -fx-border-style: solid; -fx-background-color: #373737;");

        // Add event info as Text fields
        Text codeText = new Text(eventCode);
        codeText.setLayoutX(10);
        codeText.setLayoutY(28);
        codeText.setStyle("-fx-fill: white; -fx-font-size: 16;");

        Text nameText = new Text(eventName);
        nameText.setLayoutX(100);
        nameText.setLayoutY(28);
        nameText.setStyle("-fx-fill: white; -fx-font-size: 16;");

        Text locationText = new Text(location);
        locationText.setLayoutX(300);
        locationText.setLayoutY(28);
        locationText.setStyle("-fx-fill: white; -fx-font-size: 16;");

        Text dateTimeText = new Text(dateTime);
        dateTimeText.setLayoutX(500);
        dateTimeText.setLayoutY(28);
        dateTimeText.setStyle("-fx-fill: white; -fx-font-size: 16;");

        Text capacityText = new Text("Capacity: " + enrollment + "/" + capacity);
        capacityText.setLayoutX(700);
        capacityText.setLayoutY(28);
        capacityText.setStyle("-fx-fill: white; -fx-font-size: 16;");

        Text descriptionText = new Text(description);
        descriptionText.setLayoutX(900);
        descriptionText.setLayoutY(28);
        descriptionText.setStyle("-fx-fill: white; -fx-font-size: 16;");

        pane.getChildren().addAll(codeText, nameText, locationText, dateTimeText, capacityText, descriptionText);
        return pane;
    }

    /**
     * Opens the "Add Event" screen.
     */
    @FXML
    private void handleaddEventButton() {
        try {
            System.out.println("Opening Add Event screen...");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/real/adminAddEvent.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) addEventButton.getScene().getWindow();
            
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error opening Add Event screen:");
            e.printStackTrace();
            showAlert("Error", "Failed to open Add Event screen: " + e.getMessage());
        }
    }

    /**
     * Searches for events based on user input.
     */
    @FXML
    private void searchEvents() {
        String query = searchField.getText().trim().toLowerCase();
        eventListContainer.getChildren().clear();

        InputStream inputStream = getClass().getResourceAsStream(FILE_PATH);
        if (inputStream == null) {
            showAlert("Error", "Event file not found!");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length < 7) continue;

                String eventCode = parts[0];
                String eventName = parts[1];

                if (eventCode.toLowerCase().contains(query) || eventName.toLowerCase().contains(query)) {
                    Pane eventPane = createEventPane(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
                    eventListContainer.getChildren().add(eventPane);
                }
            }
        } catch (IOException e) {
            System.err.println("Error searching events: " + e.getMessage());
            showAlert("Error", "Failed to search events: " + e.getMessage());
        }
    }

    /**
     * Navigates back to the Admin Main Menu.
     */
    @FXML
    private void openAddEventButton() {
        System.out.println("Navigating back to Admin Main Menu...");
        SceneLoader.loadScene("adminMainMenu.fxml", "Admin Main Menu", backButton);
    }

    /**
     * Displays an alert with the given title and message.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}