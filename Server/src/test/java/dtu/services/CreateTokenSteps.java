package dtu.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import dtu.TokenService.Application.TokenService;
import dtu.TokenService.Domain.Entities.Token;
import dtu.TokenService.Domain.Repositories.LocalTokenRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CreateTokenSteps {

  String customerId = null;
  String merchantId = null;
  List<Token> tokens = new ArrayList<>();
  TokenService tokenService = new TokenService(new LocalTokenRepository());

  @Given("a customer with id {string}")
  public void aCustomerWithId(String customerId) {
    this.customerId = customerId;
  }

  @Given("the customer already has {int} tokens")
  public void theCustomerAlreadyHasTokens(Integer numOfTokens) {
    tokens = tokenService.getTokens(numOfTokens, customerId);
  }



  @When("the customer requests {int} tokens")
  public void theCustomerRequestsTokens(Integer numOfTokens) {
    tokens = tokenService.getTokens(numOfTokens, customerId);
  }

  @Then("the customer has {int} tokens")
  public void theCustomerHasTokens(Integer expectedNumOfTokens) {
    assertEquals(expectedNumOfTokens, tokens.size());
  }

  // @Given("a customer with the name {string}, cpr number {string} and balance of
  // {string} kr is registered")
  // public void aCustomerWithTheNameCprNumberAndBalanceOfKrIsRegistered(String
  // name, String cprNumber, String balance) {
  // var user = new User();
  // user.setCprNumber(cprNumber);
  // user.setLastName("LAST NAME");
  // user.setFirstName(name);
  // try {
  // customerId = bankService.createAccountWithBalance(user, new
  // BigDecimal(balance));
  // } catch (BankServiceException_Exception e) {
  // e.printStackTrace();
  // }
  // }
  //
  // @Given("the customer requests {int} tokens")
  // public void theCustomerRequestsTokens(Integer int1) {
  // tokens = new ArrayList<>();
  // tokens.add("THIS IS TOTALLY RANDOM GENERATE TOKEN 1");
  //
  //
  // }
  //
  // @Given("a merchant with the name {string}, cpr number {string} and balance of
  // {string} kr is registered")
  // public void aMerchantWithTheNameCprNumberAndBalanceOfKrIsRegistered(String
  // name, String cprNumber, String balance) {
  // var user = new User();
  // user.setCprNumber(cprNumber);
  // user.setLastName("LAST NAME");
  // user.setFirstName(name);
  // try {
  // merchantId = bankService.createAccountWithBalance(user, new
  // BigDecimal(balance));
  // } catch (BankServiceException_Exception e) {
  // e.printStackTrace();
  // }
  // }
  //
  // @When("the merchant requests a payment with a token and merchant id for the
  // amount of {string} kr")
  // public void
  // theMerchantRequestsAPaymentWithATokenAndMerchantIdForTheAmountOfKr(String
  // string) {
  // // Write code here that turns the phrase above into concrete actions
  // throw new io.cucumber.java.PendingException();
  // }
  //
  // @Then("the balance of the merchant is {string} kr")
  // public void theBalanceOfTheMerchantIsKr(String string) {
  // // Write code here that turns the phrase above into concrete actions
  // throw new io.cucumber.java.PendingException();
  // }
  //
  // @Then("the balance of the customer is {string} kr")
  // public void theBalanceOfTheCustomerIsKr(String string) {
  // // Write code here that turns the phrase above into concrete actions
  // throw new io.cucumber.java.PendingException();
  // }

}
