package org.example.utils.adapter;


import org.example.converter.Converter;
import org.example.utils.FileProcessor;

public class ConverterAdapter implements FileProcessor {
  private final Converter converter;

  public ConverterAdapter(Converter converter) {
    this.converter = converter;
  }

  @Override
  public void process(String inputFile, String outputFile, String key) throws Exception {
    // Адаптер ігнорує ключ, оскільки `Converter` не підтримує шифрування
    converter.convert(inputFile, outputFile);
  }
}

