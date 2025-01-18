package org.example.state;


public class Session {
  private static int userId;
  private static UserState userState = new BasicState();
  public static UserState getUserState() {
    return userState;
  }
  public static void setPremiumState() {
    userState = new PremiumState();
  }
  public static void setBasicState() {
    userState = new BasicState();
  }
  public static int getUserId() {
    return userId;
  }
  public static void initializeState(int userId, boolean isPremium) {
    Session.userId = userId;
    if (isPremium) {
      setPremiumState();
    } else {
      setBasicState();
    }
  }
  public static void initializeState(boolean isPremium) {
    if (isPremium) {
      setPremiumState();
    } else {
      setBasicState();
    }
  }
}
