import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.server.Server;

public class Start extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    // Завантаження FXML для сторінки авторизації
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/login.fxml"));
    primaryStage.setScene(new Scene(loader.load()));
    primaryStage.setTitle("Сторінка авторизації");
    primaryStage.show();

    // Запуск сервера в окремому потоці
    new Thread(() -> {
      try {
        // Стартуємо сервер
        Server.main(new String[]{});  // Запуск методу main з вашого серверного класу
      } catch (Exception e) {
        e.printStackTrace();
      }
    }).start();  // Запуск потоку
  }

  public static void main(String[] args) {
    launch(args);
  }
}
