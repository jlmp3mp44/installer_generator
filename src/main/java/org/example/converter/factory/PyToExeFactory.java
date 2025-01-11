package org.example.converter.factory;

import org.example.converter.Converter;
import org.example.converter.PyToExeConverter;
import org.example.converter.factory.ConverterFactory;

public class PyToExeFactory implements ConverterFactory {

  public Converter createConverter() {
    return new PyToExeConverter();
  }
}
