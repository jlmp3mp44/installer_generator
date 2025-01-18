package org.example.processor;

public class EncryptionStrategyFactory {
  public static EncryptionStrategy getStrategy(String type) {
    switch (type.toUpperCase()) {
      case "AES":
        return new AESEncryptionStrategy();
      case "BLOWFISH":
        return new BlowfishEncryptionStrategy();
      default:
        throw new IllegalArgumentException("Unsupported encryption type: " + type);
    }
  }
}
