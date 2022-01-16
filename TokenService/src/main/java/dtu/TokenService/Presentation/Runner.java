package dtu.TokenService.Presentation;

import messaging.Event;

public class Runner {
  public static void main(String[] args) {
      TokenEventHandler handler = new TokenMessageFactory().getService();
      System.out.println(handler.toString() + " Started?");
  }
}