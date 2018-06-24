package restful.dao;

import java.math.BigDecimal;
import java.util.Optional;

import restful.dto.Account;

public interface AccountDao {
	
	public Optional<Account> createAccount(String id, BigDecimal amount);
	
	public Optional<Account> getAccount(String id);

	boolean replace(Account oldAccount, Account newAccount);

}
