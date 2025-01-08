package org.example.server;

import java.io.*;
import java.net.*;
import java.sql.*;
import org.example.utils.DatabaseUtil;

public class Server {

  private static final int PORT = 8080;
  private static Connection connection;

  public static void main(String[] args) {
    try {
      // Initialize database connection
      initializeDatabase();

      // Start server socket
      ServerSocket serverSocket = new ServerSocket(PORT);
      System.out.println("Server started on port " + PORT);

      while (true) {
        Socket clientSocket = serverSocket.accept();
        System.out.println("New client connected: " + clientSocket.getInetAddress());

        // Handle client request in a new thread
        new ClientHandler(clientSocket).start();
      }
    } catch (IOException | SQLException e) {
      e.printStackTrace();
    }
  }

  private static void initializeDatabase() throws IOException, SQLException {
    connection = DatabaseUtil.getConnection();
    System.out.println("Database connected.");
  }

  static class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
      this.socket = socket;
    }

    @Override
    public void run() {
      try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

        String request = in.readLine();
        System.out.println("Received request: " + request);

        // Process request
        String response = handleRequest(request);
        out.println(response);

      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          socket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    private String handleRequest(String request) {
      try {
        String[] parts = request.split(" ");
        switch (parts[0]) {
          case "REGISTER":
          // Перевірка наявності параметра `license`
          if (parts.length == 4) { // username, password, license
            boolean license = Boolean.parseBoolean(parts[3]); // Конвертуємо в boolean
            return registerUser(parts[1], parts[2], license);
          }
            break;
          case "LOGIN":
            if (parts.length == 3) {
              return loginUser(parts[1], parts[2]);
            }
            break;
          case "VALIDATE_LICENSE":
            if (parts.length == 2) {
              return isValidLicenseKey(parts[1]);
            }
            break;
          case "SAVE_FILE":
            if (parts.length == 7) {
              return saveFile(parts);
            }
            break;
          default:
            return "Invalid request";
        }
      } catch (Exception e) {
        return "Error: " + e.getMessage();
      }
      return "Invalid request format";
    }

    private String loginUser(String username, String password) throws SQLException {
      String query = "SELECT * FROM users WHERE username = ? AND password = ?";
      try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, username);
        stmt.setString(2, password);
        try (ResultSet rs = stmt.executeQuery()) {
          if (rs.next()) {
            // Перевірка статусу преміум
            boolean isPremium = rs.getBoolean("license"); // Перевірка преміум статусу
            return isPremium ? "Login successful - Premium User" : "Login successful - Regular User";
          } else {
            return "Login failed";
          }
        }
      }
    }


    private String isValidLicenseKey(String licenseKey) {
      String query = "SELECT COUNT(*) FROM license_keys WHERE license_key = ?";
      try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, licenseKey);
        try (ResultSet resultSet = statement.executeQuery()) {
          if (resultSet.next() && resultSet.getInt(1) > 0) {
            return "VALID_LICENSE";
          }
        }
      } catch (SQLException e) {
        e.printStackTrace();
        return "ERROR: " + e.getMessage();
      }
      return "INVALID_LICENSE";
    }




    private String registerUser(String username, String password, boolean license) throws SQLException {
      String query = "INSERT INTO users (username, password, license) VALUES (?, ?, ?)";
      try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, username);
        stmt.setString(2, password);
        stmt.setBoolean(3, license);
        stmt.executeUpdate();
        return "User registered successfully";
      }
    }

    private String saveFile(String[] parts) throws SQLException {
      String query = "INSERT INTO files (user_id, input_file_path, input_file_type, output_file_path, output_file_type, icon) " +
          "VALUES (?, ?, ?, ?, ?, ?)";
      try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, Integer.parseInt(parts[1])); // user_id
        stmt.setString(2, parts[2]); // input_file_path
        stmt.setString(3, parts[3]); // input_file_type
        stmt.setString(4, parts[4]); // output_file_path
        stmt.setString(5, parts[5]); // output_file_type
        stmt.setString(6, parts[6]); // icon
        stmt.executeUpdate();
        return "File saved successfully";
      }
    }

    private boolean isPremiumUser(String username) throws SQLException {
      String query = "SELECT license FROM users WHERE username = ?";
      try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, username);
        try (ResultSet rs = stmt.executeQuery()) {
          if (rs.next()) {
            return rs.getBoolean("license");
          }
        }
      }
      return false;
    }

  }
}