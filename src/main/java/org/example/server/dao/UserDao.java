package org.example.server.dao;

import java.sql.*;

public class UserDao {
  private final Connection connection;

  public UserDao(Connection connection) {
    this.connection = connection;
  }

  public String registerUser(String username, String password, boolean license) {
    String query = "INSERT INTO users (username, password, license) VALUES (?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, username);
      stmt.setString(2, password);
      stmt.setBoolean(3, license);
      stmt.executeUpdate();
      return "User registered successfully";
    } catch (SQLException e) {
      return "Error: " + e.getMessage();
    }
  }



  public String loginUser(String username, String password) {
    String query = "SELECT id, license FROM users WHERE username = ? AND password = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, username);
      stmt.setString(2, password);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          int userId = rs.getInt("id");
          boolean isPremium = rs.getBoolean("license");
          return isPremium
              ? "Login successful - Premium User userId:" + userId
              : "Login successful - Regular User userId:" + userId;
        } else {
          return "Login failed";
        }
      }
    } catch (SQLException e) {
      return "Error: " + e.getMessage();
    }
  }

}
