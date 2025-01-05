package org.example.observer;

import java.util.ArrayList;
import java.util.List;

public class InstallationSubject {
  private final List<InstallationObserver> observers = new ArrayList<>();

  public void addObserver(InstallationObserver observer) {
    observers.add(observer);
  }

  public void removeObserver(InstallationObserver observer) {
    observers.remove(observer);
  }

  public void notifyObservers(String message, int progressPercentage) {
    for (InstallationObserver observer : observers) {
      observer.onProgressUpdate(message, progressPercentage);
    }
  }

  // Новий метод для повідомлення про завершення
  public void notifyCompletion() {
    for (InstallationObserver observer : observers) {
      observer.onCompletion();
    }
  }
}
