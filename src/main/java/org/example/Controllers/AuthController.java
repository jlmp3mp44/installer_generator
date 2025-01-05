package org.example.Controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.server.Client;
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

  private final ValidationHandler usernameValidator;
  private final ValidationHandler passwordValidator;
  private final Client client;

  public AuthController() {
    usernameValidator = new NotEmptyHandler();
    usernameValidator.setNext(new UsernameFormatHandler());

    passwordValidator = new NotEmptyHandler();
    passwordValidator.setNext(new MinLengthHandler(6));

    client = new Client();
  }

  @FXML
  private void handleLogin(javafx.event.ActionEvent event) {
    String username = usernameField.getText();
    String password = passwordField.getText();

    try {
      // Validate fields
      usernameValidator.validate("Username", username);
      passwordValidator.validate("Password", password);

      // Send login request to the server
      String response = client.sendRequest("LOGIN " + username + " " + password);
      if (response.equals("Login successful")) {
        showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");
        navigateToWelcome(event);
      } else {
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
  private void handleRegister() {
    String username = usernameField.getText();
    String password = passwordField.getText();

    try {
      // Validate fields
      usernameValidator.validate("Username", username);
      passwordValidator.validate("Password", password);

      // Send register request to the server
      String response = client.sendRequest("REGISTER " + username + " " + password);
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
}
