package org.example.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class User {
  private String username;
  private String password;

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  // Save user to database
  public boolean save(Connection connection) throws SQLException {
    String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
    System.out.println("Executing query: " + sql);
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, username);
      stmt.setString(2, password);
      int rows = stmt.executeUpdate();
      System.out.println("Rows affected: " + rows);
      return rows > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

}
