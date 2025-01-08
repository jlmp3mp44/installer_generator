package org.example.builder;

import java.io.File;
import org.example.converter.Converter;
import org.example.converter.ConverterFactory;
import entities.ConversionSettings;
import entities.InputFile;
import entities.OutputFile;
import org.example.launcher.DecryptAndLaunch;
import org.example.observer.InstallationSubject;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.example.utils.AESEncryptionStrategy;
import org.example.utils.EncryptionDecorator;
import org.example.utils.FileProcessor;

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
    Converter baseConverter = ConverterFactory.createConverter(
        file.getFileType().toString(),
        outputFile.getFileType().toString()
    );
    //FileProcessor processor = new ConverterAdapter(baseConverter);

    notifyObservers("Converting file...", 50);

    try {
      baseConverter.convert(file.getFilePath(), outputFile.getFilePath());
      //processor.process(file.getFilePath(), outputFile.getFilePath(), "mysecretkey12345");
      // Інша логіка генерації пакету
      notifyObservers("Package generation completed successfully!", 100);
      notifyCompletion();
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Додаємо шифрування тільки якщо увімкнено в налаштуваннях
    if (settings.isEnableEncryption()) {
      notifyObservers("Encrypting", 80);
      FileProcessor processor = new EncryptionDecorator(new AESEncryptionStrategy());
      try {
        processor.process(outputFile.getFilePath(), outputFile.getFilePath(), "mysecretkey12345");
        notifyObservers("Package encrypting completed successfully!", 100);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      //decryptAndLaunch();
    }

  }

  private void decryptAndLaunch() {
    System.out.println("Starting decryption and launch process...");
    DecryptAndLaunch.launch(outputFile.getFilePath());
  }

  // Метод для створення XML конфігураційного файлу
  public void exportToXml(String filePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      writer.write(buildXml());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void exportToWixXml(String filePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      writer.write(buildWiXConfig());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Метод для створення XML для Launch4j
  public String buildXml() {
    return "<launch4jConfig>\n" +
        "  <outfile>" + outputFile.getFilePath() + "</outfile>\n" +
        "  <jar>" + file.getFilePath() + "</jar>\n" +
        "  <headerType>console</headerType>\n\n\n" + // Додано тип заголовка
        "  <jre>\n" +
        "    <path>auto</path>\n" + // Шлях до JRE
        "    <minVersion>11.0.0</minVersion>\n\n" +
        "  </jre>\n" +
        "</launch4jConfig>";
  }

  // Метод для створення XML для WiX Toolset
  public String buildWiXConfig() {
    return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<Wix xmlns=\"http://schemas.microsoft.com/wix/2006/wi\">\n" +
        "  <Product Id=\"*\" Name=\"Installer\" Language=\"1033\" Version=\"1.0.0.0\" Manufacturer=\"Example Co\" UpgradeCode=\"6c091c65-a4be-4ea9-b901-69c60878b1f3\">\n" +
        "    <Package InstallerVersion=\"200\" Compressed=\"yes\" InstallScope=\"perMachine\"/>\n" +
        "    <MediaTemplate EmbedCab=\"yes\"/>\n" +
        "    <Directory Id=\"TARGETDIR\" Name=\"SourceDir\">\n" +
        "      <Directory Id=\"ProgramFilesFolder\">\n" +
        "        <Directory Id=\"INSTALLDIR\" Name=\"MyApp\">\n" +
        "          <Component Id=\"MainExecutable\" Guid=\"6c091c65-a4be-4ea9-b901-69c60878b1f3\">\n" +
        "            <File Id=\"MAIN_FILE\" Source=\"" + file.getFilePath().replace("\\", "/") + "\"/>\n" +
        "          </Component>\n" +
        "        </Directory>\n" +
        "      </Directory>\n" +
        "    </Directory>\n" +
        "    <Feature Id=\"MainFeature\" Title=\"Main Feature\" Level=\"1\">\n" +
        "      <ComponentRef Id=\"MainExecutable\"/>\n" +
        "    </Feature>\n" +
        "  </Product>\n" +
        "</Wix>";
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

      return new Installer(file, settings, outputFile);
    }
  }
}
