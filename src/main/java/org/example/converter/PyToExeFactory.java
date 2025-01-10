package org.example.converter;

public class PyToExeFactory implements ConverterFactory {

  public  Converter createConverter() {
    return new PyToExeConverter();
  }
}
