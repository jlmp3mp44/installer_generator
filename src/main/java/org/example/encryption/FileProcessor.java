package org.example.encryption;

public interface FileProcessor {
  void process(String inputFile, String outputFile, String key) throws Exception;
}
