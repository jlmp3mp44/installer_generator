package org.example.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class EncryptionDecorator implements FileProcessor {
  private final FileProcessor wrapped;
  private final EncryptionStrategy strategy;

  public EncryptionDecorator(FileProcessor wrapped, EncryptionStrategy strategy) {
    this.wrapped = wrapped;
    this.strategy = strategy;
  }

  @Override
  public void process(String inputFile, String outputFile, String key) throws Exception {
    // Створення тимчасового файлу
    Path tempFilePath = Files.createTempFile("temp", ".file");
    String tempFile = tempFilePath.toString();

    try {
      // Виконання обробки та шифрування
      wrapped.process(inputFile, tempFile, key);
      strategy.encrypt(tempFile, outputFile, key);
    } finally {
      // Видалення тимчасового файлу
      new File(tempFile).delete();
    }
  }
}
