package dtu.infrastructure;

import java.util.ArrayList;

import dtu.domain.Token;


public interface ITokenRepository {

	public ArrayList<Token> get(String customerId);
	public Token create(String customerId);
	public boolean delete(String customerId);
	public Token getVerfiedToken(String tokenUuid);
}
