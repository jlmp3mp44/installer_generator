package org.example.utils;

import java.io.File;
import java.io.IOException;

public class EncryptionDecorator implements FileProcessor {
  private final EncryptionStrategy strategy;

  public EncryptionDecorator(EncryptionStrategy strategy) {
    this.strategy = strategy;
  }

  @Override
  public void process(String inputFile, String outputFile, String key) throws Exception {
    // Перевірка існування вхідного файлу
    File input = new File(inputFile);
    if (!input.exists() || input.length() == 0) {
      throw new RuntimeException("Input file is missing or empty: " + inputFile);
    }

    System.out.println("Encrypting file: " + inputFile);

    // Виконання шифрування
    strategy.encrypt(outputFile, key);

    // Перевірка створення вихідного файлу
    File output = new File(outputFile);
    if (!output.exists() || output.length() == 0) {
      throw new RuntimeException("Encryption failed: Output file is missing or empty: " + outputFile);
    }

    System.out.println("Encryption completed. Output file: " + outputFile);
  }
}
