package org.example.state;


import org.example.builder.Installer;

public interface UserState {
  boolean isPremium();
  void enableEncryptionFeature(); // Налаштування GUI
  void handleConversion(boolean encryptionEnabled, Installer installer); // Виконання дій
}

