package org.example.utils;


import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseUtil {
  private static Connection connection;

  public static Connection getConnection() throws IOException, SQLException {
    if (connection == null || connection.isClosed()) {
      Properties props = new Properties();
      try (FileInputStream fis = new FileInputStream("D:\\trpz_courcework\\installer_generator\\src\\main\\resources\\db.properties")) {
        props.load(fis);
      }
      String url = props.getProperty("db.url");
      String username = props.getProperty("db.username");
      String password = props.getProperty("db.password");
      String driver = props.getProperty("db.driver");

      try {
        Class.forName(driver);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException("Driver not found", e);
      }

      connection = DriverManager.getConnection(url, username, password);
    }
    return connection;
  }
}

