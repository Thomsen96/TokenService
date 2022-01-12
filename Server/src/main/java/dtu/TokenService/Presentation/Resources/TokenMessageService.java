package dtu.TokenService.Presentation.Resources;

import java.util.concurrent.CompletableFuture;

import messaging.Event;
import messaging.MessageQueue;

public class TokenMessageService {
  private MessageQueue queue;
	private CompletableFuture<Boolean> customerVerified;

	public TokenMessageService(MessageQueue q) {
		queue = q;
		queue.addHandler("TokenVerificationRequested", this::handleTokenVerificationRequested);
	}


  // Send request?
	public Boolean verifyCustomer(String customerId) {
		customerVerified = new CompletableFuture<>();
		Event event = new Event("CustomerVerificationRequested", new Object[] { customerId });
		queue.publish(event);
		return customerVerified.join();
	}


  // Handle incoming requests?
	public void handleTokenVerificationRequested(Event e) {
		var s = e.getArgument(0, String.class);
		// TODO: Add business logic about wether the token is valid.
		Boolean bool = true;
		Event event = new Event("TokenVerificationResponse", new Object[] { bool });
		queue.publish(event);
	}


}
