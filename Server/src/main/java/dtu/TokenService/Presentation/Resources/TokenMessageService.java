package dtu.TokenService.Presentation.Resources;

import java.util.concurrent.CompletableFuture;

import dtu.TokenService.Application.TokenService;
import messaging.Event;
import messaging.MessageQueue;

public class TokenMessageService {
  private MessageQueue messageQueue;
	//private CompletableFuture<Boolean> customerVerified;
	private TokenService tokenService;

	public TokenMessageService(MessageQueue messageQueue, TokenService tokenService) {
		
		this.messageQueue = messageQueue;
		this.tokenService = tokenService;
		this.messageQueue.addHandler("TokenVerificationRequested", this::handleTokenVerificationRequested);
	}

/*
  // Send request?
	public Boolean verifyCustomer(String customerId) {
		customerVerified = new CompletableFuture<>();
		Event event = new Event("CustomerVerificationRequested", new Object[] { customerId });
		queue.publish(event);
		return customerVerified.join();
	}

*/

  // Handle incoming requests?
	public void handleTokenVerificationRequested(Event e) {
		var token = e.getArgument(0, String.class);
		Boolean tokenValid = tokenService.verifyToken(token);
		Event event = new Event("TokenVerificationResponse", new Object[] { tokenValid });
		messageQueue.publish(event);
	}


}
