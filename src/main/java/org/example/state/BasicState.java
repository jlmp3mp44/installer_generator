package org.example.state;

import javafx.scene.control.ButtonBase;

public class BasicState implements UserState {

  @Override
  public boolean isPremium() {
    return false;
  }

  @Override
  public void enableEncryptionFeature(ButtonBase button) {
    System.out.println("Encryption feature is disabled for basic users.");
    button.setDisable(true);
  }

  @Override
  public void enableCompressionFeature(ButtonBase button) {
    System.out.println("Compression feature is disabled for basic users.");
    button.setDisable(true);
  }

}
