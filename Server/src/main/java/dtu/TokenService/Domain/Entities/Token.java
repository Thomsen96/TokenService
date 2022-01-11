package dtu.TokenService.Domain.Entities;

import java.util.UUID;

public class Token {

  private String customerId = null;
  private String uuid = null;

  public Token(String customerId) {
    this.customerId = customerId;
    uuid = UUID.randomUUID().toString();
  }
  
  public String getCustomerId() {
    return customerId;
  }
  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }
  public String getUuid() {
    return uuid;
  }



  
}
