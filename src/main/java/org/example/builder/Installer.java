package org.example.builder;

import java.util.ArrayList;
import java.util.List;
import org.example.converter.Converter;
import org.example.converter.factory.ConverterFactory;

import org.example.converter.factory.ConverterFactorySelector;
import org.example.processor.BaseProcessor;
import org.example.processor.BlowfishEncryptionStrategy;
import org.example.processor.CompressionProcessor;
import org.example.processor.EncryptionStrategy;
import org.example.processor.EncryptionStrategyFactory;
import org.example.processor.ShortcutProcessor;
import org.example.entities.ConversionSettings;
import org.example.entities.InputFile;
import org.example.entities.OutputFile;
import org.example.file_generator.WixFileGenerator;
import org.example.file_generator.XmlFileGenerator;
import org.example.launcher.DecryptAndLaunch;
import org.example.observer.InstallationObserver;
import org.example.observer.InstallationSubject;
import org.example.processor.EncryptionProcessor;
import org.example.processor.FileProcessor;

public class Installer{
  private InputFile file;
  private ConversionSettings settings;
  private OutputFile outputFile;
  private final InstallationSubject subject = new InstallationSubject();
  public Installer(InputFile file, ConversionSettings settings, OutputFile outputFile) {
    this.file = file;
    this.settings = settings;
    this.outputFile = outputFile;
  }

  public void generatePackage() {
    notifyObservers("Starting package generation...", 0);

    System.out.println("Generating " + outputFile.getFileType() + " file at " + outputFile.getFilePath());
    notifyObservers("Preparing conversion...", 20);

    ConverterFactory factory = ConverterFactorySelector.getFactory(file.getFileType().toString(), outputFile.getFileType().toString());
    Converter baseConverter  = factory.createConverter();

    FileProcessor processor = new BaseProcessor(baseConverter, file.getFilePath(), outputFile.getFilePath(), (message, progress) -> subject.notifyObservers(message, progress), subject);

    // Adjust processor based on settings
    if (settings.isEnableEncryption()) {
      String encryptionType = settings.getEncryptionStrategy(); // Отримуємо тип шифрування
      EncryptionStrategy encryptionStrategy = EncryptionStrategyFactory.getStrategy(encryptionType);

      processor = new EncryptionProcessor(new BlowfishEncryptionStrategy(), processor,  outputFile.getFilePath(), "mysecretkey12345",
          (message, progress) -> subject.notifyObservers(message, progress));

      if (settings.isEnableCompression()) {
        processor = new CompressionProcessor(processor, (message, progress) -> subject.notifyObservers(message, progress));
      }
    } else if (settings.isEnableCompression()) {
      processor = new CompressionProcessor(processor, (message, progress) -> subject.notifyObservers(message, progress));
    }

    if (!settings.isEnableEncryption() && !settings.isEnableCompression() && settings.isAddShortcut()) {
      processor = new ShortcutProcessor(processor, true, (message, progress) -> subject.notifyObservers(message, progress));
    }

    try {
      processor.process();
      notifyObservers("Processing completed successfully!", 100);
      notifyCompletion();
    } catch (Exception e) {
      notifyError("Failed to generate package: " + e.getMessage());
    }

    }


  private void decryptAndLaunch() {
    System.out.println("Starting decryption and launch process...");
    DecryptAndLaunch.launch(outputFile.getFilePath());
  }

  public void exportXml(String filePath) {
    XmlFileGenerator xmlGenerator = new XmlFileGenerator(file, outputFile);
    xmlGenerator.exportToFile(filePath);
  }

  public void exportWixXml(String filePath) {
    WixFileGenerator wixGenerator = new WixFileGenerator(file);
    wixGenerator.exportToFile(filePath);
  }
  public void addObserver(InstallationObserver observer) {
    subject.addObserver(observer);
  }

  public void notifyObservers(String message, int progressPercentage) {
    subject.notifyObservers(message, progressPercentage);
  }

  public void notifyCompletion() {
    subject.notifyCompletion();
  }

  public void notifyError(String errorMessage) {
    subject.notifyError(errorMessage);
  }

  public void reportError(String errorMessage) {
    notifyError(errorMessage);
  }


  public static class Builder {
    private InputFile file;
    private ConversionSettings settings;
    private OutputFile outputFile;
    private List<InstallationObserver> observers = new ArrayList<>();

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

    public Builder addObserver(InstallationObserver observer) {
      observers.add(observer);
      return this;
    }
    public Installer build() {
      Installer installer = new Installer(file, settings, outputFile);
      for (InstallationObserver observer : observers) {
        installer.addObserver(observer);
      }
      System.out.println("Observers added: " + observers.size());
      return installer;
    }


  }
}
