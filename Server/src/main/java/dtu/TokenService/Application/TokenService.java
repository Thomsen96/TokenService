package dtu.TokenService.Application;

import dtu.TokenService.Domain.Entities.Payment;
import dtu.TokenService.Domain.Repositories.Exceptions.ArgumentNullException;
import dtu.TokenService.Domain.Repositories.Exceptions.EntityNotFoundException;
import dtu.TokenService.Domain.Repositories.Interfaces.AccountRepository;
import dtu.TokenService.Domain.Repositories.Interfaces.PaymentRepository;
import dtu.ws.fastmoney.Account;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public class TokenService {


  private final PaymentRepository paymentRepository;
  private final AccountRepository accountRepository;
  private final BankService bankService;


  public TokenService(PaymentRepository paymentRepository, AccountRepository accountRepository) {
    this.paymentRepository = paymentRepository;
    this.bankService = new BankServiceService().getBankServicePort();
    this.accountRepository = accountRepository;
  }

  public boolean pay(int amount, String cid, String mid) throws BankServiceException_Exception {
    bankService.transferMoneyFromTo(cid, mid, new BigDecimal(String.valueOf(amount)), "Money was given and taken");
    paymentRepository.create(new Payment(amount, cid, mid));
    return true;
  }


  public Account getAccount(String id) throws ArgumentNullException, EntityNotFoundException, BankServiceException_Exception {
    return accountRepository.get(id);
  }

  public Collection<Account> getAllAccounts() {
    return accountRepository.getAll();
  }

  public Account deleteAccount(String id) {
    return accountRepository.delete(id);
  }

  public Account createAccount(Account entity) {
    return accountRepository.create(entity);
  }

  public List<String> getTokens(Integer int1, String customerId) {
    return null;
  }
}
