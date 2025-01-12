package org.example.launcher;

import java.io.File;
import org.example.processor.AESEncryptionStrategy;

public class DecryptAndLaunch {

  public static void launch(String encryptedFilePath) {
    // Шлях до тимчасового розшифрованого файлу
    String decryptedFilePath = "D:\\trpz_courcework\\installer_generator\\temp\\temp.exe";
    // Ключ для розшифрування
    String decryptionKey = "mysecretkey12345";

    AESEncryptionStrategy strategy = new AESEncryptionStrategy();

    try {
      // Перевірка, чи існує зашифрований файл
      File encryptedFile = new File(encryptedFilePath);
      if (!encryptedFile.exists() || encryptedFile.length() == 0) {
        System.err.println("Зашифрований файл не існує або порожній: " + encryptedFilePath);
        throw  new RuntimeException();
      }

      // Розшифрування
      strategy.decrypt(encryptedFilePath, decryptionKey);
      System.out.println("Файл успішно розшифровано: " + decryptedFilePath);

      decryptedFilePath =  encryptedFilePath;
      // Перевірка, чи розшифрований файл є виконуваним
      File decryptedFile = new File(decryptedFilePath);
      if (!decryptedFile.exists() || decryptedFile.length() == 0) {
        System.err.println("Розшифрований файл не створено або порожній: " + decryptedFilePath);
        return;
      }

      // Запуск розшифрованого файлу
      System.out.println("Запуск розшифрованого файлу...");
      FileLauncher.launchExecutable(decryptedFilePath);

    } catch (Exception e) {
      System.err.println("Помилка під час розшифрування або запуску: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
