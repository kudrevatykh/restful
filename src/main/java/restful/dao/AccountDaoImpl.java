package restful.dao;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import restful.dto.Account;

public class AccountDaoImpl implements AccountDao {
	
	private ConcurrentMap<String, Account> map = new ConcurrentHashMap<>();

	@Override
	public Optional<Account> createAccount(String id, BigDecimal amount) {
		Account acc = new Account(id, amount, 1L);
		Account exists = map.putIfAbsent(id, acc);
		if(exists!=null) {
			return Optional.empty();
		}
		return Optional.of(acc);
	}

	@Override
	public Optional<Account> getAccount(String id) {
		return Optional.ofNullable(map.get(id));
	}
	
	@Override
	public boolean replace(Account oldAccount, Account newAccount) {
		return map.compute(oldAccount.getId(),
				(k, v)->oldAccount.getVersion()==v.getVersion()?newAccount:oldAccount
			) == newAccount;
	}

}
