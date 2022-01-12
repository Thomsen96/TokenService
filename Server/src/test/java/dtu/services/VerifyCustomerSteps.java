package dtu.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import dtu.TokenService.Application.TokenService;
import dtu.TokenService.Domain.Entities.Token;
import dtu.TokenService.Domain.Repositories.LocalTokenRepository;
import dtu.TokenService.Presentation.Resources.TokenMessageService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import messaging.Event;
import messaging.MessageQueue;

public class VerifyCustomerSteps {

  String customerId = null;
  String token = null;

  private MessageQueue q = mock(MessageQueue.class);
  private TokenMessageService service = new TokenMessageService(q);
  private CompletableFuture<Boolean> customerVerified = new CompletableFuture<>();

  @Given("A customer with id {string} and token {string}")
  public void aCustomerWithId(String customerId, String token) {
    this.customerId = customerId;
    this.token = token;

  }

  @When("a verification of token {string} is received for verification")
  public void aVerificationOfTokenIsReceivedForVerification(String token) {
    service.handleTokenVerificationRequested(new Event("TokenVerificationRequested",new Object[] {token}));
    
  }

  @Then("the token is verified")
  public void theTokenIsVerified() {
    Boolean bool = true;
    Event event = new Event("TokenVerificationResponse", new Object[] { bool });
		verify(q).publish(event);
  }

}