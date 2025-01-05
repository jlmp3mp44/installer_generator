package org.example.server;

import java.io.*;
import java.net.*;

public class Client {

  private static final String SERVER_ADDRESS = "localhost";
  private static final int SERVER_PORT = 8080;

  public static void main(String[] args) {
    try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
      Client client = new Client();
      while (true) {
        System.out.print("Enter command: ");
        String command = console.readLine();
        if (command.equalsIgnoreCase("exit")) {
          System.out.println("Exiting...");
          break;
        }

        String response = client.sendRequest(command);
        System.out.println("Server response: " + response);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String sendRequest(String request) {
    try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

      out.println(request);
      return in.readLine(); // Read server response
    } catch (IOException e) {
      e.printStackTrace();
      return "Error: Unable to connect to server.";
    }
  }
}
