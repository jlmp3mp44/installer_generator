package org.example.converter;

import java.io.*;
import org.example.builder.Installer;
import org.example.entities.InputFile;
import org.example.entities.OutputFile;

public class JarToExeConverter implements Converter {

  @Override
  public void convert(String inputFilePath, String outputFilePath) {
    System.out.println("Конвертація JAR у EXE: " + inputFilePath + " → " + outputFilePath);

    String configFilePath = outputFilePath + ".xml";

    try {
      File outputFile = new File(outputFilePath);
      Installer installer = new Installer.Builder()
          .addFile(new InputFile(inputFilePath, InputFile.FileType.JAR))
          .setOutputFile(new OutputFile(outputFilePath, outputFilePath, OutputFile.FileType.EXE))
          .build();
      installer.exportXml(configFilePath);

      String command = "java -jar C:/Users/klubn/Launch4j/launch4j.jar " + configFilePath;
      Process process = Runtime.getRuntime().exec(command);

      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }

      process.waitFor();

      if (outputFile.exists()) {
        System.out.println("Конвертація завершена успішно! Файл збережено за адресою: " + outputFilePath);
      } else {
        System.err.println("Помилка: Файл не створено!");
      }

    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Помилка під час конвертації JAR у EXE!");
    } finally {
      try {
        File configFile = new File(configFilePath);
        if (configFile.exists() && configFile.delete()) {
          System.out.println("Тимчасовий файл конфігурації видалено: " + configFilePath);
        } else {
          System.err.println("Не вдалося видалити тимчасовий файл конфігурації: " + configFilePath);
        }
      } catch (Exception e) {
        System.err.println("Помилка під час видалення тимчасового файлу конфігурації: " + e.getMessage());
      }
    }
  }
}
