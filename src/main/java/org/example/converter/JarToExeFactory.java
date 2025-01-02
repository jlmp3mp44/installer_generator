package org.example.converter;

public class JarToExeFactory extends ConverterFactory {

  public static Converter createConverter(String inputFileType, String outputFileType) {
    return new JarToExeConverter();
  }
}
