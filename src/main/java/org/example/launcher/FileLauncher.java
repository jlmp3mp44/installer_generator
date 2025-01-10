package org.example.launcher;

import java.io.IOException;

public class FileLauncher {

  public static void launchExecutable(String filePath) throws IOException {
    System.out.println("Запуск файлу: " + filePath);
    ProcessBuilder builder = new ProcessBuilder(filePath);
    builder.inheritIO();
    Process process = builder.start();
    try {
      process.waitFor();
    } catch (InterruptedException e) {
      System.err.println("Процес було перервано: " + e.getMessage());
    }
  }

}

