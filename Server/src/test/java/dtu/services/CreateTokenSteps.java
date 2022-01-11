package dtu.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CreateTokenSteps {

  String customerId = null;
  String merchantId = null;
  List<String> tokens = new ArrayList<>();
  BankService bankService = new BankServiceService().getBankServicePort();


  @Given("a customer with the name {string}, cpr number {string} and balance of {string} kr is registered")
  public void aCustomerWithTheNameCprNumberAndBalanceOfKrIsRegistered(String name, String cprNumber, String balance) {
    var user = new User();
    user.setCprNumber(cprNumber);
    user.setLastName("LAST NAME");
    user.setFirstName(name);
    try {
      customerId = bankService.createAccountWithBalance(user, new BigDecimal(balance));
    } catch (BankServiceException_Exception e) {
        e.printStackTrace();
    }
  }

  @Given("the customer requests {int} tokens")
  public void theCustomerRequestsTokens(Integer int1) {
    tokens = new ArrayList<>();
    tokens.add("THIS IS TOTALLY RANDOM GENERATE TOKEN 1");

    
  }

  @Given("a merchant with the name {string}, cpr number {string} and balance of {string} kr is registered")
  public void aMerchantWithTheNameCprNumberAndBalanceOfKrIsRegistered(String name, String cprNumber, String balance) {
    var user = new User();
    user.setCprNumber(cprNumber);
    user.setLastName("LAST NAME");
    user.setFirstName(name);
    try {
      merchantId = bankService.createAccountWithBalance(user, new BigDecimal(balance));
    } catch (BankServiceException_Exception e) {
        e.printStackTrace();
    }
  }

  @When("the merchant requests a payment with a token and merchant id for the amount of {string} kr")
  public void theMerchantRequestsAPaymentWithATokenAndMerchantIdForTheAmountOfKr(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the balance of the merchant is {string} kr")
  public void theBalanceOfTheMerchantIsKr(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the balance of the customer is {string} kr")
  public void theBalanceOfTheCustomerIsKr(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

}
