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
  String merchantId = null;

  private MessageQueue q = mock(MessageQueue.class);
	private TokenMessageService service = new TokenMessageService(q);
	private CompletableFuture<Boolean> customerVerified = new CompletableFuture<>();

  @Given("A customer with id {string}")
  public void aCustomerWithId(String customerId) {
    this.customerId = customerId;
  }

  @When("the customer the event verify the customer have happend")
  public void theCustomerTheEventVerifyTheCustomerHaveHappend() {
    
    new Thread(() -> {
			var result = service.verifyCustomer(customerId);
			customerVerified.complete(result);
		}).start();
  }

  @Then("the {string} is sent")
  public void theIsSent(String string) {
    Event event = new Event(string, new Object[] { customerId });
		verify(q).publish(event);
  }

}