package dtu.TokenService.Domain.Entities;

import javax.json.bind.annotation.JsonbProperty;

public class Payment {
  @JsonbProperty("amount")
  public int amount;
  @JsonbProperty("cid")
  public String cid;
  @JsonbProperty("mid")
  public String mid;


  public Payment(int amount, String cid, String mid) {
    this.amount = amount;
    this.cid = cid;
    this.mid = mid;
  }

  public Payment() {
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public String getCid() {
    return cid;
  }

  public void setCid(String cid) {
    this.cid = cid;
  }

  public String getMid() {
    return mid;
  }

  public void setMid(String mid) {
    this.mid = mid;
  }


}
