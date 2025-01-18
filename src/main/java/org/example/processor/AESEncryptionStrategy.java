package org.example.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryptionStrategy implements EncryptionStrategy {

  @Override
  public String encrypt(String filePath, String key) throws Exception {
    SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    String encryptedFilePath = filePath + ".enc";

    try (InputStream is = new FileInputStream(filePath);
        OutputStream os = new FileOutputStream(encryptedFilePath)) {
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = is.read(buffer)) != -1) {
        byte[] encrypted = cipher.update(buffer, 0, bytesRead);
        if (encrypted != null) {
          os.write(encrypted);
        }
      }
      byte[] finalBlock = cipher.doFinal();
      if (finalBlock != null) {
        os.write(finalBlock);
      }
    }

    File originalFile = new File(filePath);
    File encryptedFile = new File(encryptedFilePath);

    if (originalFile.delete()) {
      System.out.println("Оригінальний файл видалено: " + filePath);
    }
    System.out.println("Файл успішно зашифровано: " + encryptedFilePath);

    return encryptedFilePath;
  }

  @Override
  public void decrypt(String filePath, String key) throws Exception {
    SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, secretKey);

    String decryptedFilePath = filePath.replace(".enc", "");

    try (InputStream is = new FileInputStream(filePath);
        OutputStream os = new FileOutputStream(decryptedFilePath)) {
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = is.read(buffer)) != -1) {
        byte[] decrypted = cipher.update(buffer, 0, bytesRead);
        if (decrypted != null) {
          os.write(decrypted);
        }
      }
      byte[] finalBlock = cipher.doFinal();
      if (finalBlock != null) {
        os.write(finalBlock);
      }
    }

    File originalFile = new File(filePath);
    File decryptedFile = new File(decryptedFilePath);

    if (originalFile.delete()) {
      System.out.println("Зашифрований файл видалено: " + filePath);
    }
    System.out.println("Файл успішно розшифровано: " + decryptedFilePath);
  }
}
