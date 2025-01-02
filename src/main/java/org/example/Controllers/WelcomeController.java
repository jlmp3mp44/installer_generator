package org.example.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController {
  @FXML
  private void handleStart(ActionEvent event) {
    System.out.println("Кнопка натиснута! Починаємо роботу.");

    try {
      // Завантажуємо FXML для сторінки конвертації
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/pyconverter.fxml"));
      VBox root = loader.load();

      // Створюємо нову сцену
      Stage stage = new Stage();
      stage.setTitle("Python to EXE Converter");
      stage.setScene(new Scene(root, 600, 600));
      stage.show();

      // Закриваємо поточне вікно (якщо потрібно)
      Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
      currentStage.close();
    } catch (IOException e) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Failed to load the converter page");
      alert.setContentText("An error occurred while trying to load the Python converter page.");
      alert.showAndWait();
      e.printStackTrace();
    }
  }
}
