package org.example.state;

import org.example.builder.Installer;

public class BasicState implements UserState {

  @Override
  public boolean isPremium() {
    return false; // Не преміумний стан
  }

  @Override
  public void enableEncryptionFeature() {
    System.out.println("Encryption feature is disabled for basic users.");
    // У GUI можна приховати або відключити опцію шифрування
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
