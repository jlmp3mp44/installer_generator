package org.example.validation;

public class MinLengthHandler extends ValidationHandler {
  private final int minLength;

  public MinLengthHandler(int minLength) {
    this.minLength = minLength;
  }
  @Override
  public void validate(String fieldName, String value) {
    if (value == null || value.length() < minLength) {
      throw new IllegalArgumentException(fieldName + " must be at least " + minLength + " characters long.");
    }
    super.validate(fieldName, value);
  }
}
