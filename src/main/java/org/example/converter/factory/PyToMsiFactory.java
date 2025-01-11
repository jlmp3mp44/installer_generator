package org.example.converter.factory;

import org.example.converter.Converter;
import org.example.converter.PyToMsiConverter;
import org.example.converter.factory.ConverterFactory;

public class PyToMsiFactory implements ConverterFactory {
  @Override
  public Converter createConverter() {
    return new PyToMsiConverter();
  }
}

