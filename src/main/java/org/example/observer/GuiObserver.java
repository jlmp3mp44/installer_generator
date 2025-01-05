package org.example.observer;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Button;

public class GuiObserver implements InstallationObserver {
  private final ProgressBar progressBar;
  private final Label statusLabel;
  private final Button convertButton; // Додано поле для кнопки

  public GuiObserver(ProgressBar progressBar, Label statusLabel, Button convertButton) {
    this.progressBar = progressBar;
    this.statusLabel = statusLabel;
    this.convertButton = convertButton; // Ініціалізуємо кнопку
  }

  @Override
  public void onProgressUpdate(String message, int progressPercentage) {
    Platform.runLater(() -> {
      statusLabel.setText(message);
      if (progressPercentage >= 0) {
        progressBar.setProgress(progressPercentage / 100.0);
      }
    });
  }

  @Override
  public void onCompletion() {
    Platform.runLater(() -> {
      // Розблокувати кнопку після завершення
      convertButton.setDisable(false);
    });
  }
}
