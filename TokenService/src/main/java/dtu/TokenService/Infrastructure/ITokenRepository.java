package dtu.TokenService.Infrastructure;

import java.util.HashSet;

import dtu.TokenService.Domain.Token;


public interface ITokenRepository {

	public HashSet<Token> get(String customerId);
	public Token create(String customerId);

	public HashSet<Token> getAll();
	public boolean delete(String customerId);
	public Token getVerfiedToken(String tokenUuid);
}
