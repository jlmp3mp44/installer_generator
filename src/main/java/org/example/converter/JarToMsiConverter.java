package org.example.converter;

public class JarToMsiConverter implements Converter {
  @Override
  public void convert(String inputFilePath, String outputFilePath) {
    System.out.println("Converting JAR file " + inputFilePath + " to MSI at " + outputFilePath);
    // Реалізація конвертації
  }
}