package org.example.Controllers;

import entities.ConversionSettings;
import entities.InputFile;
import entities.OutputFile;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.builder.Installer;
import org.example.observer.InstallationObserver;

import java.io.File;

public class JarConverterController {

  @FXML
  private TextField jarFilePath;

  @FXML
  private TextField savePath;

  @FXML
  private TextField licenseKey;

  @FXML
  private ComboBox<String> outputFormat;

  @FXML
  private ProgressBar progressBar;

  @FXML
  private Label statusLabel;

  @FXML
  private Button convertButton;


  @FXML
  private void browseJarFile(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JAR Files", "*.jar"));
    File initialDirectory = new File("D:\\trpz_courcework\\test"); // Вкажіть директорію
    fileChooser.setInitialDirectory(initialDirectory);
    File selectedFile = fileChooser.showOpenDialog(new Stage());
    if (selectedFile != null) {
      jarFilePath.setText(selectedFile.getAbsolutePath().replace("\\", "\\\\"));
    }
  }


  @FXML
  private void browseSavePath(ActionEvent event) {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Save Directory");
    File initialDirectory = new File("D:\\trpz_courcework\\test"); // Вкажіть директорію
    directoryChooser.setInitialDirectory(initialDirectory);
    File selectedDirectory = directoryChooser.showDialog(new Stage());
    if (selectedDirectory != null) {
      // Встановлюємо тільки шлях до директорії
      savePath.setText(selectedDirectory.getAbsolutePath());
    }
  }



  @FXML
  private void handleConvert(ActionEvent event) {
    convertButton.setDisable(true);

    String jarFile = jarFilePath.getText();
    String saveLocation = savePath.getText();
    String format = outputFormat.getValue();
    String key = licenseKey.getText();

    if (jarFile.isEmpty() || saveLocation.isEmpty() || format == null) {
      System.out.println("Please select the JAR file, save location, and output format.");
      convertButton.setDisable(false);
      return;
    }

    System.out.println("Converting...");
    System.out.println("JAR File: " + jarFile);
    System.out.println("Save Path: " + saveLocation);
    System.out.println("Output Format: " + format);

    InputFile inputFile = new InputFile(jarFile, InputFile.FileType.JAR);
    String inputFileName = new File(jarFile).getName();
    String outputFileName = inputFileName.substring(0, inputFileName.lastIndexOf(".")) + (format.equalsIgnoreCase("EXE") ? ".exe" : ".msi");
    String outputFilePath = saveLocation + File.separator + outputFileName;

// Створення об'єкта OutputFile
    OutputFile outputFile = new OutputFile(outputFilePath, format.equalsIgnoreCase("EXE") ? OutputFile.FileType.EXE : OutputFile.FileType.MSI);
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

    // Додаємо спостерігач для оновлення GUI
    installer.addObserver(new InstallationObserver() {
      @Override
      public void onProgressUpdate(String message, int progressPercentage) {
        Platform.runLater(() -> {
          statusLabel.setText(message);
          if (progressPercentage >= 0) {
            progressBar.setProgress(progressPercentage / 100.0);
          }
        });
      }

      @Override
      public void onCompletion() {
        // Після завершення процесу зробити кнопку доступною
        Platform.runLater(() -> convertButton.setDisable(false));
      }
    });
    new Thread(installer::generatePackage).start();
  }

  @FXML
  private void goToWelcome(ActionEvent event) {
    // Логіка для переходу на головну сторінку
    // Це може бути, наприклад, завантаження іншої сцени, або очищення поточного вікна
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/welcome.fxml"));
    Parent root = null;
    try {
      root = loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Scene scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
  }

}
