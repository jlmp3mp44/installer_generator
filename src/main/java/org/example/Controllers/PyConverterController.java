package org.example.Controllers;

import entities.ConversionSettings;
import entities.InputFile;
import entities.OutputFile;
import javafx.concurrent.Task;
import javafx.scene.control.CheckBox;
import javafx.stage.DirectoryChooser;
import org.example.server.Client;
import org.example.utils.Session;
import org.example.validation.DirectoryExistsHandler;
import org.example.validation.FileExistsHandler;
import org.example.validation.FileFormatHandler;
import org.example.validation.NotEmptyHandler;
import org.example.validation.ValidationHandler;
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
  private TextField outputFileName;

  @FXML
  private CheckBox enableEncryptionCheckBox; // Додано чекбокс для вибору шифрування


  private Client client;

  // Ланцюжки валідації для різних полів
  private final ValidationHandler pyFileValidator;
  private final ValidationHandler savePathValidator;
  //private final ValidationHandler licenseKeyValidator;

  public PyConverterController() {
    // Створення ланцюжків валідації
    pyFileValidator = new NotEmptyHandler();
    pyFileValidator.setNext(new FileFormatHandler(".py"))
        .setNext(new FileExistsHandler());

    savePathValidator = new NotEmptyHandler();
    savePathValidator.setNext(new DirectoryExistsHandler());

    client =  new Client();
   // licenseKeyValidator = new NotEmptyHandler();
   // licenseKeyValidator.setNext(new LicenseKeyFormatHandler());
  }

  @FXML
  public void initialize() {
    // Налаштовуємо GUI відповідно до поточного стану
    if (Session.getUserState().isPremium()) {
      enableEncryptionCheckBox.setDisable(false);
    } else {
      enableEncryptionCheckBox.setDisable(true);
    }
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
  private void handleConvert(ActionEvent event) {
    boolean encryptionEnabled = enableEncryptionCheckBox.isSelected();

    // Зробити кнопку недоступною
    convertButton.setDisable(true);
    statusLabel.setText("Initializing conversion...");

    try {
      // Валідація полів
      pyFileValidator.validate("Python File Path", pyFilePath.getText());
      savePathValidator.validate("Save Path", savePath.getText());
      if (outputFileName.getText() == null || outputFileName.getText().trim().isEmpty()) {
        throw new IllegalArgumentException("Output file name cannot be empty.");
      }

      // Підготовка до конвертації
      String pyFile = pyFilePath.getText();
      String saveDirectory = savePath.getText();
      String fileName = outputFileName.getText().trim();
      String format = outputFormat.getValue();

      if (format == null || format.isEmpty()) {
        throw new IllegalArgumentException("Output format must be selected.");
      }

      String fileExtension = format.equalsIgnoreCase("EXE") ? ".exe" : ".msi";
      String outputFilePath = saveDirectory + File.separator + fileName + fileExtension;

      File saveDir = new File(saveDirectory);
      if (!saveDir.exists() || !saveDir.isDirectory()) {
        throw new IllegalArgumentException("Save path must be a valid directory.");
      }

      InputFile inputFile = new InputFile(pyFile, InputFile.FileType.PY);
      OutputFile outputFile = new OutputFile(outputFilePath, format.equalsIgnoreCase("EXE") ? OutputFile.FileType.EXE : OutputFile.FileType.MSI);
      ConversionSettings settings = new ConversionSettings();
      settings.setEnableEncryption(encryptionEnabled);
      settings.setAddShortcut(true);
      settings.setInstallPath(saveDirectory);

      Installer installer = new Installer.Builder()
          .addFile(inputFile)
          .setConversionSettings(settings)
          .setOutputFile(outputFile)
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

      // Використання Task для асинхронного виконання
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
          Platform.runLater(() -> {
            statusLabel.setText("Conversion completed successfully!");
            convertButton.setDisable(false);
          });
        }
      });

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
