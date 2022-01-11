package dtu.TokenService.Application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import dtu.TokenService.Domain.Entities.Token;
import dtu.TokenService.Domain.Interfaces.ITokenRepository;

public class TokenService {

	private ITokenRepository tokenRepository;



	public TokenService(ITokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}

	public List<Token> createTokens(Integer numOfTokens, String customerId) {
		List<Token> tokens = tokenRepository.get(customerId);
		if(numOfTokens > 0 && numOfTokens < 6 && tokens.size() < 2) {
			for( int i = 0; i < numOfTokens; i++) {
				tokens.add(tokenRepository.create(customerId));
			}
		}
		return tokens;
	}

	public List<Token> getTokens(String customerId) {
		return tokenRepository.get(customerId);
	}

	public boolean deleteTokensForCustomer(String customerId) {
		return tokenRepository.delete(customerId);
	}





	//public boolean pay(int amount, String cid, String mid) throws BankServiceException_Exception {
	//bankService.transferMoneyFromTo(cid, mid, new BigDecimal(String.valueOf(amount)), "Money was given and taken");
	//paymentRepository.create(new Payment(amount, cid, mid));
	//return true;
	//}
	//
	//
	//public Account getAccount(String id) throws ArgumentNullException, EntityNotFoundException, BankServiceException_Exception {
	//return accountRepository.get(id);
	//}
	//
	//public Collection<Account> getAllAccounts() {
	//return accountRepository.getAll();
	//}
	//
	//public Account deleteAccount(String id) {
	//return accountRepository.delete(id);
	//}
	//
	//public Account createAccount(Account entity) {
	//return accountRepository.create(entity);
	//}
}
