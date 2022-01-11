package dtu.TokenService.Domain.Entities;

public class Merchant {


  private String name;
  private String cprNumber;
  private String balance;

  public Merchant(String name, String cprNumber, String balance) {
    this.name = name;
    this.cprNumber = cprNumber;
    this.balance = balance;
  }
  
}
