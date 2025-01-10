package org.example.encryption;

public class BaseProcessor implements FileProcessor {

  @Override
  public String process(String outputFile, String key) throws Exception {
      return outputFile;
  }
}
