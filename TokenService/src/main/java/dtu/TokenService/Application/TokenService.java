package dtu.TokenService.Application;

import java.util.HashSet;

import dtu.TokenService.Domain.Token;
import dtu.TokenService.Infrastructure.AccountAccess;
import dtu.TokenService.Infrastructure.ITokenRepository;
import messaging.Event;
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

	public HashSet<Token> createTokens(String customerId, Integer numOfTokens, String sessionId) {
		
		Event event = accountAccess.customerVerificationRequest(customerId, sessionId);
		Event responseEvent;
		if(event.getArgument(0, boolean.class)) {
			responseEvent = generateTokens(customerId, numOfTokens, sessionId);
		}
		else {
			responseEvent = new Event("TokenCreationResponse." + sessionId, new Object[] { "Token creation failed. Either the customerId is not in our system, or you requested the wrong amount, or you have too many tokens already." });
		}
		messageQueue.publish(responseEvent);
		return tokenRepository.get(customerId);
	}

	public Event generateTokens(String customerId, Integer numOfTokens, String sessionId) {
		Event responseEvent;
		if(numOfTokens > 0 && numOfTokens < 6 && tokenRepository.get(customerId).size() < 2) {
			for( int i = 0; i < numOfTokens; i++) {
				tokenRepository.create(customerId);
			}
			responseEvent = new Event("TokenCreationResponse." + sessionId, new Object[] { tokenRepository.get(customerId) });
		}
		else {
			responseEvent = new Event("TokenCreationResponse." + sessionId, new Object[] { "Invalid token request" });
		}
		return responseEvent;
	}

	public HashSet<Token> getTokens(String customerId) {
		return tokenRepository.get(customerId);
	}

	public boolean deleteTokensForCustomer(String customerId) {
		return tokenRepository.delete(customerId);
	}
	
	public Token getVerifiedToken(String tokenUuid) {
		return tokenRepository.getVerfiedToken(tokenUuid);
	}

	public void getStatus() {
		Event event = new Event("TokenStatusResponse", new Object[] {"Token service ready"});
		messageQueue.publish(event);
	}
}
