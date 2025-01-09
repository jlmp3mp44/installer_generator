package org.example.Controllers;

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
import org.example.validation.MinLengthHandler;
import org.example.validation.NotEmptyHandler;
import org.example.validation.UsernameFormatHandler;
import org.example.validation.ValidationHandler;

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
    Session.initializeState(false);
  }


  @FXML
  private void handleLogin(javafx.event.ActionEvent event) {
    String username = usernameField.getText();
    String password = passwordField.getText();

    try {
      // Send login request to the server
      String response = client.sendRequest("LOGIN " + username + " " + password);
      if (response.equals("Login successful - Premium User")) {
        //Session.initializeState(true);
        int userId = extractUserId(response); // Метод для вилучення userId із відповіді
        Session.initializeState(userId, true);
        showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");
        navigateToWelcome(event);
      }
      else if(response.equals("Login successful - Regular User")){
        int userId = extractUserId(response); // Метод для вилучення userId із відповіді
        Session.initializeState(userId, false);
        showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");
        navigateToWelcome(event);
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
      // Send register request to the server
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
  private boolean validateLicense(ActionEvent event) {
    String enteredLicenseKey = licenseKeyField.getText();
    boolean isLicenseValid = false;
    if (enteredLicenseKey != null && !enteredLicenseKey.trim().isEmpty()) {
      String licenseResponse = client.sendRequest("VALIDATE_LICENSE " + enteredLicenseKey);
      if ("VALID_LICENSE".equals(licenseResponse)) {
        isLicenseValid = true;
        statusLabel.setText("License key is valid! Premium features unlocked.");
        //enablePremiumFeatures();
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
      stage.setScene(new Scene(root));
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
      showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load welcome page.");
    }
  }

  private int extractUserId(String response) {
    // Логіка вилучення userId з відповіді сервера
    // Наприклад, якщо сервер повертає щось типу "Login successful - UserId:123"
    String[] parts = response.split(":");
    return Integer.parseInt(parts[1].trim());
  }

}
