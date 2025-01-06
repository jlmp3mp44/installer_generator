package org.example.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryptionStrategy implements EncryptionStrategy {

  @Override
  public void encrypt(String inputFile, String outputFile, String key) throws Exception {
    SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    File input = new File(inputFile);
    if (!input.exists() || input.length() == 0) {
      throw new RuntimeException("Вхідний файл для шифрування не існує або порожній: " + inputFile);
    }

    try (InputStream is = new FileInputStream(inputFile);
        OutputStream os = new FileOutputStream(outputFile)) {
      byte[] buffer = new byte[16];
      int bytesRead;
      while ((bytesRead = is.read(buffer)) != -1) {
        if (bytesRead < 16) {
          Arrays.fill(buffer, bytesRead, 16, (byte) 0); // Доповнення
        }
        byte[] encrypted = cipher.doFinal(buffer);
        os.write(encrypted);
      }
    } catch (Exception e) {
      System.err.println("Помилка під час шифрування: " + e.getMessage());
      throw e;
    }
  }


  @Override
  public void decrypt(String inputFile, String outputFile, String key) throws Exception {
    SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // Рекомендується PKCS5Padding
    cipher.init(Cipher.DECRYPT_MODE, secretKey);

    try (InputStream is = new FileInputStream(inputFile);
        OutputStream os = new FileOutputStream(outputFile)) {
      byte[] buffer = new byte[16]; // Розмір блоку AES
      int bytesRead;
      while ((bytesRead = is.read(buffer)) != -1) {
        byte[] decrypted = cipher.doFinal(Arrays.copyOf(buffer, bytesRead));
        os.write(decrypted);
      }
    } catch (Exception e) {
      System.err.println("Помилка під час розшифрування: " + e.getMessage());
      throw e;
    }
  }


}

