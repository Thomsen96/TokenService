package dtu.TokenService.Infrastructure;

import java.util.ArrayList;
import java.util.HashSet;

import dtu.TokenService.Domain.Token;


public interface ITokenRepository {

	public ArrayList<Token> get(String customerId);
	public Token create(String customerId);
	public boolean delete(String customerId);
	public Token getVerfiedToken(String tokenUuid);
}
