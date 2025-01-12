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
      stmt.setInt(1, Integer.parseInt(parts[1])); // user_id
      stmt.setString(2, parts[2]); // input_file_path
      stmt.setString(3, parts[3]); // input_file_type
      stmt.setString(4, parts[4]); // output_file_path
      stmt.setString(5, parts[5]); // output_file_type
      stmt.executeUpdate();
      return "File saved successfully";
    } catch (SQLException e) {
      return "Error: " + e.getMessage();
    }
  }
}

