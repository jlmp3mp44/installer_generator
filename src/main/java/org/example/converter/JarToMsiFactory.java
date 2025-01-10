package org.example.converter;

public class JarToMsiFactory implements ConverterFactory {

  @Override
  public  Converter createConverter() {
    return new JarToMsiConverter();
  }
}
