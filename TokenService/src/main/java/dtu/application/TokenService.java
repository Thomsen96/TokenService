package dtu.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dtu.domain.Token;
import dtu.infrastructure.AccountAccess;
import dtu.infrastructure.ITokenRepository;
import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;

public class TokenService {

	private ITokenRepository tokenRepository;
	private MessageQueue messageQueue;
	private AccountAccess accountAccess;


	public TokenService(MessageQueue messageQueue, ITokenRepository tokenRepository, AccountAccess accountAccess) {
		this.messageQueue = messageQueue;
		this.tokenRepository = tokenRepository;
		this.accountAccess = accountAccess;
	}

	public ArrayList<Token> createTokens(String customerId, Integer numOfTokens, String sessionId) {
		Event event = accountAccess.customerVerificationRequest(customerId, sessionId);
		EventResponse eventResponse;
		if(event.getArgument(0, EventResponse.class).isSuccess()) {
			eventResponse = generateTokenEventResponse(sessionId, customerId, numOfTokens);
		}
		else {
			eventResponse = new EventResponse(sessionId, false, "Token creation failed: Customer ID is not in our system");
		}
		Event creationResponseEvent = new Event("TokenCreationResponse." + sessionId, eventResponse);
		messageQueue.publish(creationResponseEvent);
		return tokenRepository.get(customerId);
	}

	public EventResponse generateTokenEventResponse(String sessionId, String customerId, Integer numOfTokens) {
		EventResponse eventResponse;
		ArrayList<Token> tokenList = tokenRepository.get(customerId);
		if(numOfTokens > 0 && numOfTokens < 6 && tokenList.size() < 2) {
			for( int i = 0; i < numOfTokens; i++) {
				tokenRepository.create(customerId);
			}
			tokenList = tokenRepository.get(customerId);
			List<String> tokenIdList = tokenList.stream().map(token -> token.getUuid()).collect(Collectors.toList());
			String[] tokenIdArray = tokenIdList.toArray(new String[] {});
			eventResponse = new EventResponse(sessionId, true, null, tokenIdArray);
		}
		else {
			eventResponse = new EventResponse(sessionId, false, "Invalid token request: You either have 2 or more tokens already, or you requested an invalid amount");
		}
		return eventResponse;
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
