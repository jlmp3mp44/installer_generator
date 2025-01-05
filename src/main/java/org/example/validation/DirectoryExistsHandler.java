package org.example.validation;

import java.nio.file.Files;
import java.nio.file.Path;

public class DirectoryExistsHandler extends ValidationHandler {
  @Override
  public void validate(String fieldName, String value) {
    if (!Files.isDirectory(Path.of(value))) {
      throw new IllegalArgumentException(fieldName + " must be an existing directory.");
    }
    super.validate(fieldName, value);
  }
}

