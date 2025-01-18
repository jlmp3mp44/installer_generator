package org.example.observer;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Button;

public class GuiObserver implements InstallationObserver {
  private final ProgressBar progressBar;
  private final Label statusLabel;
  private final Button convertButton;

  public GuiObserver(ProgressBar progressBar, Label statusLabel, Button convertButton) {
    this.progressBar = progressBar;
    this.statusLabel = statusLabel;
    this.convertButton = convertButton;
  }

  @Override
  public void onProgressUpdate(String message, int progress) {
    System.out.println("GuiObserver received progress: " + progress + ", Message: " + message);
    Platform.runLater(() -> {
      System.out.println("Updating progress bar in GUI with progress: " + (progress / 100.0));
      progressBar.setProgress(progress / 100.0);
      statusLabel.setText("Status: " + message);
  // Увімкнення кнопки після завершення
      if (progress == 100) {
        convertButton.setDisable(false);
        statusLabel.setText("Status: Completed!");
      } else {
        convertButton.setDisable(true); // Вимкнення кнопки під час роботи
      }
    });
  }

  @Override
  public void onCompletion() {
    Platform.runLater(() -> {
      statusLabel.setText("Conversion completed successfully!");
      progressBar.setProgress(1.0); // Повний прогрес
      convertButton.setDisable(false);
    });
  }

  @Override
  public void onError(String errorMessage) {
    Platform.runLater(() -> {
      statusLabel.setText("Error: " + errorMessage);
      progressBar.setProgress(0); // Скидання прогресу
      convertButton.setDisable(false);
    });
  }
}
