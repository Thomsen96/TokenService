package dtu.TokenService.Presentation;

public class Runner {
  public static void main(String[] args) {
      TokenEventHandler handler = new TokenMessageFactory().getService();
      System.out.println(handler.toString() + " Started?");
  }
}