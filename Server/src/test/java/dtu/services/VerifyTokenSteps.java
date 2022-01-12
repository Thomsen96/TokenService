package dtu.services;

//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;

import dtu.TokenService.Application.TokenService;
//import dtu.TokenService.Domain.Entities.Token;
import dtu.TokenService.Domain.Repositories.LocalTokenRepository;
import dtu.TokenService.Presentation.Resources.TokenMessageService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import messaging.Event;
import messaging.MessageQueue;

public class VerifyTokenSteps {

  String customerId = null;
  String token = null;

  private MessageQueue messageQueue = mock(MessageQueue.class);
  private TokenService tokenService = new TokenService(new LocalTokenRepository());
  private TokenMessageService service = new TokenMessageService(messageQueue, tokenService);

  @Given("A customer with id {string}")
  public void aCustomerWithId(String customerId) {
    this.customerId = customerId;

    token = tokenService.createTokens(1, customerId).get(0).getUuid();
  }

  @When("a request to verify the token is received")
  public void aRequestToVerifyTheTokenIsReceived() {
    service.handleTokenVerificationRequested(new Event("TokenVerificationRequested",new Object[] {this.token}));
  }

  @Then("the token is verified")
  public void theTokenIsVerified() {
    Boolean bool = true;
    Event event = new Event("TokenVerificationResponse", new Object[] { bool });
	verify(messageQueue).publish(event);
  }

}