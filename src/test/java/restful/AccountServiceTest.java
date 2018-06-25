package restful;

import java.math.BigDecimal;
import java.util.Optional;

import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import restful.dao.AccountDao;
import restful.dto.Account;

public class AccountServiceTest {
	
	AccountDao accountDao;
	AccountService service;
	
	@Before
	public void setUp() {
		accountDao = Mockito.mock(AccountDao.class);
		service = new AccountService();
		service.setDao(accountDao);
	}
	
	private Optional<Account> createAccount() {
		Account acc = new Account("1", BigDecimal.ZERO, 0);
		return Optional.of(acc);
	}

   @Test
   public void testCreateAccount() throws Exception
   {
	   Mockito.when(accountDao.createAccount(Mockito.anyString(), Mockito.any())).thenReturn(createAccount());
	   Account account = (Account) service.createAccount("1", BigDecimal.ZERO).getEntity();
	   Assert.assertEquals(BigDecimal.ZERO, account.getAmount());
	   Assert.assertEquals("1", account.getId());
   }

   @Test
   public void testCreateWhenAccountExists() throws Exception
   {
	   Mockito.when(accountDao.createAccount(Mockito.anyString(), Mockito.any())).thenReturn(Optional.empty());
	   Status status = service.createAccount("1", BigDecimal.ZERO).getStatusInfo().toEnum();
	   Assert.assertEquals(Status.CONFLICT, status);
   }
   
   @Test
   public void testGet() throws Exception
   {
	   Mockito.when(accountDao.getAccount(Mockito.anyString())).thenReturn(createAccount());
	   Account account = (Account) service.getAccount("1").getEntity();
	   Assert.assertEquals(BigDecimal.ZERO, account.getAmount());
	   Assert.assertEquals("1", account.getId());
   }
   
   @Test
   public void testGetWhenNotExists() throws Exception
   {
	   Mockito.when(accountDao.getAccount(Mockito.anyString())).thenReturn(Optional.empty());
	   Status status = service.getAccount("1").getStatusInfo().toEnum();
	   Assert.assertEquals(Status.NOT_FOUND, status);
   }

}
