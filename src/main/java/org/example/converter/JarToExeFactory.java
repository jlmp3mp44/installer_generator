package org.example.converter;

class JarToExeFactory implements ConverterFactory {
  @Override
  public Converter createConverter() {
    return new JarToExeConverter();
  }
}
