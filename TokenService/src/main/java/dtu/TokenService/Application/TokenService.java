package dtu.TokenService.Application;

import java.util.HashSet;
import java.util.List;

import dtu.TokenService.Domain.Entities.Token;
import dtu.TokenService.Domain.Interfaces.ITokenRepository;

public class TokenService {

	private ITokenRepository tokenRepository;



	public TokenService(ITokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}
	
	public Token createAndReturnSingleToken(String customerId, Integer numOfTokens) {
		if(numOfTokens > 0 && numOfTokens < 6 && tokenRepository.get(customerId).size() < 2) {
			tokenRepository.create(customerId);
		}
		return tokenRepository.get(customerId).iterator().next();
	}

	public HashSet<Token> createTokens(String customerId, Integer numOfTokens) {
		if(numOfTokens > 0 && numOfTokens < 6 && tokenRepository.get(customerId).size() < 2) {
			for( int i = 0; i < numOfTokens; i++) {
				tokenRepository.create(customerId);
			}
		}
		return tokenRepository.get(customerId);
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
}
