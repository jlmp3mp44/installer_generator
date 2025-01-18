package org.example.processor;

import java.util.function.BiConsumer;
import org.example.converter.Converter;
import org.example.observer.InstallationSubject;

public class BaseProcessor implements FileProcessor {
  Converter converter;
  String inputFile;
  String outputFile;
  private final BiConsumer<String, Integer> notifier;
  private final InstallationSubject subject;


  public BaseProcessor(Converter converter, String inputFile, String outputFile, BiConsumer<String, Integer> notifier, InstallationSubject subject) {
    this.converter = converter;
    this.inputFile = inputFile;
    this.outputFile = outputFile;
    this.notifier = notifier;
    this.subject = subject;
  }

  @Override
  public String process() throws Exception {
    subject.notifyObservers("Converting", 50);
    notifier.accept("Converting...", 50);
    System.out.println("Notifier called with progress: 70");
      converter.convert(inputFile, outputFile);
      return outputFile;
  }
}
