package restful;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import restful.dao.AccountDao;
import restful.dto.Account;

public class AccountTransferServiceTest {
	
	AccountDao accountDao;
	AccountService service;
	
	@Before
	public void setUp() {
		accountDao = Mockito.mock(AccountDao.class);
		service = new AccountService();
		service.setDao(accountDao);
	}
	
	private Account createAccount() {
		Account acc = new Account();
		acc.setAmount(BigDecimal.ZERO);
		acc.setId("1");
		return acc;
	}

   @Test
   public void testCreateAccount() throws Exception
   {
	   Mockito.when(accountDao.createAccount(Mockito.anyString(), Mockito.any())).thenReturn(Optional.of(createAccount()));
	   Account account = (Account) service.createAccount("1", BigDecimal.ZERO).getEntity();
	   Assert.assertEquals(BigDecimal.ZERO, account.getAmount());
	   Assert.assertEquals("1", account.getId());
   }


}
