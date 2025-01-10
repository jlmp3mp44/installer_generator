package org.example.entities;

public class ConversionSettings {
  private boolean addShortcut;
  private String installPath;
  private String licenseKey;
  private boolean enableEncryption;
  private boolean enableCompression;

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

  public String getLicenseKey() {
    return licenseKey;
  }

  public void setLicenseKey(String licenseKey) {
    this.licenseKey = licenseKey;
  }

}