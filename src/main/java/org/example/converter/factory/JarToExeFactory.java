package org.example.converter.factory;

import org.example.converter.Converter;
import org.example.converter.JarToExeConverter;
import org.example.converter.factory.ConverterFactory;

public  class JarToExeFactory implements ConverterFactory {
  @Override
  public Converter createConverter() {
    return new JarToExeConverter();
  }
}
