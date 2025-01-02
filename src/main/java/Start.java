import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Start extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/welcome.fxml"));
    primaryStage.setScene(new Scene(loader.load()));
    primaryStage.setTitle("Сторінка вітання");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
