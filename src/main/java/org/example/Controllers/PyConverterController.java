package org.example.Controllers;

import entities.ConversionSettings;
import entities.InputFile;
import entities.OutputFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.builder.Installer;
import org.example.builder.InstallerBuilder;
import org.example.builder.InstallerBuilderImpl;

import java.io.File;

public class PyConverterController {

  @FXML
  private TextField pyFilePath;

  @FXML
  private TextField exeSavePath;

  @FXML
  private TextField licenseKey;

  private final InstallerBuilder builder = new InstallerBuilderImpl();

  @FXML
  private void browsePyFile(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Python Files", "*.py"));
    File selectedFile = fileChooser.showOpenDialog(new Stage());
    if (selectedFile != null) {
      pyFilePath.setText(selectedFile.getAbsolutePath());
    }
  }

  @FXML
  private void browseSavePath(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialFileName("output.exe");
    File selectedFile = fileChooser.showSaveDialog(new Stage());
    if (selectedFile != null) {
      exeSavePath.setText(selectedFile.getAbsolutePath());
    }
  }

  @FXML
  private void handleConvert(ActionEvent event) {
    String pyFile = pyFilePath.getText();
    String savePath = exeSavePath.getText();
    String key = licenseKey.getText();

    if (pyFile.isEmpty() || savePath.isEmpty()) {
      System.out.println("Please select the Python file and save location.");
      return;
    }

    System.out.println("Converting...");
    System.out.println("Python File: " + pyFile);
    System.out.println("Save Path: " + savePath);

    // Створення сутностей через Builder
    InputFile inputFile = new InputFile(pyFile, InputFile.FileType.PY);
    OutputFile outputFile = new OutputFile(savePath, OutputFile.FileType.EXE);
    ConversionSettings settings = new ConversionSettings();
    settings.setLicenseKey(key);
    settings.setAddShortcut(true); // Наприклад, за замовчуванням додаємо ярлик
    settings.setInstallPath(savePath);

    builder.addFile(inputFile);
    builder.setOutputFile(outputFile);
    builder.setConversionSettings(settings);

    // Створення інсталятора і запуск
    Installer installer = builder.build();
    installer.generatePackage();

    System.out.println("Conversion completed!");
  }
}
