package dtu.services;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import java.util.concurrent.CompletableFuture;

import dtu.TokenService.Application.TokenService;
import dtu.TokenService.Domain.Repositories.LocalTokenRepository;
import dtu.TokenService.Presentation.TokenEventHandler;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.implementations.MockMessageQueue;

public class VerifyCustomerSteps {
	String customerId = null;
	String merchantId = null;
	
	private static MockMessageQueue messageQueue = new MockMessageQueue();
	private TokenService tokenService = new TokenService(new LocalTokenRepository());
	private TokenEventHandler messageService = new TokenEventHandler(messageQueue, tokenService);
	private CompletableFuture<Event> customerVerificationResponseComplete = new CompletableFuture<>();

	private Event customerVerificationResponse;


	

	public VerifyCustomerSteps() {
	}
	
	@Given("a customer has an id {string}")
	public void aCustomerHasAnId(String customerId){
		this.customerId = customerId;
	}

	@When("the customer is being verified") // "When customer customerVerificationRequest is sent"
	public void theCustomerIsBeingVerified() {
		// We have to run the verification in a thread, because
		// the register method will only finish after the next @When
		// step is executed.
		new Thread(() -> {
			customerVerificationResponse  = messageService.verifyCustomer(customerId);
			customerVerificationResponseComplete.complete(customerVerificationResponse);
		}).start();
	}

	@Then("the {string} event is sent") // If this assert fails, maybe try again you were unlucky.
	public void theEventIsSent(String sendEventString) throws InterruptedException {
		Event event = new Event(sendEventString, new Object[] { customerId }); // 	"CustomerVerificationRequested"
		Thread.sleep(20); // added feature for concurrency
		assertEquals(event, messageQueue.getEvent(sendEventString));
	}
	
	@When("the {string} event is sent with customerId") // "when the CustomerVerificationResponse is received"
	public void theEventIsSentWithCustomerId(String returnEventString) {
		// This step simulate the event created by a downstream service.'
		boolean returnVal = true;					// "CustomerVerified"

		messageService.handleCustomerVerification(new Event(returnEventString,new Object[] {returnVal}));
	}

	@Then("the customer is verified")
	public void theCustomerIsVerified() {
		assertTrue(customerVerificationResponseComplete.join().getArgument(0, boolean.class));
	}
}