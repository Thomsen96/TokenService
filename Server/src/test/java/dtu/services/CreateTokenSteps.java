package dtu.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import dtu.TokenService.Application.TokenService;
import dtu.TokenService.Domain.Entities.Token;
import dtu.TokenService.Domain.Repositories.LocalTokenRepository;
import dtu.TokenService.Presentation.Resources.TokenMessageService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.MessageQueue;

public class CreateTokenSteps {

  String customerId = null;
  String merchantId = null;
  private MessageQueue q = mock(MessageQueue.class);
	private TokenMessageService service = new TokenMessageService(q);
  List<Token> tokens = new ArrayList<>();

  TokenService tokenService = new TokenService(new LocalTokenRepository());

  @Given("a customer with id {string}")
  public void aCustomerWithId(String customerId) {
    this.customerId = customerId;
  }

  @Given("the customer already has {int} tokens")
  public void theCustomerAlreadyHasTokens(Integer numOfTokens) {
    tokens = tokenService.createTokens(numOfTokens, customerId);
  }

  @When("the customer requests {int} tokens")
  public void theCustomerRequestsTokens(Integer numOfTokens) {
    tokens = tokenService.createTokens(numOfTokens, customerId);
  }

  @Then("the customer has {int} tokens")
  public void theCustomerHasTokens(Integer expectedNumOfTokens) {
    assertEquals(expectedNumOfTokens, tokens.size());
  }
}
