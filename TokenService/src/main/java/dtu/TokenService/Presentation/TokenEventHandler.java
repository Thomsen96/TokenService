package dtu.TokenService.Presentation;

import java.util.concurrent.CompletableFuture;

import dtu.TokenService.Application.TokenService;
import messaging.Event;
import messaging.MessageQueue;
import dtu.TokenService.Domain.Entities.Token;

public class TokenEventHandler {
	private MessageQueue messageQueue;
	private CompletableFuture<Event> customerVerified;
	private TokenService tokenService;

	public TokenEventHandler(MessageQueue messageQueue, TokenService tokenService) {
		this.messageQueue = messageQueue;
		this.tokenService = tokenService;
		this.messageQueue.addHandler("TokenStatusRequest", this::handleTokenStatusRequest);
		this.messageQueue.addHandler("TokenVerificationRequested", this::handleTokenVerificationRequest);
		this.messageQueue.addHandler("TokenCreationRequest", this::handleTokenCreationRequest);
		this.messageQueue.addHandler("CustomerVerified", this::handleCustomerVerification);
	}

	public void handleTokenStatusRequest(Event e) {
		System.out.println("Received a request to send back to status the service");
		Event event = new Event("TokenStatusResponse", new Object[] {tokenService.getStatus()});
		messageQueue.publish(event);
	}

	// We send a verification request meant for AccountService with the customerId
	public Event verifyCustomer(String customerId) {
		customerVerified = new CompletableFuture<Event>();
		Event event = new Event("CustomerVerificationRequested", new Object[] { customerId });
		messageQueue.publish(event);
		return customerVerified.join();
	}

	// Handler for verification response from AccountService in the form of a boolean to see if the customer is registered.
	public void handleCustomerVerification(Event e) {
		System.err.println("handleCustomerVerification" + e);
		customerVerified.complete(e);
	}

	public void handleTokenCreationRequest(Event e) {
		var customerId = e.getArgument(0, String.class);
		var numOfTokens = e.getArgument(1, Integer.class);
		var sessionId = e.getArgument(2, String.class);
//								TODO: CHANGE TO createTokens WHEN REST SERVICE HAS BEEN TESTED!
		Token tokenObj = tokenService.createAndReturnSingleToken(customerId, numOfTokens);
		Event event = new Event("TokenCreationResponse" + "#" + sessionId, new Object[] { tokenObj });
		messageQueue.publish(event);
	}
	
	// Handler for verification request from Payments that needs to know if the token is valid and the cid for the token.
	public void handleTokenVerificationRequest(Event e) {
		var tokenUuid = e.getArgument(0, String.class);
		Token tokenObj = tokenService.getVerifiedToken(tokenUuid);
		Event event = new Event("TokenVerificationResponse", new Object[] { tokenObj });
		messageQueue.publish(event);
	}
}
