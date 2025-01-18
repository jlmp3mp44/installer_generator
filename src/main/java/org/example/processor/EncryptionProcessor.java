package org.example.processor;

import java.io.File;
import java.util.function.BiConsumer;

public class EncryptionProcessor implements FileProcessor {
  private final EncryptionStrategy strategy;
  private final FileProcessor wrapped;
  private String outputFile;
  private String key;
  private final BiConsumer<String, Integer> notifier;

  public EncryptionProcessor(EncryptionStrategy strategy, FileProcessor wrapped,  String outputFile, String key, BiConsumer<String, Integer> notifier) {
    this.wrapped = wrapped;
    this.strategy = strategy;
    this.outputFile = outputFile;
    this.key = key;
    this.notifier = notifier;
  }

  @Override
  public String process() throws Exception {
    outputFile = wrapped.process();
    notifier.accept("Encrypting...", 70);

    File input = new File(outputFile);
    if (!input.exists() || input.length() == 0) {
      throw new RuntimeException("Input file is missing or empty: " + outputFile);
    }
    outputFile =  strategy.encrypt(outputFile, key);
    return outputFile;
  }
}
