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

  public static class Builder {
    private InputFile file;
    private ConversionSettings settings;
    private OutputFile outputFile;

    public Builder addFile(InputFile file) {
      this.file = file;
      return this;
    }

    public Builder setConversionSettings(ConversionSettings settings) {
      this.settings = settings;
      return this;
    }

    public Builder setOutputFile(OutputFile outputFile) {
      this.outputFile = outputFile;
      return this;
    }

    public Installer build() {
      if (file == null || settings == null || outputFile == null) {
        throw new IllegalStateException("All fields must be set before building.");
      }
      return new Installer(file, settings, outputFile);
    }
  }

}
