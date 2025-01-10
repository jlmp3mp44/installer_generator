package org.example.converter;

class PyToMsiFactory implements ConverterFactory {
  @Override
  public Converter createConverter() {
    return new PyToMsiConverter();
  }
}

