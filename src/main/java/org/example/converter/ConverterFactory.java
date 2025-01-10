package org.example.converter;


public class ConverterFactory {

  public static Converter createConverter(String inputFileType, String outputFileType) {
    if (inputFileType.equalsIgnoreCase("py") && outputFileType.equalsIgnoreCase("exe")) {
      return new PyToExeConverter();
    }
    else if (inputFileType.equalsIgnoreCase("py") && outputFileType.equalsIgnoreCase("msi")) {
      return new PyToMsiConverter();
    }
    else if (inputFileType.equalsIgnoreCase("jar") && outputFileType.equalsIgnoreCase("exe")) {
      return new JarToExeConverter();
    }
    else if (inputFileType.equalsIgnoreCase("jar") && outputFileType.equalsIgnoreCase("msi")) {
      return new JarToMsiConverter();
    }
    throw new IllegalArgumentException("Unsupported conversion type");
  }
}
