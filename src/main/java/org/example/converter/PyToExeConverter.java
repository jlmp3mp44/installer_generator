package org.example.converter;

import java.io.IOException;

public class PyToExeConverter implements Converter {
  @Override
  public void convert(String inputFilePath, String outputFilePath) {
    System.out.println("Converting Python file " + inputFilePath + " to EXE at " + outputFilePath);

    try {
      // Визначаємо директорію збереження та ім'я файлу
      java.nio.file.Path outputPath = java.nio.file.Paths.get(outputFilePath);
      String saveDirectory = outputPath.getParent().toString();
      String outputFileName = outputPath.getFileName().toString();

      // Формуємо команду для PyInstaller
      String command = String.join(" ",
          "pyinstaller",
          "--onefile",
          "--distpath", saveDirectory,
          "--name", outputFileName.replace(".exe", ""),
          inputFilePath
      );

      // Викликаємо команду через командний рядок
      ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
      processBuilder.inheritIO();  // Спільний вивід і ввід для поточного процесу
      Process process = processBuilder.start();
      process.waitFor();  // Чекаємо завершення процесу
      System.out.println("Conversion completed!");
    } catch (IOException | InterruptedException e) {
      System.err.println("Error during conversion: " + e.getMessage());
      e.printStackTrace();
    }
  }

}
