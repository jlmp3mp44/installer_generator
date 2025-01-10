package org.example.file_generator;


import org.example.entities.InputFile;
import org.example.entities.OutputFile;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class XmlFileGenerator implements FileGenerator {
  private final InputFile inputFile;
  private final OutputFile outputFile;

  public XmlFileGenerator(InputFile inputFile, OutputFile outputFile) {
    this.inputFile = inputFile;
    this.outputFile = outputFile;
  }

  @Override
  public String generateContent() {
    return "<launch4jConfig>\n" +
        "  <dontWrapJar>false</dontWrapJar>\n" +  // Ensure JAR wrapping is disabled
        "  <outfile>" + outputFile.getFilePath() + "</outfile>\n" +
        "  <jar>" + inputFile.getFilePath() + "</jar>\n" +
        "  <headerType>console</headerType>\n" + // Set header type to console
        "  <jre>\n" +
        "    <path>auto</path>\n" +
        "    <minVersion>11.0.0</minVersion>\n" +
        "  </jre>\n" +
        "</launch4jConfig>";
  }


  @Override
  public void exportToFile(String filePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      writer.write(generateContent());
    } catch (IOException e) {
      throw new RuntimeException("Failed to write XML file: " + filePath, e);
    }
  }
}
