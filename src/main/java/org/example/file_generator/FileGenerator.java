package org.example.file_generator;

public interface FileGenerator {
  String generateContent();
  void exportToFile(String filePath);
}
