package org.example.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.function.BiConsumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CompressionProcessor implements FileProcessor {
  private final FileProcessor wrapped;
  private String outputFile;
  private final BiConsumer<String, Integer> notifier;

  public CompressionProcessor(FileProcessor wrapped, BiConsumer<String, Integer> notifier) {
    this.wrapped = wrapped;
    this.notifier = notifier;
  }

  @Override
  public String process() throws Exception {
    outputFile = wrapped.process();
    notifier.accept("Compressing...", 80);

    String compressedFile = outputFile + ".zip";
    compressFile(outputFile, compressedFile);

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
