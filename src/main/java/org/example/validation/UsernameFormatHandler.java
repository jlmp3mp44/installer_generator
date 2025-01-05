package org.example.validation;

public class UsernameFormatHandler extends ValidationHandler {
  @Override
  public void validate(String fieldName, String value) {
    if (!value.matches("^[a-zA-Z0-9._-]+$")) {
      throw new IllegalArgumentException(fieldName + " contains invalid characters. Only letters, numbers, '.', '_', and '-' are allowed.");
    }
    super.validate(fieldName, value);
  }
}
