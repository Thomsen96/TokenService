package dtu.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dtu.domain.Token;

public class LocalTokenRepository implements ITokenRepository {

	List<Token> tokens = new ArrayList<>();
	HashMap<String, Token> tokenHashMap = new HashMap<>();
	HashMap<String, ArrayList<Token>> customerHashMap = new HashMap<>();


	@Override
	public ArrayList<Token> get(String customerId) {
		if(!customerHashMap.containsKey(customerId)) {
			customerHashMap.put(customerId, new ArrayList<Token>());
		}
		return customerHashMap.get(customerId);
	}

	@Override
	public Token create(String customerId) {
		Token token = new Token(customerId);
		tokenHashMap.put(token.getUuid(), token);
		if (customerHashMap.containsKey(customerId)) {
			customerHashMap.get(customerId).add(token);
		}
		else {
			var tokenSet = new ArrayList<Token>();
			tokenSet.add(token);
			customerHashMap.put(customerId, tokenSet);
		}
		return token;
	}

	@Override
	public boolean delete(String customerId) {
		var tokensToRemove = customerHashMap.remove(customerId);
		for (Token token : tokensToRemove) {
			tokenHashMap.remove(token.getUuid());
		}
		return true;
	}

	@Override
	public Token getVerfiedToken(String tokenUuid) {
		try {
			var token = tokenHashMap.remove(tokenUuid);
			customerHashMap.get(token.getCustomerId()).remove(token);
			return token;
		} catch (Exception e) {
			return new Token(false);
		}
	}
}
