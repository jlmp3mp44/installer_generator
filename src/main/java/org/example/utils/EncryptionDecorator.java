package org.example.utils;

public class EncryptionDecorator implements FileProcessor {
  private final FileProcessor wrapped;
  private final EncryptionStrategy strategy;

  public EncryptionDecorator(FileProcessor wrapped, EncryptionStrategy strategy) {
    this.wrapped = wrapped;
    this.strategy = strategy;
  }

  @Override
  public void process(String inputFile, String outputFile, String key) throws Exception {
    String tempFile = "temp.file";
    wrapped.process(inputFile, tempFile, key);
    strategy.encrypt(tempFile, outputFile, key);
    new File(tempFile).delete();
  }
}
