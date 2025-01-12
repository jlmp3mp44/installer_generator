package org.example.observer;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Button;

public class GuiObserver implements InstallationObserver {
  private final ProgressBar progressBar;
  private final Label statusLabel;
  private final Button convertButton;
  private long startTime;


  public GuiObserver(ProgressBar progressBar, Label statusLabel, Button convertButton) {
    this.progressBar = progressBar;
    this.statusLabel = statusLabel;
    this.convertButton = convertButton;
  }

  public void startTimer() {
    startTime = System.currentTimeMillis();
  }

  @Override
  public void onProgressUpdate(String message, int progressPercentage) {
    Platform.runLater(() -> {
      // Оновлення статусу
      statusLabel.setText(message);

      // Оновлення прогрес-бару
      if (progressPercentage >= 0) {
        progressBar.setProgress(progressPercentage / 100.0);
      }
    });
  }

  @Override
  public void onCompletion() {
    Platform.runLater(() -> {
      long elapsedTime = System.currentTimeMillis() - startTime;
      statusLabel.setText("Process completed in " + (elapsedTime / 1000) + " seconds!");
      progressBar.setProgress(1.0);
      convertButton.setDisable(false);
    });
  }

  public void onError(String errorMessage) {
    Platform.runLater(() -> {
      statusLabel.setText("Error: " + errorMessage);
      progressBar.setProgress(0);
      convertButton.setDisable(false); // Розблокування кнопки навіть у разі помилки
    });
  }
}
