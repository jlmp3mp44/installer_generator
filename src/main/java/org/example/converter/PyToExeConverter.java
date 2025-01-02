package org.example.converter;

import java.io.IOException;

public class PyToExeConverter implements Converter {
  @Override
  public void convert(String inputFilePath, String outputFilePath) {
    System.out.println("Converting Python file " + inputFilePath + " to EXE at " + outputFilePath);

    // Створюємо команду для PyInstaller
    String command = "pyinstaller --onefile --distpath " + outputFilePath + " " + inputFilePath;

    try {
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
