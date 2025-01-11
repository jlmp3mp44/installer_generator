package org.example.converter.factory;

import org.example.converter.CombinedJarToMsiConverter;
import org.example.converter.Converter;

public class CombinedJarToMsiFactory implements ConverterFactory {

  @Override
  public Converter createConverter() {
    return new CombinedJarToMsiConverter();
  }
}
