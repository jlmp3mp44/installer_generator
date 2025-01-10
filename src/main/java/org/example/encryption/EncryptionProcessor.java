package org.example.encryption;

import java.io.File;

public class EncryptionProcessor implements FileProcessor {
  private final EncryptionStrategy strategy;
  private String outputFile;
  private String key;

  public EncryptionProcessor(EncryptionStrategy strategy, String outputFile, String key) {
    this.strategy = strategy;
    this.outputFile = outputFile;
    this.key = key;
  }


  @Override
  public String process() throws Exception {
    File input = new File(outputFile);
    if (!input.exists() || input.length() == 0) {
      throw new RuntimeException("Input file is missing or empty: " + outputFile);
    }

    System.out.println("Encrypting file: " + outputFile);

    // Виконання шифрування
   outputFile =  strategy.encrypt(outputFile, key);

    System.out.println("Encryption completed. Output file: " + outputFile);
    return outputFile;
  }
}
