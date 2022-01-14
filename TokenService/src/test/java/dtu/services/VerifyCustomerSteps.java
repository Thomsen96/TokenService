package dtu.services;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import dtu.TokenService.Application.TokenService;
import dtu.TokenService.Domain.Repositories.LocalTokenRepository;
import dtu.TokenService.Presentation.Resources.TokenMessageService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;

public class VerifyCustomerSteps {
	String customerId = null;
	String merchantId = null;
	
	private CompletableFuture<Event> eventPublished = new CompletableFuture<>();
	private MessageQueue messageQueue = new MessageQueue() {
		
		@Override
		public void publish(Event message) {
			eventPublished.complete(message);
		}
		
		@Override
		public void addHandler(String eventType, Consumer<Event> handler) {
		}
	};

	private TokenService tokenService = new TokenService(new LocalTokenRepository());
	private TokenMessageService messageService = new TokenMessageService(messageQueue, tokenService);
	private CompletableFuture<Boolean> customerVerified = new CompletableFuture<>();

	public VerifyCustomerSteps() {
	}
	
	@Given("a customer has an id {string}")
	public void aCustomerHasAnId(String customerId){
		this.customerId = customerId;
	}

	@When("the customer is being verified")
	public void theCustomerIsBeingVerified() {
		// We have to run the verification in a thread, because
		// the register method will only finish after the next @When
		// step is executed.
		new Thread(() -> {
			var result = messageService.verifyCustomer(customerId);
			customerVerified.complete(result);
		}).start();
	}

	@Then("the {string} event is sent")
	public void theEventIsSent(String sendEventString) {
		Event event = new Event(sendEventString, new Object[] { customerId }); // 	"CustomerVerificationRequested"
//		verify(messageQueue).publish(event);
		assertEquals(event, eventPublished.join());
	}
	
	@When("the {string} event is sent with customerId")
	public void theEventIsSentWithCustomerId(String returnEventString) {
		// This step simulate the event created by a downstream service.'
		boolean returnVal = true;					// "CustomerVerified"
		messageService.handleCustomerVerification(new Event(returnEventString,new Object[] {returnVal}));
	}

	@Then("the customer is verified")
	public void theCustomerIsVerified() {
		// Our logic is very simple at the moment; we don't
		// remember that the student is registered.
		assertNotNull(customerVerified.join().booleanValue());
	}

}