package org.example.launcher;

import java.io.File;
import org.example.utils.AESEncryptionStrategy;

public class DecryptAndLaunch {

  public static void launch(String encryptedFilePath) {
    // Шлях до тимчасового розшифрованого файлу
    String decryptedFilePath = "D:\\trpz_courcework\\temp\\temp.exe";
    // Ключ для розшифрування
    String decryptionKey = "mysecretkey12345";

    AESEncryptionStrategy strategy = new AESEncryptionStrategy();

    if (!new File(decryptedFilePath).exists() || new File(decryptedFilePath).length() == 0) {
      System.err.println("Розшифрований файл не створено або порожній.");
      return;
    }

    try {
      // Розшифрування
      strategy.decrypt(encryptedFilePath, decryptedFilePath, decryptionKey);
      System.out.println("Файл успішно розшифровано: " + decryptedFilePath);

      // Запуск розшифрованого файлу
      FileLauncher.launchExecutable(decryptedFilePath);

    } catch (Exception e) {
      System.err.println("Помилка під час розшифрування або запуску: " + e.getMessage());
      e.printStackTrace();
    } finally {
      // Видалення тимчасового файлу після завершення
      new File(decryptedFilePath).delete();
      System.out.println("Тимчасовий файл видалено.");
    }
  }
}

