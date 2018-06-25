package restful.dao;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import restful.dao.AccountDao;
import restful.dao.AccountDaoImpl;
import restful.dto.Account;

public class AccountDaoImplTest {
	
	private AccountDao accountDao = new AccountDaoImpl();
	
	@Test
	public void testDuplicateCreation() {
		Assert.assertTrue(accountDao.createAccount("1", null).isPresent());
		Assert.assertFalse(accountDao.createAccount("1", null).isPresent());
	}
	
	@Test
	public void testGet() {
		Assert.assertFalse(accountDao.getAccount("1").isPresent());
		Assert.assertFalse(accountDao.getAccount("2").isPresent());
		accountDao.createAccount("1", null);
		Assert.assertTrue(accountDao.getAccount("1").isPresent());
		Assert.assertFalse(accountDao.getAccount("2").isPresent());
		accountDao.createAccount("2", null);
		Assert.assertTrue(accountDao.getAccount("2").isPresent());
	}
	
	@Test
	public void testReplace() {
		accountDao.createAccount("1", BigDecimal.ZERO);
		Account account = accountDao.getAccount("1").get();
		Assert.assertTrue( accountDao.replace(new Account(account.getId(), account.getAmount(), account.getVersion()  ), account));
		Assert.assertFalse(accountDao.replace(new Account(account.getId(), account.getAmount(), account.getVersion()+1), account));
	}

}
