package org.example.converter;

public class PyToMsiFactory extends ConverterFactory {

  public static Converter createConverter() {
    return new PyToMsiConverter();
  }
}

