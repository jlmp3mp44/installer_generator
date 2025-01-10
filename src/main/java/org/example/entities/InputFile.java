package org.example.entities;

import org.example.validation.FileExistsHandler;
import org.example.validation.FileFormatHandler;
import org.example.validation.NotEmptyHandler;
import org.example.validation.ValidationHandler;

public class InputFile {
  private String filePath;
  private FileType fileType;
  private ValidationHandler fileValidator;


  public InputFile(String filePath, FileType fileType) {
    this.filePath = filePath;
    this.fileType = fileType;
    initializeValidator();
  }

  private void initializeValidator() {
    fileValidator = new NotEmptyHandler()
        .setNext(new FileFormatHandler("." + fileType.toString().toLowerCase()))
        .setNext(new FileExistsHandler());
  }


  public String getFilePath() {
    return filePath;
  }

  public FileType getFileType() {
    return fileType;
  }

  public void validate() {
    fileValidator.validate(fileType + "File Path", filePath);

  }

  public enum FileType {
    PY, JAR
  }
}

