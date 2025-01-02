package org.example.converter;

public class JarToMsiFactory extends ConverterFactory {

  public static Converter createConverter() {
    return new JarToMsiConverter();
  }
}
