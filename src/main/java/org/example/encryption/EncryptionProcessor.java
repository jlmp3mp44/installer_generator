package org.example.encryption;

import java.io.File;

public class EncryptionProcessor implements FileProcessor {
  private final EncryptionStrategy strategy;

  public EncryptionProcessor(EncryptionStrategy strategy) {
    this.strategy = strategy;
  }

  @Override
  public String process(String outputFile, String key) throws Exception {
    File input = new File(outputFile);
    if (!input.exists() || input.length() == 0) {
      throw new RuntimeException("Input file is missing or empty: " + outputFile);
    }

    System.out.println("Encrypting file: " + outputFile);

    // Виконання шифрування
   outputFile =  strategy.encrypt(outputFile, key);

    // Перевірка створення вихідного файлу
   /* File output = new File(outputFile);
    if (!output.exists() || output.length() == 0) {
      throw new RuntimeException("Encryption failed: Output file is missing or empty: " + outputFile);
    }*/

    System.out.println("Encryption completed. Output file: " + outputFile);
    return outputFile;
  }
}
