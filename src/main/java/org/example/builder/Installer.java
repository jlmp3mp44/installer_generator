package org.example.builder;

import java.util.ArrayList;
import java.util.List;
import org.example.converter.Converter;
import org.example.converter.ConverterFactory;

import org.example.encryption.BaseProcessor;
import org.example.encryption.CompressionProcessor;
import org.example.entities.ConversionSettings;
import org.example.entities.InputFile;
import org.example.entities.OutputFile;
import org.example.file_generator.WixFileGenerator;
import org.example.file_generator.XmlFileGenerator;
import org.example.launcher.DecryptAndLaunch;
import org.example.observer.InstallationObserver;
import org.example.observer.InstallationSubject;
import org.example.encryption.AESEncryptionStrategy;
import org.example.encryption.EncryptionProcessor;
import org.example.encryption.FileProcessor;

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

    Converter baseConverter = ConverterFactory.createConverter(
        file.getFileType().toString(),
        outputFile.getFileType().toString()
    );

    notifyObservers("Converting file...", 50);

    try {
      baseConverter.convert(file.getFilePath(), outputFile.getFilePath());
      notifyObservers("Package generation completed successfully!", 100);
      notifyCompletion();
    } catch (Exception e) {
      e.printStackTrace();
    }

    FileProcessor processor =  new BaseProcessor();
    if (settings.isEnableEncryption()) {
      notifyObservers("Encrypting", 80);
      processor = new EncryptionProcessor(new AESEncryptionStrategy());
    }
    if (settings.isEnableCompression()) {
      processor = new CompressionProcessor(processor);
    }
    try {
      processor.process(outputFile.getFilePath(), "mysecretkey12345");
      notifyObservers("Package encrypting completed successfully!", 100);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

      //decryptAndLaunch();
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
      return installer;
    }
  }
}
