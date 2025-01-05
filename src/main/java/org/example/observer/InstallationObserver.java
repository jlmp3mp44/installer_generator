package org.example.observer;

public interface InstallationObserver {
  void onProgressUpdate(String message, int progressPercentage);

  // Додано метод для завершення процесу
  void onCompletion();
}
