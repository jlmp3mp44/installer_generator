package org.example.entities;

import org.example.validation.DirectoryExistsHandler;
import org.example.validation.NotEmptyHandler;
import org.example.validation.ValidationHandler;

public class OutputFile {
  private String filePath;
  private String fileName;
  private FileType fileType;
  private String icon;
  private ValidationHandler pathValidator;
  private ValidationHandler fileNameValidator;

  public OutputFile(String filePath, String fileName,  FileType fileType) {
    this.filePath = filePath;
    this.fileName = fileName;
    this.fileType = fileType;
    initializeValidator();
  }

  private void initializeValidator() {
    pathValidator =  new NotEmptyHandler().
        setNext(new DirectoryExistsHandler());
    fileNameValidator =  new NotEmptyHandler();

  }

  public void validate() {
    pathValidator.validate("Save Path", filePath);
    fileNameValidator.validate("Save file name", fileName);


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


  public enum FileType {
    EXE, MSI
  }
}
