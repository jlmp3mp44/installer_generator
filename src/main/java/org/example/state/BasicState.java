package org.example.state;

import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
import org.example.builder.Installer;

public class BasicState implements UserState {

  @Override
  public boolean isPremium() {
    return false; // Не преміумний стан
  }

  @Override
  public void enableEncryptionFeature(ButtonBase button) {
    System.out.println("Encryption feature is disabled for basic users.");
    button.setDisable(true);
  }

  @Override
  public void handleConversion(boolean encryptionEnabled, Installer installer) {
    if (encryptionEnabled) {
      throw new UnsupportedOperationException("Encryption is not allowed for basic users.");
    }

    System.out.println("Performing basic conversion...");
    // Викликаємо стандартну конвертацію через Installer
    installer.generatePackage();
  }
}
