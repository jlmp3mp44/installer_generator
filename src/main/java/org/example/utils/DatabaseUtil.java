package org.example.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseUtil {
  private static DatabaseUtil instance;
  private Connection connection;

  private DatabaseUtil() throws IOException, SQLException {
    initializeConnection();
  }

  public static synchronized DatabaseUtil getInstance() throws IOException, SQLException {
    if (instance == null) {
      instance = new DatabaseUtil();
    }
    return instance;
  }

  public Connection getConnection() {
    return connection;
  }

  private void initializeConnection() throws IOException, SQLException {
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
}
