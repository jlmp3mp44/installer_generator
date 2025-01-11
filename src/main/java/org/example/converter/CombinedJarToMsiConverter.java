package org.example.converter;

public class CombinedJarToMsiConverter implements Converter{

  private final JarToExeConverter jarToExeConverter;
  private final JarToMsiConverter jarToMsiConverter;

  public CombinedJarToMsiConverter() {
    this.jarToExeConverter = new JarToExeConverter();
    this.jarToMsiConverter = new JarToMsiConverter();
  }

  public void convert(String jarPath, String msiPath) {
    try {
      // Тимчасовий шлях для EXE-файлу
      String exePath = jarPath.replace(".jar", ".exe");

      // Конвертація JAR → EXE
      jarToExeConverter.convert(jarPath, exePath);

      // Конвертація EXE → MSI
      jarToMsiConverter.convert(exePath, msiPath);

      System.out.println("Конвертація JAR → MSI завершена успішно!");

    } catch (Exception e) {
      System.err.println("Помилка під час конвертації JAR → MSI!");
      e.printStackTrace();
    }
  }
}