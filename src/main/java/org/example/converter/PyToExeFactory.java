package org.example.converter;

public class PyToExeFactory extends ConverterFactory {

  public static Converter createConverter() {
    return new PyToExeConverter();
  }
}
