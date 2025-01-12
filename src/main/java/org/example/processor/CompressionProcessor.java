package org.example.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CompressionProcessor implements FileProcessor {
  private final FileProcessor wrapped;
  private String outputFile;
  private String key;


  public CompressionProcessor(FileProcessor wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public String process() throws Exception {
    // Спочатку виконуємо обгортковий процес
    outputFile = wrapped.process();

    // Додаємо логіку стиснення
    String compressedFile = outputFile + ".zip";
    compressFile(outputFile, compressedFile);

    System.out.println("Файл успішно стиснутий до: " + compressedFile);
    return outputFile;
  }

  private void compressFile(String inputFile, String zipFile) throws Exception {
    try (FileOutputStream fos = new FileOutputStream(zipFile);
        ZipOutputStream zos = new ZipOutputStream(fos);
        FileInputStream fis = new FileInputStream(inputFile)) {

      File fileToZip = new File(inputFile);
      ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
      zos.putNextEntry(zipEntry);

      byte[] buffer = new byte[1024];
      int length;
      while ((length = fis.read(buffer)) >= 0) {
        zos.write(buffer, 0, length);
      }
    } catch (Exception e) {
      System.err.println("Помилка при стисненні файлу: " + e.getMessage());
      throw e;
    }
  }
}
