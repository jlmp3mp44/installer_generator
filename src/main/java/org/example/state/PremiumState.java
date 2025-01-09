package org.example.state;


import javafx.scene.control.ButtonBase;
import org.example.builder.Installer;

public class PremiumState implements UserState {

  @Override
  public boolean isPremium() {
    return true; // Преміумний стан
  }

  @Override
  public void enableEncryptionFeature(ButtonBase button) {
    System.out.println("Encryption feature is enabled for premium users.");
    button.setDisable(false);
  }

  @Override
  public void handleConversion(boolean encryptionEnabled, Installer installer) {
    if (encryptionEnabled) {
      System.out.println("Performing conversion with encryption...");
      // Логіка конвертації з шифруванням
    } else {
      System.out.println("Performing conversion without encryption...");
      installer.generatePackage();
    }
  }
}

