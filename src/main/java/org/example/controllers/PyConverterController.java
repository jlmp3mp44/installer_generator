package org.example.controllers;


import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import org.example.entities.ConversionSettings;
import org.example.entities.InputFile;
import org.example.entities.OutputFile;
import org.example.observer.GuiObserver;
import org.example.processor.EncryptionStrategy;
import org.example.processor.EncryptionStrategyFactory;
import org.example.server.Client;
import org.example.state.Session;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.builder.Installer;
import org.example.observer.InstallationObserver;

import java.io.File;

public class PyConverterController {

  @FXML
  private TextField pyFilePath;

  @FXML
  private TextField savePath;

  @FXML
  private ComboBox<String> outputFormat;

  @FXML
  private ProgressBar progressBar;

  @FXML
  private Label statusLabel;

  @FXML
  private Button convertButton;

  @FXML
  private TextField outputFileName;

  @FXML
  private CheckBox enableEncryptionCheckBox;

  @FXML
  private CheckBox enableCompressionCheckBox;

  @FXML
  private CheckBox createShortcutCheckBox;

  @FXML
  private ComboBox<String> encryptionMethod;


  private Client client;


  public PyConverterController() {

    client =  new Client();

  }

  @FXML
  private void initialize() {
    // Ініціалізація залежності стану елемента `encryptionMethod` від стану `enableEncryptionCheckBox`
      enableEncryptionCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
      encryptionMethod.setDisable(!newValue); // Включити ComboBox, якщо чекбокс обраний
    });

    // Ініціалізація інших чекбоксів
    Session.getUserState().enableEncryptionFeature(enableEncryptionCheckBox);
    Session.getUserState().enableCompressionFeature(enableCompressionCheckBox);
    progressBar.setProgress(0.0);

  }



  @FXML
  private void browsePyFile(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Python Files", "*.py"));
    File initialDirectory = new File("D:\\trpz_courcework\\test"); // Вкажіть директорію
    fileChooser.setInitialDirectory(initialDirectory);
    File selectedFile = fileChooser.showOpenDialog(new Stage());
    if (selectedFile != null) {
      pyFilePath.setText(selectedFile.getAbsolutePath().replace("\\", "/"));
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
  private void handleExit(ActionEvent event) {
    // Закриває програму
    Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
    currentStage.close();
  }

  @FXML
  private void handleConvert(ActionEvent event) {
    boolean encryptionEnabled = enableEncryptionCheckBox.isSelected();
    boolean compressionEnambled = enableCompressionCheckBox.isSelected();
    boolean createShortcut = createShortcutCheckBox.isSelected();

    String encryptionAlgorithm = encryptionEnabled ? encryptionMethod.getValue() : null;

    if (encryptionEnabled && (encryptionAlgorithm == null || encryptionAlgorithm.isEmpty())) {
      statusLabel.setText("Please select an encryption method.");
      return;
    }

    EncryptionStrategy encryptionStrategy = null;
    if (encryptionEnabled) {
      try {
        encryptionStrategy = EncryptionStrategyFactory.getStrategy(encryptionAlgorithm);
      } catch (IllegalArgumentException e) {
        statusLabel.setText("Invalid encryption method selected.");
        return;
      }
    }


    // Зробити кнопку недоступною
    convertButton.setDisable(true);
    statusLabel.setText("Initializing conversion...");

    try {

      if (outputFileName.getText() == null || outputFileName.getText().trim().isEmpty()) {
        throw new IllegalArgumentException("Output file name cannot be empty.");
      }

      // Підготовка до конвертації
      String pyFile = pyFilePath.getText();
      String saveDirectory = savePath.getText();
      String fileName = outputFileName.getText().trim();
      String format = outputFormat.getValue();
      String fileExtension = format.equalsIgnoreCase("EXE") ? ".exe" : ".msi";
      String outputFilePath = saveDirectory + File.separator + fileName + fileExtension;

      InputFile inputFile = new InputFile(pyFile, InputFile.FileType.PY);
      inputFile.validate();
      OutputFile outputFile = new OutputFile(saveDirectory, fileName, format.equalsIgnoreCase("EXE") ? OutputFile.FileType.EXE : OutputFile.FileType.MSI);
      outputFile.validate();


      ConversionSettings settings = new ConversionSettings();
      settings.setEnableEncryption(encryptionEnabled);
      settings.setEnableCompression(compressionEnambled);
      settings.setAddShortcut(createShortcut);
      settings.setAddShortcut(true);
      settings.setEncryptionStrategy(encryptionAlgorithm);
      settings.setInstallPath(saveDirectory);
      outputFile = new OutputFile(outputFilePath, fileName, format.equalsIgnoreCase("EXE") ? OutputFile.FileType.EXE : OutputFile.FileType.MSI);
      Installer installer = new Installer.Builder()
          .addFile(inputFile)
          .setConversionSettings(settings)
          .setOutputFile(outputFile)
          .addObserver(new GuiObserver(progressBar, statusLabel, convertButton) )
          .build();

      String saveRequest = String.format("SAVE_FILE %d %s %s %s %s %s",
          1, // user_id, замініть на реального користувача
          inputFile.getFilePath(),
          inputFile.getFileType().name(),
          outputFile.getFilePath(),
          outputFile.getFileType().name(),
          outputFile.getIcon() != null ? outputFile.getIcon() : "NULL");

      String response = client.sendRequest(saveRequest);

      if (!response.equals("File saved successfully")) {
        statusLabel.setText("Error: " + response);
        convertButton.setDisable(false);
        return;
      }
      new Thread(installer::generatePackage).start();
    } catch (IllegalArgumentException e) {
      // Відображення повідомлення про помилку
      statusLabel.setText("Validation Error: " + e.getMessage());
      convertButton.setDisable(false); // Знову зробити кнопку доступною
    }
  }

  @FXML
  private void goToWelcome(ActionEvent event) {
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
