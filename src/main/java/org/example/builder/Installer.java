package org.example.builder;

import org.example.converter.Converter;
import org.example.converter.ConverterFactory;
import entities.ConversionSettings;
import entities.InputFile;
import entities.OutputFile;

public class Installer {
  private InputFile file;
  private ConversionSettings settings;
  private OutputFile outputFile;

  public Installer(InputFile file, ConversionSettings settings, OutputFile outputFile) {
    this.file = file;
    this.settings = settings;
    this.outputFile = outputFile;
  }

  public void generatePackage() {
    System.out.println("Generating " + outputFile.getFileType() + " file at " + outputFile.getFilePath());

    // Викликаємо фабрику для отримання правильного конвертера
    Converter converter = ConverterFactory.createConverter(file.getFileType().toString(), outputFile.getFileType().toString());

    // Виконуємо конвертацію
    converter.convert(file.getFilePath(), outputFile.getFilePath());

    // Інша логіка генерації пакету
  }
}
