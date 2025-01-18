package org.example.processor;

import java.io.File;
import java.util.function.BiConsumer;
import org.example.converter.ShortcutCreator;

public class ShortcutProcessor implements FileProcessor {
  private final FileProcessor wrapped;
  private final boolean addShortcut;
  BiConsumer<String, Integer> notifier;

  public ShortcutProcessor(FileProcessor wrapped, boolean addShortcut, BiConsumer<String, Integer> notifier) {
    this.wrapped = wrapped;
    this.addShortcut = addShortcut;
    this.notifier = notifier;
  }

  @Override
  public String process() throws Exception {
    notifier.accept("Creating shortcut...", 70);
    String outputFile = wrapped.process();
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
