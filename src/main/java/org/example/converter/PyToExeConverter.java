package org.example.converter;

import java.io.IOException;

public class PyToExeConverter implements Converter {
  @Override
  public void convert(String inputFilePath, String outputFilePath) {
    System.out.println("Converting Python file " + inputFilePath + " to EXE at " + outputFilePath);

    try {
      java.nio.file.Path outputPath = java.nio.file.Paths.get(outputFilePath);
      String saveDirectory = outputPath.getParent().toString();
      String outputFileName = outputPath.getFileName().toString();
      String command = String.join(" ",
          "pyinstaller",
          "--onefile",
          "--distpath", saveDirectory,
          "--name", outputFileName.replace(".exe", ""),
          inputFilePath
      );

      ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
      processBuilder.inheritIO();
      Process process = processBuilder.start();
      process.waitFor();
      System.out.println("Conversion completed!");
    } catch (IOException | InterruptedException e) {
      System.err.println("Error during conversion: " + e.getMessage());
      e.printStackTrace();
    }
  }

}
