package org.example.file_generator;

import org.example.entities.InputFile;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WixFileGenerator implements FileGenerator {
  private final InputFile inputFile;

  public WixFileGenerator(InputFile inputFile) {
    this.inputFile = inputFile;
  }

  @Override
  public String generateContent() {
    return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<Wix xmlns=\"http://schemas.microsoft.com/wix/2006/wi\">\n" +
        "  <Product Id=\"*\" Name=\"MyApp\" Language=\"1033\" Version=\"1.0.0.0\" Manufacturer=\"Example Co\" UpgradeCode=\"6c091c65-a4be-4ea9-b901-69c60878b1f3\">\n" +
        "    <Package InstallerVersion=\"200\" Compressed=\"yes\" InstallScope=\"perMachine\"/>\n" +
        "    <MediaTemplate EmbedCab=\"yes\"/>\n" +
        "    <Directory Id=\"TARGETDIR\" Name=\"SourceDir\">\n" +
        "      <Directory Id=\"ProgramFilesFolder\">\n" +
        "        <Directory Id=\"INSTALLDIR\" Name=\"MyApp\">\n" +
        "          <Component Id=\"MainExecutable\" Guid=\"6c091c65-a4be-4ea9-b901-69c60878b1f3\">\n" +
        "            <File Id=\"MAIN_FILE\" Source=\"" + inputFile.getFilePath().replace("\\", "/") + "\" KeyPath=\"yes\"/>\n" +
        "          </Component>\n" +
        "        </Directory>\n" +
        "      </Directory>\n" +
        "    </Directory>\n" +
        "    <Feature Id=\"MainFeature\" Title=\"Main Feature\" Level=\"1\">\n" +
        "      <ComponentRef Id=\"MainExecutable\"/>\n" +
        "    </Feature>\n" +
        "  </Product>\n" +
        "</Wix>";
  }

  @Override
  public void exportToFile(String filePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      writer.write(generateContent());
    } catch (IOException e) {
      throw new RuntimeException("Failed to write WiX file: " + filePath, e);
    }
  }
}
