package org.example.converter;

import java.io.IOException;

public class PyToMsiConverter implements Converter {
  @Override
  public void convert(String inputFilePath, String outputFilePath) {
    System.out.println("Converting Python file " + inputFilePath + " to MSI at " + outputFilePath);

    // Створюємо команду для cx_Freeze з конфігурацією для створення MSI
    String setupScript = "setup.py";
    String command = "python " + setupScript + " bdist_msi";

    try {
      // Генеруємо файл setup.py
      String setupScriptContent = String.join("\n",
          "from cx_Freeze import setup, Executable",
          "setup(",
          "    name='GeneratedApp',",
          "    version='1.0',",
          "    description='Generated MSI from Python script',",
          "    executables=[Executable('" + inputFilePath + "')]",
          ")"
      );

      // Записуємо setup.py
      java.nio.file.Files.write(
          java.nio.file.Paths.get(setupScript),
          setupScriptContent.getBytes()
      );

      // Викликаємо команду для створення MSI
      ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
      processBuilder.inheritIO();  // Спільний вивід і ввід
      Process process = processBuilder.start();
      process.waitFor();  // Чекаємо завершення

      // Переміщуємо створений MSI до вихідної директорії
      java.nio.file.Path msiPath = java.nio.file.Paths.get("dist", "GeneratedApp-1.0-win64.msi");
      java.nio.file.Files.move(msiPath, java.nio.file.Paths.get(outputFilePath));

      System.out.println("Conversion completed!");
    } catch (IOException | InterruptedException e) {
      System.err.println("Error during conversion: " + e.getMessage());
      e.printStackTrace();
    } finally {
      // Видаляємо тимчасові файли
      try {
        java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(setupScript));
        java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get("dist"));
        java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get("build"));
        java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get("__pycache__"));
      } catch (IOException e) {
        System.err.println("Error cleaning up temporary files: " + e.getMessage());
      }
    }
  }
}
