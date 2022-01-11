package dtu.services;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import dtu.ws.fastmoney.*;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleDTUPaySteps {
  String cid = "cid1", mid = "mid1";
  SimpleDtuService dtuPay = new SimpleDtuService();
  Response res;
  ArrayList<Payment> paymentList;
  static BankService bankService = new BankServiceService().getBankServicePort();

  
/*

  @When("the merchant initiates a payment for {int} kr by the customer")
  public void theMerchantInitiatesAPaymentForKrByTheCustomer(int amount) {
    var res = dtuPay.pay(amount, customerId, merchantId);
    System.out.println(res.getStatus());
    this.res = res;
  }

  @Then("the response status code is {int}")
  public void theResponseStatusCodeIs(int statusCode) {
    assertEquals(statusCode, res.getStatus());
  }

  @Given("a successful payment of {int} kr from customer {string} to merchant {string}")
  public void aSuccessfulPaymentOfKrFromCustomerToMerchant(int amount, String cid, String mid) {
    res = dtuPay.pay(amount, cid, mid);
  }

  @When("the manager asks for a list of payments")
  public void theManagerAsksForAListOfPayments() {
    var res = dtuPay.getPayments();
    System.out.println(res);
    var payments = res.readEntity(new GenericType<ArrayList<Payment>>() {});
    paymentList = payments.stream().filter(p -> p.cid.equals(cid) && p.mid.equals(mid)).collect(Collectors.toCollection(ArrayList::new));
  }

  @Then("the list contains a payments where customer {string} paid {int} kr to merchant {string}")
  public void theListContainsAPaymentsWhereCustomerPaidKrToMerchant(String cid, int amount, String mid) {
    Payment payment = new Payment(amount, cid, mid);
    System.out.println(paymentList.size());
    assertTrue(paymentList.stream().anyMatch(p -> p.equals(payment)));
  }

  @Then("the payment is not successful")
  public void thePaymentIsNotSuccessful() {
    assertEquals(400, res.getStatus());
  }

  @And("an error message is returned saying {string}")
  public void anErrorMessageIsReturnedSaying(String arg0) {
    assertEquals(arg0, res.readEntity(String.class));
  }

  */



  @After
  public static void before_or_after_all() {
    try {
      bankService.retireAccount(customerId);

    } catch (BankServiceException_Exception e) {

    }

    try {
      bankService.retireAccount(merchantId);
    } catch (BankServiceException_Exception e) {

    }

  }

  static String customerId = null;
  @Given("a customer with a bank account with balance {int}")
  public void aCustomerWithABankAccountWithBalance(int balance) {

    var user = new User();
    user.setCprNumber("135643-1337");
    user.setLastName("customer");
    user.setFirstName("customer");
    try {
      customerId = bankService.createAccountWithBalance(user, new BigDecimal(String.valueOf(balance)));
    } catch (BankServiceException_Exception e) {
        e.printStackTrace();
    }
  }

  @And("that the customer is registered with DTU Pay")
  public void thatTheCustomerIsRegisteredWithDTUPay() throws BankServiceException_Exception {
    var account = bankService.getAccount(customerId);
    dtuPay.registerAccount(account);
  }

  static String merchantId = null;
  @Given("a merchant with a bank account with balance {int}")
  public void aMerchantWithABankAccountWithBalance(int balance) throws BankServiceException_Exception {
    var user = new User();
    user.setCprNumber("135413-0505");
    user.setLastName("merchant");
    user.setFirstName("merchant");
    merchantId = bankService.createAccountWithBalance(user, new BigDecimal(String.valueOf(balance)));
  }

  @And("that the merchant is registered with DTU Pay")
  public void thatTheMerchantIsRegisteredWithDTUPay() throws BankServiceException_Exception {
    var account = bankService.getAccount(merchantId);
    dtuPay.registerAccount(account);
  }

  @Then("the payment is successful")
  public void thePaymentIsSuccessful() {
    assertEquals(204, res.getStatus());
  }

  @And("the balance of the customer at the bank is {string} kr")
  public void theBalanceOfTheCustomerAtTheBankIsKr(String balance) throws BankServiceException_Exception {
    assertEquals(new BigDecimal(balance), bankService.getAccount(customerId).getBalance());
  }

  @And("the balance of the merchant at the bank is {string} kr")
  public void theBalanceOfTheMerchantAtTheBankIsKr(String balance) throws BankServiceException_Exception {
    assertEquals(new BigDecimal(balance), bankService.getAccount(merchantId).getBalance());
  }

  @When("the merchant initiates a payment for {int} kr by the customer")
  public void theMerchantInitiatesAPaymentForKrByTheCustomer(int arg0) {
    res = dtuPay.pay(arg0, customerId, merchantId);
  }
}