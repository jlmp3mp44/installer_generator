package org.example.processor;

import java.io.File;
import org.example.converter.ShortcutCreator;

public class ShortcutProcessor implements FileProcessor {
  private final FileProcessor wrapped;
  private final boolean addShortcut;

  public ShortcutProcessor(FileProcessor wrapped, boolean addShortcut) {
    this.wrapped = wrapped;
    this.addShortcut = addShortcut;
  }

  @Override
  public String process() throws Exception {
    // Спочатку виконуємо обгортковий процес
    String outputFile = wrapped.process();

    // Додаємо логіку створення ярлика
    if (addShortcut) {
      createShortcut(outputFile, getOutputFileNameWithoutExtension(outputFile));
    }

    return outputFile;
  }

  private void createShortcut(String targetPath, String shortcutName) throws Exception {
    String desktopPath = System.getProperty("user.home") + "/OneDrive/Desktop/" + shortcutName + ".lnk";
    ShortcutCreator.createShortcut(targetPath, desktopPath, "Shortcut for " + shortcutName);
    System.out.println("Ярлик створено: " + desktopPath);
  }

  private String getOutputFileNameWithoutExtension(String outputFile) {
    String fileName = new File(outputFile).getName();
    int dotIndex = fileName.lastIndexOf('.');
    return (dotIndex > 0) ? fileName.substring(0, dotIndex) : fileName;
  }
}
