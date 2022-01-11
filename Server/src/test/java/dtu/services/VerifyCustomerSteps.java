package dtu.services;

import java.util.ArrayList;
import java.util.List;

import dtu.TokenService.Application.TokenService;
import dtu.TokenService.Domain.Entities.Token;
import dtu.TokenService.Domain.Repositories.LocalTokenRepository;
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
	private StudentRegistrationService service = new StudentRegistrationService(q);
	private CompletableFuture<Student> registeredStudent = new CompletableFuture<>();
	private Student student;

  @Given("A customer with id {string}")
  public void aCustomerWithId(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the customer the event verify the customer have happend")
  public void theCustomerTheEventVerifyTheCustomerHaveHappend() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the {string} is sent")
  public void theIsSent(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

}