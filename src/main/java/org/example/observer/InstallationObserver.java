package org.example.observer;

public interface InstallationObserver {
  void onProgressUpdate(String message, int progressPercentage);

  void onCompletion();

  void onError(String errorMessage);
}
