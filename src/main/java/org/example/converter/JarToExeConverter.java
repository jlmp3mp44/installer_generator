package org.example.converter;

import java.io.*;
import org.example.builder.Installer;
import org.example.entities.InputFile;
import org.example.entities.OutputFile;

public class JarToExeConverter implements Converter {

  @Override
  public void convert(String inputFilePath, String outputFilePath) {
    System.out.println("Конвертація JAR у EXE: " + inputFilePath + " → " + outputFilePath);

    try {

      File inputFile = new File(inputFilePath);
      if (!inputFile.exists()) {
        throw new FileNotFoundException("Вхідний файл не знайдено: " + inputFilePath);
      }


      File outputFile = new File(outputFilePath);
      File parentDir = outputFile.getParentFile();
      if (!parentDir.exists() && !parentDir.mkdirs()) {
        throw new IOException("Не вдалося створити папку для збереження файлу: " + parentDir.getAbsolutePath());
      }


      String configFilePath = outputFilePath + ".xml";
      Installer installer = new Installer.Builder()
          .addFile(new InputFile(inputFilePath, InputFile.FileType.JAR))
          .setOutputFile(new OutputFile(outputFilePath, OutputFile.FileType.EXE))
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
    }
  }
}
