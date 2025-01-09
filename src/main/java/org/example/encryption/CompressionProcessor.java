package org.example.encryption;

public class CompressionProcessor implements FileProcessor {
  private final FileProcessor wrapped;

  public CompressionProcessor(FileProcessor wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public void process(String inputFile, String outputFile, String key) throws Exception {
    // Placeholder for compression logic
    wrapped.process(inputFile, outputFile, key);
  }
}
