package org.example.server;

import java.io.*;
import java.net.*;
import java.sql.*;
import org.example.server.dao.FileDao;
import org.example.server.dao.LicenseDao;
import org.example.server.dao.UserDao;
import org.example.utils.DatabaseUtil;

public class Server {

  private static final int PORT = 8080;
  private static Connection connection;


  public static void main(String[] args) {
    try {
      initializeDatabase();

      ServerSocket serverSocket = new ServerSocket(PORT);
      System.out.println("Server started on port " + PORT);

      while (true) {
        Socket clientSocket = serverSocket.accept();
        System.out.println("New client connected: " + clientSocket.getInetAddress());

        new ClientHandler(clientSocket).start();
      }
    } catch (IOException | SQLException e) {
      e.printStackTrace();
    }
  }

  private static void initializeDatabase() throws IOException, SQLException {
    connection = DatabaseUtil.getInstance().getConnection();
    System.out.println("Database connected.");
  }

  static class ClientHandler extends Thread {
    private Socket socket;
    private UserDao userDao;
    private LicenseDao licenseDao;
    private FileDao fileDao;

    public ClientHandler(Socket socket) throws IOException, SQLException {
      this.socket = socket;
      Connection connection = DatabaseUtil.getInstance().getConnection();
      this.userDao = new UserDao(connection);
      this.licenseDao = new LicenseDao(connection);
      this.fileDao = new FileDao(connection);
    }


    @Override
    public void run() {
      try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

        String request = in.readLine();
        System.out.println("Received request: " + request);

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
          if (parts.length == 4) {
            boolean license = Boolean.parseBoolean(parts[3]);
            return userDao.registerUser(parts[1], parts[2], license);
          }
            break;
          case "LOGIN":
            if (parts.length == 3) {
              return userDao.loginUser(parts[1], parts[2]);
            }
            break;
          case "VALIDATE_LICENSE":
            if (parts.length == 2) {
              return licenseDao.isValidLicenseKey(parts[1]);
            }
            break;
          case "SAVE_FILE":
            if (parts.length == 7) {
              return fileDao.saveFile(parts);
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
  }
}