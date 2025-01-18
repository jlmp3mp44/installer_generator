package org.example.server.dao;

import java.sql.*;

public class LicenseDao {
  private final Connection connection;
  public LicenseDao(Connection connection) {
    this.connection = connection;
  }
  public String isValidLicenseKey(String licenseKey) {
    String query = "SELECT COUNT(*) FROM license_keys WHERE license_key = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, licenseKey);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next() && rs.getInt(1) > 0) {
          return "VALID_LICENSE";
        } else {
          return "INVALID_LICENSE";
        }
      }
    } catch (SQLException e) {
      return "Error: " + e.getMessage();
    }
  }

}

