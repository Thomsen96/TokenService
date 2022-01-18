package dtu.services;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.concurrent.CompletableFuture;

import dtu.TokenService.Infrastructure.AccountAccess;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
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
		// We have to run the verification in a thread, because
		// the register method will only finish after the next @When
		// step is executed.
		new Thread(() -> {
			customerVerificationResponse = accountAccess.customerVerificationRequest(customerId, sessionId);
			customerVerificationResponseComplete.complete(customerVerificationResponse);
		}).start();
	}

	@Then("the verification request event is sent") // If this assert fails, maybe try again you were unlucky.
	public void theVerificationEventIsSent() throws InterruptedException {
		Event event = new Event("CustomerVerificationRequest", new Object[] { customerId, sessionId }); // 	"CustomerVerificationRequested"
		Thread.sleep(20); // added feature for concurrency
		assertEquals(event, messageQueue.getEvent("CustomerVerificationRequest"));
	}
	
	@When("the verification response event is sent with customerId") // "when the CustomerVerificationResponse is received"
	public void theVerificationEventIsSentWithCustomerId() {
		// This step simulate the event created by a downstream service.'
		boolean returnVal = true;					// "CustomerVerified"
		Event responseEvent = new Event("CustomerVerificationResponse",new Object[] {returnVal});
		accountAccess.handleCustomerVerificationResponse(responseEvent);
	}

	@Then("the customer is verified")
	public void theCustomerIsVerified() {
		assertTrue(customerVerificationResponseComplete.join().getArgument(0, boolean.class));
	}
}