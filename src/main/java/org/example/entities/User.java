package org.example.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.example.validation.MinLengthHandler;
import org.example.validation.NotEmptyHandler;
import org.example.validation.UsernameFormatHandler;
import org.example.validation.ValidationHandler;

public class User {
  private String username;
  private String password;
  private boolean license;
  private final ValidationHandler usernameValidator = new NotEmptyHandler().setNext(new UsernameFormatHandler());
  private final ValidationHandler passwordValidator =  new NotEmptyHandler().setNext(new MinLengthHandler(6));


  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public User(String username, String password, boolean license) {
    this.username = username;
    this.password = password;
    this.license = false;
  }

  public void validate(){
    usernameValidator.validate("Username", username);
    passwordValidator.validate("Password", password);
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

  public boolean isLicense() {
    return license;
  }

  public void setLicense(boolean license) {
    this.license = license;
  }
}
