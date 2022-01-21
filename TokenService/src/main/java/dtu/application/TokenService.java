package dtu.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dtu.domain.Token;
import dtu.domain.TokenDTO;
import dtu.infrastructure.AccountAccess;
import dtu.infrastructure.ITokenRepository;
import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;

import static messaging.GLOBAL_STRINGS.TOKEN_SERVICE.PUBLISH.TOKEN_CREATION_RESPONDED;
import static messaging.GLOBAL_STRINGS.TOKEN_SERVICE.PUBLISH.TOKEN_STATUS_RESPONDED;

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
			System.out.println("Created tokens eventResponse: " + eventResponse);
		}
		else {
			eventResponse = new EventResponse(sessionId, false, "Token creation failed: Customer ID is not in our system");
		}
		Event creationResponseEvent = new Event(TOKEN_CREATION_RESPONDED + sessionId, eventResponse);
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


			TokenDTO tokens = new TokenDTO(getTokens(customerId));
			//String tokensAsJson = getTokensJson(customerId);
			
			eventResponse = new EventResponse(sessionId, true, null, tokens);
		}
		else {
			eventResponse = new EventResponse(sessionId, false, "Invalid token request: You either have 2 or more tokens already, or you requested an invalid amount");
		}
		return eventResponse;
	}
	
	public String getTokensJson(String customerId) {
		//		return tokenRepository.get(customerId);
		ArrayList<Token> tokenList = tokenRepository.get(customerId);
		List<String> tokenIdList = tokenList.stream().map(token -> token.getUuid()).collect(Collectors.toList());
		Gson gson=new GsonBuilder().create();
		String tokensAsJson = gson.toJson(tokenIdList);
		System.out.println("Token array: " + tokensAsJson);
		return tokensAsJson;
	}

	public List<String> getTokens(String customerId) {
		//		return tokenRepository.get(customerId);
		ArrayList<Token> tokenList = tokenRepository.get(customerId);
		List<String> tokenIdList = tokenList.stream().map(token -> token.getUuid()).collect(Collectors.toList());
		return tokenIdList;
	}

	public boolean deleteTokensForCustomer(String customerId) {
		return tokenRepository.delete(customerId);
	}

	public EventResponse getVerifiedTokenResponse(String sessionId, String tokenUuid) {
		Token verifiedToken = tokenRepository.getVerfiedToken(tokenUuid);
		System.out.println("Token verified, sending customerId: " + verifiedToken.getCustomerId());
		EventResponse eventResponse;
		if(verifiedToken.getTokenValidity()) {
			eventResponse = new EventResponse(sessionId, true, null, verifiedToken.getCustomerId());
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
		Event event = new Event(TOKEN_STATUS_RESPONDED + sessionId, eventResponse);
		messageQueue.publish(event);
	}

}
