package org.example.controllers;


import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.builder.Installer;
import org.example.entities.ConversionSettings;
import org.example.entities.InputFile;
import org.example.entities.OutputFile;
import org.example.observer.GuiObserver;
import org.example.observer.InstallationObserver;

import java.io.File;
import org.example.server.Client;
import org.example.state.Session;

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

  @FXML
  private CheckBox enableEncryptionCheckBox;

  @FXML
  private CheckBox enableCompressionCheckBox;

  @FXML
  private CheckBox createShortcutCheckBox;

  private Client client;


  public JarConverterController(){

    client =  new Client();
  }

  @FXML
  public void initialize() {
    Session.getUserState().enableEncryptionFeature(enableEncryptionCheckBox);
    Session.getUserState().enableCompressionFeature(enableCompressionCheckBox);
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
  private void handleExit(ActionEvent event) {
    // Закриває програму
    Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
    currentStage.close();
  }



  @FXML
  private void handleConvert(ActionEvent event) {
    convertButton.setDisable(true);
    boolean encryptionEnabled = enableEncryptionCheckBox.isSelected();
    boolean compressionEnambled = enableCompressionCheckBox.isSelected();
    boolean createShortcut = createShortcutCheckBox.isSelected();

    String jarFile = jarFilePath.getText();
    String saveLocation = savePath.getText();
    String format = outputFormat.getValue();
    String desiredFileName = outputFileName.getText();

    String fileExtension = format.equalsIgnoreCase("EXE") ? ".exe" : ".msi";
    String outputFilePath = saveLocation + File.separator + desiredFileName + fileExtension;

    InputFile inputFile = new InputFile(jarFilePath.getText(), InputFile.FileType.JAR);
    OutputFile outputFile =  new OutputFile(saveLocation, desiredFileName, format.equalsIgnoreCase("EXE") ? OutputFile.FileType.EXE : OutputFile.FileType.MSI);
    try {
      inputFile.validate();
      outputFile.validate();

      System.out.println("Converting...");
      System.out.println("JAR File: " + jarFile);
      System.out.println("Save Path: " + saveLocation);
      System.out.println("Output Format: " + format);
      System.out.println("Desired File Name: " + desiredFileName);


      ConversionSettings settings = new ConversionSettings();
      settings.setEnableEncryption(encryptionEnabled);
      settings.setEnableCompression(compressionEnambled);
      settings.setAddShortcut(createShortcut);
      settings.setInstallPath(saveLocation);
      outputFile =  new OutputFile(outputFilePath, desiredFileName, format.equalsIgnoreCase("EXE") ? OutputFile.FileType.EXE : OutputFile.FileType.MSI);
      Installer installer = new Installer.Builder()
          .addFile(inputFile)
          .setConversionSettings(settings)
          .setOutputFile(outputFile)
          .addObserver(new GuiObserver(progressBar, statusLabel, convertButton) )
          .build();



      int userId = Session.getUserId();
      // Відправка запиту на сервер
      String saveRequest = String.format("SAVE_FILE %d %s %s %s %s %s",
          userId, // user_id, замініть на реального користувача
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

    } catch (IllegalArgumentException e) {
      Platform.runLater(() -> {

        statusLabel.setText("Validation Error: " + e.getMessage());
        convertButton.setDisable(false);
      });
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
