package dtu.TokenService.Domain.Repositories.Interfaces;

import dtu.TokenService.Domain.Entities.Payment;
import dtu.TokenService.Domain.Repositories.Exceptions.ArgumentNullException;
import dtu.TokenService.Domain.Repositories.Exceptions.EntityNotFoundException;
import dtu.ws.fastmoney.Account;

import java.util.Collection;

public interface AccountRepository {
  public Account get(String id) throws EntityNotFoundException, ArgumentNullException;
  public Collection<Account> getAll();
  public Account create(Account account);
  public Account delete(String id);
}
