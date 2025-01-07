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
import org.example.server.Client;
import org.example.validation.DirectoryExistsHandler;
import org.example.validation.FileExistsHandler;
import org.example.validation.FileFormatHandler;
import org.example.validation.NotEmptyHandler;
import org.example.validation.ValidationHandler;

public class JarConverterController {

  @FXML
  private TextField jarFilePath;

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

  private Client client;

  private final ValidationHandler jarFileValidator;
  private final ValidationHandler savePathValidator;

  public JarConverterController(){

    jarFileValidator = new NotEmptyHandler();
    jarFileValidator.setNext(new FileFormatHandler(".jar"))
        .setNext(new FileExistsHandler());

    savePathValidator = new NotEmptyHandler();
    savePathValidator.setNext(new DirectoryExistsHandler());
    client =  new Client();
  }

  @FXML
  private void browseJarFile(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JAR Files", "*.jar"));
    File initialDirectory = new File("D:\\trpz_courcework\\test"); // Вкажіть директорію
    fileChooser.setInitialDirectory(initialDirectory);
    File selectedFile = fileChooser.showOpenDialog(new Stage());
    if (selectedFile != null) {
      jarFilePath.setText(selectedFile.getAbsolutePath());
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

    try {
      // Валідація полів
      jarFileValidator.validate("Jar File Path", jarFilePath.getText());
      savePathValidator.validate("Save Path", savePath.getText());
      if (outputFileName.getText() == null || outputFileName.getText().trim().isEmpty()) {
        throw new IllegalArgumentException("Output file name cannot be empty.");
      }

      String jarFile = jarFilePath.getText();
      String saveLocation = savePath.getText();
      String format = outputFormat.getValue();
      String desiredFileName = outputFileName.getText();

      if (jarFile.isEmpty() || saveLocation.isEmpty() || format == null
          || desiredFileName.isEmpty()) {
        System.out.println("Please fill in all required fields.");
        convertButton.setDisable(false);
        return;
      }

      System.out.println("Converting...");
      System.out.println("JAR File: " + jarFile);
      System.out.println("Save Path: " + saveLocation);
      System.out.println("Output Format: " + format);
      System.out.println("Desired File Name: " + desiredFileName);

      // Додаємо розширення до імені файлу
      String fileExtension = format.equalsIgnoreCase("EXE") ? ".exe" : ".msi";
      String outputFilePath = saveLocation + File.separator + desiredFileName + fileExtension;

      InputFile inputFile = new InputFile(jarFile, InputFile.FileType.JAR);
      OutputFile outputFile = new OutputFile(outputFilePath,
          format.equalsIgnoreCase("EXE") ? OutputFile.FileType.EXE : OutputFile.FileType.MSI);
      ConversionSettings settings = new ConversionSettings();
      settings.setAddShortcut(true);
      settings.setInstallPath(saveLocation);

      Installer installer = new Installer.Builder()
          .addFile(inputFile)
          .setConversionSettings(settings)
          .setOutputFile(outputFile)
          .build();

      // Відправка запиту на сервер
      String saveRequest = String.format("SAVE_FILE %d %s %s %s %s %s",
          1, // user_id, замініть на реального користувача
          inputFile.getFilePath(),
          inputFile.getFileType().name(),
          outputFile.getFilePath(),
          outputFile.getFileType().name(),
          outputFile.getIcon() != null ? outputFile.getIcon() : "NULL");

      String response = client.sendRequest(saveRequest);

      // Обробка відповіді сервера
      if (!response.equals("File saved successfully")) {
        statusLabel.setText("Error: " + response);
        convertButton.setDisable(false);
        return;
      }

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
