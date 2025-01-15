package org.example.processor;

import java.util.function.BiConsumer;
import org.example.converter.Converter;

public class BaseProcessor implements FileProcessor {
  Converter converter;
  String inputFile;
  String outputFile;
  private final BiConsumer<String, Integer> notifier;

  public BaseProcessor(Converter converter, String inputFile, String outputFile, BiConsumer<String, Integer> notifier) {
    this.converter = converter;
    this.inputFile = inputFile;
    this.outputFile = outputFile;
    this.notifier = notifier;
  }

  @Override
  public String process() throws Exception {
    notifier.accept("Converting...", 50);
    System.out.println("Notifier called with progress: 70");
      converter.convert(inputFile, outputFile);
      return outputFile;
  }
}
