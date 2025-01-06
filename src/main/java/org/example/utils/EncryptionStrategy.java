package org.example.utils;

public interface EncryptionStrategy {
  void encrypt(String inputFile, String outputFile, String key) throws Exception;
  public void decrypt(String inputFile, String outputFile, String key) throws Exception;
}
