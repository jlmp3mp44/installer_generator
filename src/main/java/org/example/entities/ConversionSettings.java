package org.example.entities;

import org.example.processor.EncryptionStrategy;

public class ConversionSettings {
  private boolean addShortcut;
  private String installPath;
  private boolean enableEncryption;
  private boolean enableCompression;
  private String encryptionStrategy;

  public boolean isEnableEncryption() {
    return enableEncryption;
  }

  public void setEnableEncryption(boolean enableEncryption) {
    this.enableEncryption = enableEncryption;
  }

  public boolean isEnableCompression() {
    return enableCompression;
  }

  public void setEnableCompression(boolean enableCompression) {
    this.enableCompression = enableCompression;
  }

  public boolean isAddShortcut() {
    return addShortcut;
  }

  public void setAddShortcut(boolean addShortcut) {
    this.addShortcut = addShortcut;
  }

  public String getInstallPath() {
    return installPath;
  }

  public void setInstallPath(String installPath) {
    this.installPath = installPath;
  }

  public String getEncryptionStrategy() {
    return encryptionStrategy;
  }

  public void setEncryptionStrategy(String encryptionStrategy) {
    this.encryptionStrategy = encryptionStrategy;
  }
}