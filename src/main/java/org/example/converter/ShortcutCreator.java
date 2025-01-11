package org.example.converter;

import java.io.File;

public class ShortcutCreator {

  public static void createShortcut(String targetPath, String shortcutPath, String description) throws Exception {
    // Windows Script Host для створення ярлика
    String vbsScript =
        "Set objShell = CreateObject(\"WScript.Shell\")\n" +
            "Set objShortcut = objShell.CreateShortcut(\"" + shortcutPath + "\")\n" +
            "objShortcut.TargetPath = \"" + targetPath + "\"\n" +
            "objShortcut.Description = \"" + description + "\"\n" +
            "objShortcut.WorkingDirectory = \"" + new File(targetPath).getParent() + "\"\n" +
            "objShortcut.Save";

    // Тимчасовий файл для збереження скрипту
    File tempVbsFile = File.createTempFile("shortcut", ".vbs");
    try {
      java.nio.file.Files.writeString(tempVbsFile.toPath(), vbsScript);

      // Виконання скрипту
      Process process = new ProcessBuilder("cscript", "//NoLogo", tempVbsFile.getAbsolutePath()).start();
      process.waitFor();
    } finally {
      tempVbsFile.delete();
    }
  }
}
