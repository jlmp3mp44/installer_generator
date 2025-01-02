package org.example.converter;

public class JarToExeConverter implements Converter {
  @Override
  public void  convert(String inputFilePath, String outputFilePath) {
    System.out.println("Converting JAR file " + inputFilePath + " to EXE at " + outputFilePath);
    // Реалізація конвертації
  }
}
