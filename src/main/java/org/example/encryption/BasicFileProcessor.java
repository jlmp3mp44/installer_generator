package org.example.encryption;

import java.io.File;
import java.nio.file.Files;

public class BasicFileProcessor implements FileProcessor {
  @Override
  public void process(String inputFile, String outputFile, String key) throws Exception {
    Files.copy(new File(inputFile).toPath(), new File(outputFile).toPath());
  }
}