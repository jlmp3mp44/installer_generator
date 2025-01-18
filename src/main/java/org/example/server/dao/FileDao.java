package org.example.server.dao;

import java.sql.*;

public class FileDao {
  private final Connection connection;
  public FileDao(Connection connection) {
    this.connection = connection;
  }

  public String saveFile(String[] parts) {
    String query = "INSERT INTO files (user_id, input_file_path, input_file_type, output_file_path, output_file_type) " +
        "VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, Integer.parseInt(parts[1]));
      stmt.setString(2, parts[2]);
      stmt.setString(3, parts[3]);
      stmt.setString(4, parts[4]);
      stmt.setString(5, parts[5]);
      stmt.executeUpdate();
      return "File saved successfully";
    } catch (SQLException e) {
      return "Error: " + e.getMessage();
    }
  }

  public void updateInstallationResult(int fileId, String status, String errorMessage) {
    String query = "UPDATE files SET status = ?, error_message = ? WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, status);
      stmt.setString(2, errorMessage);
      stmt.setInt(3, fileId);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}

