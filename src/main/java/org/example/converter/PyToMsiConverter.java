package org.example.converter;

public class PyToMsiConverter implements Converter {
  @Override
  public void convert(String inputFilePath, String outputFilePath) {
    System.out.println("Converting Python file " + inputFilePath + " to MSI at " + outputFilePath);
    // Реалізація конвертації
  }
}
