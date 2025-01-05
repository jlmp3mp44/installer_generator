package org.example.builder;

import org.example.converter.Converter;
import org.example.converter.ConverterFactory;
import entities.ConversionSettings;
import entities.InputFile;
import entities.OutputFile;
import org.example.observer.InstallationSubject;

public class Installer extends InstallationSubject {
  private InputFile file;
  private ConversionSettings settings;
  private OutputFile outputFile;

  public Installer(InputFile file, ConversionSettings settings, OutputFile outputFile) {
    this.file = file;
    this.settings = settings;
    this.outputFile = outputFile;
  }

  public void generatePackage() {
    notifyObservers("Starting package generation...", 0);

    System.out.println("Generating " + outputFile.getFileType() + " file at " + outputFile.getFilePath());
    notifyObservers("Preparing conversion...", 20);

    // Викликаємо фабрику для отримання правильного конвертера
    Converter converter = ConverterFactory.createConverter(
        file.getFileType().toString(),
        outputFile.getFileType().toString()
    );

    notifyObservers("Converting file...", 50);
    // Виконуємо конвертацію
    converter.convert(file.getFilePath(), outputFile.getFilePath());

    notifyObservers("Finalizing package generation...", 80);
    // Інша логіка генерації пакету
    notifyObservers("Package generation completed successfully!", 100);
    notifyCompletion();
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

