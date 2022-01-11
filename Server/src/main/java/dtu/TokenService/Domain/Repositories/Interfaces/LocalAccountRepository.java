package dtu.TokenService.Domain.Repositories.Interfaces;

import dtu.TokenService.Domain.Repositories.Exceptions.ArgumentNullException;
import dtu.TokenService.Domain.Repositories.Exceptions.EntityNotFoundException;
import dtu.ws.fastmoney.Account;

import java.util.Collection;
import java.util.HashMap;

public class LocalAccountRepository implements AccountRepository{

    private static final HashMap<String, Account> accounts = new HashMap<>();

    @Override
    public Account get(String id) throws EntityNotFoundException, ArgumentNullException {
        return accounts.get(id);
    }

    @Override
    public Collection<Account> getAll() {
        return accounts.values();
    }

    @Override
    public Account create(Account account) {
        return accounts.put(account.getId(), account);
    }

    @Override
    public Account delete(String id) {
        return accounts.remove(id);
    }
}
