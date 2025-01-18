package org.example.converter;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class PyToMsiConverter implements Converter {
  @Override
  public void convert(String inputFilePath, String outputFilePath) {
    System.out.println("Converting Python file " + inputFilePath + " to MSI at " + outputFilePath);

    String setupScript = "setup.py";
    String command = "python " + setupScript + " bdist_msi";

    try {
      String setupScriptContent = String.join("\n",
          "from cx_Freeze import setup, Executable",
          "setup(",
          "    name='GeneratedApp',",
          "    version='1.0',",
          "    description='Generated MSI from Python script',",
          "    executables=[Executable('" + inputFilePath + "')]",
          ")"
      );

      Files.write(
          Paths.get(setupScript),
          setupScriptContent.getBytes()
      );

      ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
      processBuilder.inheritIO();
      Process process = processBuilder.start();
      process.waitFor();

      Path msiPath = Paths.get("dist", "GeneratedApp-1.0-win64.msi");
      Files.move(msiPath, Paths.get(outputFilePath), StandardCopyOption.REPLACE_EXISTING);

      System.out.println("Conversion completed!");
    } catch (IOException | InterruptedException e) {
      System.err.println("Error during conversion: " + e.getMessage());
      e.printStackTrace();
    } finally {
      try {
        Files.deleteIfExists(Paths.get(setupScript));
        deleteDirectoryIfExists(Paths.get("dist"));
        deleteDirectoryIfExists(Paths.get("build"));
        deleteDirectoryIfExists(Paths.get("__pycache__"));
      } catch (IOException e) {
        System.err.println("Error cleaning up temporary files: " + e.getMessage());
      }
    }
  }

  private void deleteDirectoryIfExists(Path directory) throws IOException {
    if (Files.exists(directory) && Files.isDirectory(directory)) {
      try (Stream<Path> files = Files.walk(directory)) {
        files.sorted((p1, p2) -> p2.compareTo(p1))
            .forEach(path -> {
              try {
                Files.delete(path);
              } catch (IOException e) {
                System.err.println("Error deleting file: " + path + " - " + e.getMessage());
              }
            });
      }
    }
  }
}
