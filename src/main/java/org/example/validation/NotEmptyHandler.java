package org.example.validation;

public class NotEmptyHandler extends ValidationHandler {
  @Override
  public void validate(String fieldName, String value) {
    if (value == null || value.isEmpty()) {
      throw new IllegalArgumentException(fieldName + " cannot be empty.");
    }
    super.validate(fieldName, value);
  }
}

