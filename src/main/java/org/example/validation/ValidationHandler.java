package org.example.validation;

public abstract class ValidationHandler {
  private ValidationHandler next;

  public ValidationHandler setNext(ValidationHandler next) {
    this.next = next;
    return next;
  }
  public void validate(String fieldName, String value) {
    if (next != null) {
      next.validate(fieldName, value);
    }
  }
}

