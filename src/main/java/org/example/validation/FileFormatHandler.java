package org.example.validation;


public class FileFormatHandler extends ValidationHandler {
  private final String expectedExtension;

  public FileFormatHandler(String expectedExtension) {
    this.expectedExtension = expectedExtension.toLowerCase();
  }
  @Override
  public void validate(String fieldName, String value) {
    if (!value.toLowerCase().endsWith(expectedExtension)) {
      throw new IllegalArgumentException(fieldName + " must have extension " + expectedExtension + ".");
    }
    super.validate(fieldName, value);
  }
}
