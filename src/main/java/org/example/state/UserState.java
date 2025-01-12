package org.example.state;


import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
import org.example.builder.Installer;

public interface UserState {
  boolean isPremium();
  void enableEncryptionFeature(ButtonBase button);

  void enableCompressionFeature(ButtonBase button);
}

