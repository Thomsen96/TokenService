package dtu.TokenService.Presentation;

import java.util.HashSet;
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
		//		this.messageQueue.addHandler("CustomerVerified", this::handleCustomerVerification);
	}

	public void handleTokenCreationRequest(Event e) {
		var customerId = e.getArgument(0, String.class);
		var numOfTokens = e.getArgument(1, Integer.class);
		var sessionId = e.getArgument(2, String.class);
		
		tokenService.createTokens(customerId, numOfTokens, sessionId);
	}

	// Handler for verification request from Payments that needs to know if the token is valid and the cid for the token.
	public void handleTokenVerificationRequest(Event e) {
		var tokenUuid = e.getArgument(0, String.class);
		var sessionId = e.getArgument(1, String.class);
		Token tokenObj = tokenService.getVerifiedToken(tokenUuid);
		Event event = new Event("TokenVerificationResponse." + sessionId, new Object[] { tokenObj });
		messageQueue.publish(event);
	}

	public void handleTokenStatusRequest(Event e) {
		tokenService.getStatus();
	}
}
