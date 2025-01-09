package org.example.converter;

import java.io.*;
import org.example.builder.Installer;
import org.example.entities.InputFile;
import org.example.entities.OutputFile;

public class JarToMsiConverter implements Converter {

  @Override
  public void convert(String inputFilePath, String outputFilePath) {
    System.out.println("Конвертація JAR у MSI: " + inputFilePath + " → " + outputFilePath);

    try {

      File outputFile = new File(outputFilePath);

      // Створюємо WiX XML конфігураційний файл
      String configFilePath = outputFilePath + ".wxs";
      Installer installer = new Installer.Builder()
          .addFile(new InputFile(inputFilePath, InputFile.FileType.JAR))
          .setOutputFile(new OutputFile(outputFilePath, outputFilePath, OutputFile.FileType.MSI))
          .build();
      installer.exportWixXml(configFilePath);

      // Виконання процесів WiX Toolset
      String candlePath = "D:\\trpz_courcework\\wix\\bin\\candle.exe";
      String lightPath = "D:\\trpz_courcework\\wix\\bin\\light.exe";

      // Створюємо шлях до файлу .wixobj, вказуючи правильну директорію
     // String wixObjFilePath = outputFile.getParent() + File.separator + configFilePath.replace(".wxs", ".wixobj");
      String wixObjFilePath = configFilePath.replace(".wxs", ".wixobj");


      // Виконання процесів WiX Toolset
      executeCommand(candlePath, configFilePath, "-out", wixObjFilePath);
      executeCommand(lightPath, "-out", outputFilePath, wixObjFilePath);

      // Перевірка результату
      if (outputFile.exists()) {
        System.out.println("Конвертація завершена успішно! Файл збережено за адресою: " + outputFilePath);
      } else {
        System.err.println("Помилка: Файл не створено!");
      }

    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Помилка під час конвертації JAR у MSI!");
    }
  }

  private void executeCommand(String... command) throws IOException, InterruptedException {
    System.out.println("Executing command: " + String.join(" ", command)); // Додаємо логування команд

    ProcessBuilder processBuilder = new ProcessBuilder(command);
    processBuilder.redirectErrorStream(true);

    Process process = processBuilder.start();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line); // Логуємо кожен рядок виводу
      }
    }
    int exitCode = process.waitFor();
    if (exitCode != 0) {
      throw new IOException("Команда завершилася з кодом помилки: " + exitCode);
    }
  }

}
