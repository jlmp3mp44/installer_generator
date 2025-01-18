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
    try {
      String userExistsQuery = "SELECT id FROM users WHERE username = ?";
      try (PreparedStatement userExistsStmt = connection.prepareStatement(userExistsQuery)) {
        userExistsStmt.setString(1, username);
        try (ResultSet userExistsRs = userExistsStmt.executeQuery()) {
          if (!userExistsRs.next()) {
            return "Login failed: User does not exist.";
          }
        }
      }

      String loginQuery = "SELECT id, license FROM users WHERE username = ? AND password = ?";
      try (PreparedStatement loginStmt = connection.prepareStatement(loginQuery)) {
        loginStmt.setString(1, username);
        loginStmt.setString(2, password);
        try (ResultSet loginRs = loginStmt.executeQuery()) {
          if (loginRs.next()) {
            int userId = loginRs.getInt("id");
            boolean isPremium = loginRs.getBoolean("license");
            return isPremium
                ? "Login successful - Premium User userId:" + userId
                : "Login successful - Regular User userId:" + userId;
          } else {
            return "Login failed: Incorrect password.";
          }
        }
      }
    } catch (SQLException e) {
      return "Error: " + e.getMessage();
    }
  }
}

