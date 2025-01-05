package org.example.validation;

public class FileExistsHandler extends ValidationHandler {
  @Override
  public void validate(String fieldName, String value) {
    if (!java.nio.file.Files.exists(java.nio.file.Path.of(value))) {
      throw new IllegalArgumentException(fieldName + " must be an existing file.");
    }
    super.validate(fieldName, value);
  }
}

