package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController {

  @FXML
  private void handlePythonConvert(ActionEvent event) {
    navigateToPage(event, "/application/pyconverter.fxml", "Python File Converter");
  }

  @FXML
  private void handleJarConvert(ActionEvent event) {
    navigateToPage(event, "/application/jarconverter.fxml", "JAR File Converter");
  }

  @FXML
  private void handleExit(ActionEvent event) {
    Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
    currentStage.close();
  }

  private void navigateToPage(ActionEvent event, String fxmlPath, String title) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
      VBox root = loader.load();

      Stage stage = new Stage();
      stage.setTitle(title);
      stage.setScene(new Scene(root, 600, 600));
      stage.show();

      Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
      currentStage.close();
    } catch (IOException e) {
      showError("Failed to load the page", "An error occurred while trying to load the page.");
      e.printStackTrace();
    }
  }

  private void showError(String header, String content) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.showAndWait();
  }
}
