package org.example.entities;

public class OutputFile {
  private String filePath;
  private FileType fileType;
  private String icon;
  private boolean compressed;

  public OutputFile(String filePath, FileType fileType) {
    this.filePath = filePath;
    this.fileType = fileType;
  }

  public String getFilePath() {
    return filePath;
  }

  public FileType getFileType() {
    return fileType;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public String getIcon() {
    return icon;
  }

  public void setCompressed(boolean compressed) {
    this.compressed = compressed;
  }

  public boolean isCompressed() {
    return compressed;
  }

  public enum FileType {
    EXE, MSI
  }
}
