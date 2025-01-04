package org.example.Controllers;

import entities.ConversionSettings;
import entities.InputFile;
import entities.OutputFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.builder.Installer;

import java.io.File;

public class PyConverterController {

  @FXML
  private TextField pyFilePath;

  @FXML
  private TextField savePath;

  @FXML
  private TextField licenseKey;

  @FXML
  private ComboBox<String> outputFormat;

  @FXML
  private void browsePyFile(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Python Files", "*.py"));
    File selectedFile = fileChooser.showOpenDialog(new Stage());
    if (selectedFile != null) {
      pyFilePath.setText(selectedFile.getAbsolutePath().replace("\\", "\\\\"));
    }
  }

  @FXML
  private void browseSavePath(ActionEvent event) {
    javafx.stage.DirectoryChooser directoryChooser = new javafx.stage.DirectoryChooser();
    directoryChooser.setTitle("Select Save Directory");
    File selectedDirectory = directoryChooser.showDialog(new Stage());
    if (selectedDirectory != null) {
      String format = outputFormat.getValue();
      String fileName = (format != null && format.equalsIgnoreCase("MSI")) ? "output.msi" : "output.exe";
      savePath.setText(selectedDirectory.getAbsolutePath().replace("\\", "\\\\") + "\\\\" + fileName);
    }
  }


  @FXML
  private void handleConvert(ActionEvent event) {
    String pyFile = pyFilePath.getText();
    String saveLocation = savePath.getText();
    String format = outputFormat.getValue();
    String key = licenseKey.getText();

    if (pyFile.isEmpty() || saveLocation.isEmpty() || format == null) {
      System.out.println("Please select the Python file, save location, and output format.");
      return;
    }

    System.out.println("Converting...");
    System.out.println("Python File: " + pyFile);
    System.out.println("Save Path: " + saveLocation);
    System.out.println("Output Format: " + format);

    InputFile inputFile = new InputFile(pyFile, InputFile.FileType.PY);
    OutputFile outputFile = new OutputFile(saveLocation, format.equalsIgnoreCase("EXE") ? OutputFile.FileType.EXE : OutputFile.FileType.MSI);
    ConversionSettings settings = new ConversionSettings();
    settings.setLicenseKey(key);
    settings.setAddShortcut(true);
    settings.setInstallPath(saveLocation);

    // Використання внутрішнього класу Builder
    Installer installer = new Installer.Builder()
        .addFile(inputFile)
        .setConversionSettings(settings)
        .setOutputFile(outputFile)
        .build();

    installer.generatePackage();

    System.out.println("Conversion completed!");
  }
}
