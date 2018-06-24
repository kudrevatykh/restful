package restful.dao;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import restful.dto.Account;

public class AccountDaoImpl implements AccountDao {
	
	private ConcurrentMap<String, Account> map = new ConcurrentHashMap<>();

	@Override
	public Account createAccount(String id, BigDecimal amount) {
		Account acc = new Account(id, amount);
		Account exists = map.putIfAbsent(id, acc);
		if(exists!=null) {
			throw new ResourceExistsException(id);
		}
		return acc;
	}

	@Override
	public Account getAccount(String id) {
		return map.get(id);
	}

}
