package dtu.TokenService.Domain.Entities;

public class Customer {

  private String name;
  private String cprNumber;
  private String balance;

  public Customer(String name, String cprNumber, String balance)
  {
    this.name = name;
    this.cprNumber = cprNumber;
    this.balance = balance;
  }


  
}
