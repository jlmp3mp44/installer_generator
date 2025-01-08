package org.example.entities;

import java.io.File;

// InputFile.java
public class InputFile {
  private String filePath;
  private FileType fileType;

  public InputFile(String filePath, FileType fileType) {
    this.filePath = filePath;
    this.fileType = fileType;
  }

  public String getFilePath() {
    return filePath;
  }

  public FileType getFileType() {
    return fileType;
  }

  public boolean validate() {
    // Validate file existence and format
    File file = new File(filePath);
    return file.exists() && fileType != null;
  }

  public enum FileType {
    PY, JAR
  }
}

