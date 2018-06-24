package restful.dao;

import java.math.BigDecimal;

import restful.dto.Account;

public interface AccountDao {
	
	public Account createAccount(String id, BigDecimal amount);
	
	public Account getAccount(String id);

}
