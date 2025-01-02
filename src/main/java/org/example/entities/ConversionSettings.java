package entities;

public class ConversionSettings {
  private boolean addShortcut;
  private String installPath;
  private String licenseKey;


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