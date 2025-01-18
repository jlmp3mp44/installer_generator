package org.example.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.entities.User;
import org.example.server.Client;
import org.example.state.Session;

public class AuthController {

  @FXML
  private TextField usernameField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private Button loginButton;

  @FXML
  private Button registerButton;

  @FXML
  private TextField licenseKeyField;

  @FXML
  private Label statusLabel;

  private final Client client;

  public AuthController() {
    client = new Client();
  }

  @FXML
  private void handleLogin(javafx.event.ActionEvent event) {
    String username = usernameField.getText();
    String password = passwordField.getText();

    try {
      String response = client.sendRequest("LOGIN " + username + " " + password);
      if (response.contains("Premium User")) {
        int userId = extractUserId(response);
        Session.initializeState(userId, true);
        showAlert(Alert.AlertType.INFORMATION, "Login Successful, Premium user", "Welcome, " + username + "!");
        navigateToWelcome(event);
      }
      else if(response.contains("Regular User")){
        int userId = extractUserId(response);
        Session.initializeState(userId, false);
        showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");
        navigateToWelcome(event);
      }
      else if (response.equals("User does not exist.")) {
        showAlert(Alert.AlertType.ERROR, "Login Failed", "The username you entered does not exist. Please register.");
      } else if (response.equals("Invalid password.")) {
        showAlert(Alert.AlertType.ERROR, "Login Failed", "The password you entered is incorrect. Please try again.");
      }
      else {
        showAlert(Alert.AlertType.ERROR, "Login Failed", response);
      }
    } catch (IllegalArgumentException e) {
      showAlert(Alert.AlertType.ERROR, "Validation Error", e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while processing your request.");
    }
  }

  @FXML
  private void handleRegister(ActionEvent event) {
    String username = usernameField.getText();
    String password = passwordField.getText();

    User user =  new User(username, password);
    try {
     user.validate();

      boolean isPremium = validateLicense(event);
      user.setLicense(true);
      String response = client.sendRequest("REGISTER " + username + " " + password + " " + isPremium);
      if (response.equals("User registered successfully")) {
        showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "You can now log in.");
      } else {
        showAlert(Alert.AlertType.ERROR, "Registration Failed", response);
      }
    } catch (IllegalArgumentException e) {
      showAlert(Alert.AlertType.ERROR, "Validation Error", e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while processing your request.");
    }
  }

  @FXML
  private void handleExit(ActionEvent event) {
    Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
    currentStage.close();
  }

  @FXML
  private boolean validateLicense(ActionEvent event) {
    String enteredLicenseKey = licenseKeyField.getText();
    boolean isLicenseValid = false;
    if (enteredLicenseKey != null && !enteredLicenseKey.trim().isEmpty()) {
      String licenseResponse = client.sendRequest("VALIDATE_LICENSE " + enteredLicenseKey);
      if ("VALID_LICENSE".equals(licenseResponse)) {
        isLicenseValid = true;
        statusLabel.setText("License key is valid! Premium features unlocked.");
        Session.initializeState(true);
      } else if ("INVALID_LICENSE".equals(licenseResponse)) {
        statusLabel.setText("Invalid license key. Registering without premium features.");
      } else {
        statusLabel.setText("Error validating license: " + licenseResponse);
      }
    }
    return isLicenseValid;
  }

  private void showAlert(Alert.AlertType alertType, String title, String content) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }

  private void navigateToWelcome(javafx.event.ActionEvent event) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/application/welcome.fxml"));
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      stage.setScene(new Scene(root, 800,800));
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
      showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load welcome page.");
    }
  }

  private int extractUserId(String response) {
    String[] parts = response.split("userId:");
    if (parts.length < 2) {
      throw new IllegalArgumentException("Invalid response format");
    }
    return Integer.parseInt(parts[1].trim());
  }
}
