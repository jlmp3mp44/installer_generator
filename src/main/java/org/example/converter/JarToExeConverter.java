package org.example.converter;

import java.io.*;

public class JarToExeConverter implements Converter {

  @Override
  public void convert(String inputFilePath, String outputFilePath) {
    System.out.println("Конвертація JAR у EXE: " + inputFilePath + " → " + outputFilePath);

    try {
      // Перевірка існування вхідного файлу
      File inputFile = new File(inputFilePath);
      if (!inputFile.exists()) {
        throw new FileNotFoundException("Вхідний файл не знайдено: " + inputFilePath);
      }

      // Перевірка наявності папки для збереження файлу
      File outputFile = new File(outputFilePath);
      File parentDir = outputFile.getParentFile();
      if (!parentDir.exists() && !parentDir.mkdirs()) {
        throw new IOException("Не вдалося створити папку для збереження файлу: " + parentDir.getAbsolutePath());
      }

      // Формування команди для конвертації (можна інтегрувати ліцензійний ключ в параметри)
      String command = "java -jar C:/Users/klubn/Launch4j/launch4j.jar -dontWrapJar " +
          inputFilePath + " " + outputFilePath;

      // Логування ліцензійного ключа (якщо є)


      // Виконання процесу
      Process process = Runtime.getRuntime().exec(command);

      // Зчитування виводу процесу
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line); // Логування процесу
      }

      process.waitFor(); // Очікування завершення процесу

      // Перевірка результату
      if (outputFile.exists()) {
        System.out.println("Конвертація завершена успішно! Файл збережено за адресою: " + outputFilePath);
      } else {
        System.err.println("Помилка: Файл не створено!");
      }

    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Помилка під час конвертації JAR у EXE!");
    }
  }
}
