package dtu.TokenService.Domain.Repositories;

import dtu.TokenService.Domain.Entities.Token;
import dtu.TokenService.Domain.Interfaces.ITokenRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class LocalTokenRepository implements ITokenRepository {

	List<Token> tokens = new ArrayList<>();

	@Override
	public List<Token> get(String customerId) {
		List<Token> customerTokens = tokens.stream().filter(cid -> cid.getCustomerId().equals(customerId))
				.collect(Collectors.toList());

		return customerTokens;
	}

	@Override
	public Token create(String customerId) {
		Token token = new Token(customerId);
		tokens.add(token);
		return token;
	}

	@Override
	public Collection<Token> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(String customerId) {
		List<Token> customerTokens = tokens.stream().filter(cid -> cid.getCustomerId().equals(customerId))
				.collect(Collectors.toList());
		if (customerTokens.size() > 0) {
			tokens.removeAll(customerTokens);
			return true;
		}
		return false;
	}

	@Override
	public Boolean verifyToken(String token) {
		List<Token> customerTokens = tokens.stream().filter(t -> t.getUuid().equals(token))
				.collect(Collectors.toList());
		if (customerTokens.size() > 0) {
			return true;
		}
		return false;
	}
}
