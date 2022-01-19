package dtu.services;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.concurrent.CompletableFuture;

import dtu.infrastructure.AccountAccess;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.EventResponse;
import messaging.implementations.MockMessageQueue;

public class VerifyCustomerSteps {
	String customerId = null;
	String merchantId = null;
	String sessionId;
	
	private static MockMessageQueue messageQueue = new MockMessageQueue();
	private static AccountAccess accountAccess = new AccountAccess(messageQueue);
	private Event customerVerificationResponse;
	private CompletableFuture<Event> customerVerificationResponseComplete = new CompletableFuture<>();

	public VerifyCustomerSteps() {
	}
	
	@Given("a customer has an id {string}")
	public void aCustomerHasAnId(String customerId){
		this.customerId = customerId;
		this.sessionId = "UniqueSessionId";
	}

	@When("the customer is being verified") // "When customer customerVerificationRequest is sent"
	public void theCustomerIsBeingVerified() {
		new Thread(() -> {
			customerVerificationResponse = accountAccess.customerVerificationRequest(customerId, sessionId);
			customerVerificationResponseComplete.complete(customerVerificationResponse);
		}).start();
	}

	@Then("the verification request event is sent") // If this assert fails, maybe try again you were unlucky.
	public void theVerificationEventIsSent() throws InterruptedException {
		EventResponse eventResponse = new EventResponse(sessionId, true, null, customerId);
		Event event = new Event("CustomerVerificationRequest", eventResponse); 
		Thread.sleep(20); // added feature for concurrency
		assertEquals(event, messageQueue.getEvent("CustomerVerificationRequest"));
	}
	
	@When("the verification response event is sent with customerId") // "when the CustomerVerificationResponse is received"
	public void theVerificationEventIsSentWithCustomerId() {
		// This step simulate the event created by the account service.'
		EventResponse eventResponse = new EventResponse(sessionId, true, null);
		Event responseEvent = new Event("CustomerVerificationResponse." + sessionId, eventResponse);
		accountAccess.handleCustomerVerificationResponse(responseEvent);
	}

	@Then("the customer is verified")
	public void theCustomerIsVerified() {
		EventResponse eventResponse = new EventResponse(sessionId, true, null);
		Event expectedEvent = new Event("CustomerVerificationResponse." + sessionId, eventResponse);
		Event actualEvent = customerVerificationResponseComplete.join();
		assertEquals(expectedEvent, actualEvent);
	}
}