package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthController {

  @FXML
  private TextField usernameField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private Button loginButton;

  @FXML
  private Button registerButton;

  private final Map<String, String> users = new HashMap<>(); // Імітація бази даних

  @FXML
  public void initialize() {
    // Переконайтеся, що кнопки правильно ініціалізовані
    loginButton.setOnAction(event -> handleLogin(event));
    registerButton.setOnAction(event -> handleRegister());
  }

  @FXML
  private void handleLogin(javafx.event.ActionEvent event) {
    String username = usernameField.getText();
    String password = passwordField.getText();

    if (users.containsKey(username) && users.get(username).equals(password)) {
      showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");
      // Перехід до наступного вікна
      navigateToWelcome(event);
    } else {
      showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
    }
  }

  @FXML
  private void handleRegister() {
    String username = usernameField.getText();
    String password = passwordField.getText();

    if (users.containsKey(username)) {
      showAlert(Alert.AlertType.ERROR, "Registration Failed", "Username already exists.");
    } else if (username.isEmpty() || password.isEmpty()) {
      showAlert(Alert.AlertType.ERROR, "Registration Failed", "Username and password cannot be empty.");
    } else {
      users.put(username, password);
      showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "You can now log in.");
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
