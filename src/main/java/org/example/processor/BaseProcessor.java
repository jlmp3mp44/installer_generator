package org.example.processor;

import org.example.converter.Converter;

public class BaseProcessor implements FileProcessor {
  Converter converter;
  String inputFile;
  String outputFile;

  public BaseProcessor(Converter converter, String inputFile, String outputFile) {
    this.converter = converter;
    this.inputFile = inputFile;
    this.outputFile = outputFile;
  }

  @Override
  public String process() throws Exception {
      converter.convert(inputFile, outputFile);
      return outputFile;
  }
}
