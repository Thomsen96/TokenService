package dtu.application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import dtu.domain.Token;
import dtu.infrastructure.ITokenRepository;
import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;

public class TokenService {

	private ITokenRepository tokenRepository;
	private MessageQueue messageQueue;
    ServiceHelper serviceHelper = new ServiceHelper();
	
	private static ConcurrentHashMap<String, CompletableFuture<Event>> sessions = new ConcurrentHashMap<>();
//	CompletableFuture<Event> session;


	public TokenService(MessageQueue messageQueue, ITokenRepository tokenRepository) {
		this.messageQueue = messageQueue;
		this.tokenRepository = tokenRepository;
	}

	public ArrayList<Token> createTokens(String customerId, Integer numOfTokens, String sessionId) {
		System.out.println("createTokens");
		Event verificationResponseEvent = customerVerificationRequest(customerId, sessionId);
		EventResponse eventResponse;
		System.out.println("Checking if success");
		if(verificationResponseEvent.getArgument(0, EventResponse.class).isSuccess()) {
			System.out.println("Event success");
			eventResponse = generateTokenEventResponse(sessionId, customerId, numOfTokens);
		}
		else {
			System.out.println("Event failure");
			eventResponse = new EventResponse(sessionId, false, "Token creation failed: Either a timeout or customer ID is not in our system");
		}
		Event creationResponseEvent = new Event("TokenCreationResponse." + sessionId, eventResponse);
		System.out.println("Responding to REST with: " + creationResponseEvent);
		messageQueue.publish(creationResponseEvent);
		return tokenRepository.get(customerId);
	}

	public EventResponse generateTokenEventResponse(String sessionId, String customerId, Integer numOfTokens) {
		System.out.println("generateTokenEventResponse");
		EventResponse eventResponse;
		ArrayList<Token> tokenList = tokenRepository.get(customerId);
		if(numOfTokens > 0 && numOfTokens < 6 && tokenList.size() < 2) {
			for( int i = 0; i < numOfTokens; i++) {
				tokenRepository.create(customerId);
			}
			eventResponse = new EventResponse(sessionId, true, null, getTokens(customerId));
		}
		else {
			eventResponse = new EventResponse(sessionId, false, "Invalid token request: You either have 2 or more tokens already, or you requested an invalid amount");
		}
		return eventResponse;
	}
	
	
	
	
	
	
	public Event customerVerificationRequest(String customerId, String sessionId) {
		System.out.println("customerVerificationRequest");
		sessions.put(sessionId, new CompletableFuture<Event>());
		messageQueue.addHandler("CustomerVerificationResponse." + sessionId, this::handleCustomerVerificationResponse);
		
		EventResponse eventResponse = new EventResponse(sessionId, true, null, customerId);
		Event outgoingEvent = new Event("CustomerVerificationRequest", eventResponse);
		messageQueue.publish(outgoingEvent);

		serviceHelper.addTimeOut(sessionId, sessions.get(sessionId), "No response from AccountService");

		
		System.out.println("Waiting for verification response");
		Event responseEvent = sessions.get(sessionId).join();
		return responseEvent;
	}

	// Handler for verification response from CustomerService in the form of a boolean to see if the customer is registered.
	public void handleCustomerVerificationResponse(Event e) {
		System.out.println("CustomerVerificationResponse from AccountService:\n" + e);
		String sessionId = e.getArgument(0, EventResponse.class).getSessionId();
		sessions.get(sessionId).complete(e);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	public String[] getTokens(String customerId) {
//		return tokenRepository.get(customerId);
		ArrayList<Token> tokenList = tokenRepository.get(customerId);
		List<String> tokenIdList = tokenList.stream().map(token -> token.getUuid()).collect(Collectors.toList());
		return tokenIdList.toArray(new String[] {});
	}

	public boolean deleteTokensForCustomer(String customerId) {
		return tokenRepository.delete(customerId);
	}
	
	public EventResponse getVerifiedTokenResponse(String sessionId, String tokenUuid) {
		Token verfiedToken = tokenRepository.getVerfiedToken(tokenUuid);
		EventResponse eventResponse;
		if(verfiedToken.getTokenValidity()) {
			eventResponse = new EventResponse(sessionId, true, null, verfiedToken);
		}
		else {
			eventResponse = new EventResponse(sessionId, false, "The requested token is not valid");
		}
		return eventResponse;
	}
	
	public Token getVerifiedToken(String tokenUuid) {
		return tokenRepository.getVerfiedToken(tokenUuid);
	}

	public void getStatus(String sessionId) {
		EventResponse eventResponse = new EventResponse(sessionId, true, null, "Token service ready");
		Event event = new Event("TokenStatusResponse." + sessionId, eventResponse);
		messageQueue.publish(event);
	}

}
