package dtu.presentation;

import dtu.application.TokenService;
import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;

public class TokenEventHandler {
	private MessageQueue messageQueue;
	private TokenService tokenService;

	public TokenEventHandler(MessageQueue messageQueue, TokenService tokenService) {
		this.messageQueue = messageQueue;
		this.tokenService = tokenService;
		this.messageQueue.addHandler("TokenStatusRequest", this::handleTokenStatusRequest);
		this.messageQueue.addHandler("TokenVerificationRequested", this::handleTokenVerificationRequest);
		this.messageQueue.addHandler("TokenCreationRequest", this::handleTokenCreationRequest);
		//		this.messageQueue.addHandler("CustomerVerified", this::handleCustomerVerification);
	}

	// TODO: Change sessionId to arg 0
	public void handleTokenCreationRequest(Event incommingEvent) {
		EventResponse eventArguments = incommingEvent.getArgument(0, EventResponse.class);
		var sessionId = eventArguments.getSessionId();
		var customerId = eventArguments.getArgument(0, String.class);
		var numOfTokens = eventArguments.getArgument(1, Integer.class);
		
		tokenService.createTokens(customerId, numOfTokens, sessionId);
	}


	// TODO: Change sessionId to arg 0
	// Handler for verification request from Payments that needs to know if the token is valid and the cid for the token.
	public void handleTokenVerificationRequest(Event incommingEvent) {
		EventResponse eventArguments = incommingEvent.getArgument(0, EventResponse.class);
		var sessionId = eventArguments.getSessionId();
		var tokenUuid = eventArguments.getArgument(0, String.class);
		EventResponse eventResponse = tokenService.getVerifiedTokenResponse(sessionId, tokenUuid);
		Event event = new Event("TokenVerificationResponse." + sessionId, eventResponse);
		messageQueue.publish(event);
	}

	public void handleTokenStatusRequest(Event e) {
		tokenService.getStatus(e.getArgument(0, EventResponse.class).getArgument(0, String.class));
	}
}
