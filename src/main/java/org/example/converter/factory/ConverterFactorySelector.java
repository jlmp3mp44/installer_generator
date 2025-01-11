package org.example.converter.factory;


public class ConverterFactorySelector {
  public static ConverterFactory getFactory(String inputFileType, String outputFileType) {
    if (inputFileType.equalsIgnoreCase("py") && outputFileType.equalsIgnoreCase("exe")) {
      return new PyToExeFactory();
    } else if (inputFileType.equalsIgnoreCase("py") && outputFileType.equalsIgnoreCase("msi")) {
      return new PyToMsiFactory();
    } else if (inputFileType.equalsIgnoreCase("jar") && outputFileType.equalsIgnoreCase("exe")) {
      return new JarToExeFactory();
    } else if (inputFileType.equalsIgnoreCase("jar") && outputFileType.equalsIgnoreCase("msi")) {
      return new CombinedJarToMsiFactory();
    }
    throw new IllegalArgumentException("Unsupported conversion type");
  }
}
