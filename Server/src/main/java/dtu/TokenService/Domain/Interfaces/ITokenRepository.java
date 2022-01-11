package dtu.TokenService.Domain.Interfaces;

import java.util.List;
import java.util.Collection;

import dtu.TokenService.Domain.Entities.Token;


public interface ITokenRepository {

	public List<Token> get(String customerId);
	public Token create(String customerId);

	public Collection<Token> getAll();
	public Token delete(String id);

}
