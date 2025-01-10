package org.example.encryption;

public interface EncryptionStrategy {
  String encrypt(String outputFile, String key) throws Exception;
  public void decrypt(String outputFile, String key) throws Exception;
}
