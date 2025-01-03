package org.example.converter;


public class ConverterFactory {

  public static Converter createConverter(String inputFileType, String outputFileType) {
    if (inputFileType.equalsIgnoreCase("py") && outputFileType.equalsIgnoreCase("exe")) {
      return new PyToExeConverter();
    } else if (inputFileType.equalsIgnoreCase("jar") && outputFileType.equalsIgnoreCase("msi")) {
      return new JarToMsiConverter();
    }
    // Додайте інші варіанти за потребою
    throw new IllegalArgumentException("Unsupported conversion type");
  }
}
