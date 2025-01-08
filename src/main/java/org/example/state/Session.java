package org.example.state;

import org.example.builder.Installer;
import org.example.state.BasicState;
import org.example.state.PremiumState;
import org.example.state.UserState;

public class Session {

  private static UserState userState = new BasicState();
  // Початковий стан — Basic

  public static UserState getUserState() {
    return userState;
  }

  public static void setPremiumState() {

    userState = new PremiumState();
  }

  public static void setBasicState() {
    userState = new BasicState();
  }

  public static void initializeState(boolean isPremium) {
    if (isPremium) {
      setPremiumState();
    } else {
      setBasicState();
    }
  }
}
