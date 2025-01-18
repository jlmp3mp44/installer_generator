package org.example.state;


import javafx.scene.control.ButtonBase;
import org.example.builder.Installer;

public class PremiumState implements UserState {

  @Override
  public boolean isPremium() {
    return true;
  }

  @Override
  public void enableEncryptionFeature(ButtonBase button) {
    System.out.println("Encryption feature is enabled for premium users.");
    button.setDisable(false);
  }

  @Override
  public void enableCompressionFeature(ButtonBase button) {
    System.out.println("Encryption feature is enabled for premium users.");
    button.setDisable(false);
  }

}

