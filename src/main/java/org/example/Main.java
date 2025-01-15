package org.example;

import java.sql.Connection;
import org.example.utils.DatabaseUtil;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
  public static void main(String[] args) {
    try (Connection conn = DatabaseUtil.getInstance().getConnection()) {
      System.out.println("Database connected successfully!");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}