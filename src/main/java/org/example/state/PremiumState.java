package org.example.state;


import org.example.builder.Installer;

public class PremiumState implements UserState {

  @Override
  public boolean isPremium() {
    return true; // Преміумний стан
  }

  @Override
  public void enableEncryptionFeature() {
    System.out.println("Encryption feature is enabled for premium users.");
    // У GUI можна показати активну опцію шифрування
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

