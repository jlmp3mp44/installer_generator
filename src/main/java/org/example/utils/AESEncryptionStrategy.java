package org.example.utils;

public class AESEncryptionStrategy implements EncryptionStrategy {
  @Override
  public void encrypt(String inputFile, String outputFile, String key) throws Exception {
    SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    byte[] fileBytes = Files.readAllBytes(new File(inputFile).toPath());
    byte[] encryptedBytes = cipher.doFinal(fileBytes);

    Files.write(new File(outputFile).toPath(), encryptedBytes);
  }
}
