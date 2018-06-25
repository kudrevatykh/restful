package restful;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import restful.dao.AccountDao;
import restful.dto.Account;

public class AccountTransferServiceTest {
	
	AccountDao accountDao;
	AccountTransferService service;
	
	@Before
	public void setUp() {
		accountDao = Mockito.mock(AccountDao.class);
		service = new AccountTransferService();
		service.setDao(accountDao);
	}
	
	private Optional<Account> createAccount(long amount, long version) {
		Account acc = new Account("1", BigDecimal.valueOf(amount), version);
		return Optional.of(acc);
	}

   @Test
   public void testTransferSameAccount() throws Exception
   {
	   Status status = service.transfer("1", "1", BigDecimal.valueOf(100)).getStatusInfo().toEnum();
	   Assert.assertEquals(Status.BAD_REQUEST, status);
   }

	
   @Test
   public void testAccountNotFound() throws Exception
   {
	   Mockito.when(accountDao.getAccount(Mockito.anyString())).thenReturn(Optional.empty());
	   Mockito.when(accountDao.getAccount(Mockito.eq("1"))).thenReturn(Optional.empty()).thenReturn(createAccount(100, 1));
	   Status status = service.transfer("1", "2", BigDecimal.valueOf(100)).getStatusInfo().toEnum();
	   Assert.assertEquals(Status.NOT_FOUND, status);
	   status = service.transfer("1", "2", BigDecimal.valueOf(100)).getStatusInfo().toEnum();
	   Assert.assertEquals(Status.NOT_FOUND, status);
	   Mockito.verify(accountDao, Mockito.atLeast(2)).getAccount(Mockito.eq("1"));
	   Mockito.verify(accountDao, Mockito.atLeast(1)).getAccount(Mockito.eq("2"));
	   Mockito.verifyNoMoreInteractions(accountDao);
   }
   
   @Test
   public void testAccountNotEnoughFunds() throws Exception
   {
	   Mockito.when(accountDao.getAccount(Mockito.anyString())).thenReturn(createAccount(0, 1));
	   Mockito.when(accountDao.getAccount(Mockito.eq("1"))).thenReturn(createAccount(10, 1)).thenReturn(createAccount(1, 1));
	   Mockito.when(accountDao.replace(Mockito.any(), Mockito.any())).thenReturn(false);
	   Status status = service.transfer("1", "2", BigDecimal.valueOf(5)).getStatusInfo().toEnum();
	   Assert.assertEquals(Status.CONFLICT, status);
	   Mockito.verify(accountDao, Mockito.atLeast(2)).getAccount(Mockito.eq("1"));
	   Mockito.verify(accountDao, Mockito.atLeast(1)).getAccount(Mockito.eq("2"));
	   Mockito.verify(accountDao, Mockito.times(1)).replace(Mockito.any(), Mockito.any());
	   Mockito.verifyNoMoreInteractions(accountDao);
   }
   
   @Test
   public void testSuccess() throws Exception
   {
	   Mockito.when(accountDao.getAccount(Mockito.eq("1"))).thenReturn(createAccount(10, 1)).thenReturn(createAccount(15, 5));
	   Mockito.when(accountDao.getAccount(Mockito.eq("2"))).thenReturn(createAccount(0, 1)).thenReturn(createAccount(1, 2));
	   Mockito.when(accountDao.replace(Mockito.any(), Mockito.any())).thenReturn(false, true, false, true);
	   Status status = service.transfer("1", "2", BigDecimal.valueOf(5)).getStatusInfo().toEnum();
	   Assert.assertEquals(Status.OK, status);
	   Mockito.verify(accountDao, Mockito.atLeast(2)).getAccount(Mockito.eq("1"));
	   Mockito.verify(accountDao, Mockito.atLeast(2)).getAccount(Mockito.eq("2"));
	   ArgumentCaptor<Account> oldValueCaptor = ArgumentCaptor.forClass(Account.class);
	   ArgumentCaptor<Account> newValueCaptor = ArgumentCaptor.forClass(Account.class);
	   Mockito.verify(accountDao, Mockito.times(4)).replace(oldValueCaptor.capture(), newValueCaptor.capture());
	   List<Account> oldAccounts = oldValueCaptor.getAllValues();
	   List<Account> newAccounts = newValueCaptor.getAllValues();
	   Assert.assertEquals(1, oldAccounts.get(0).getVersion());
	   Assert.assertEquals(2, newAccounts.get(0).getVersion());
	   Assert.assertEquals(5, oldAccounts.get(1).getVersion());
	   Assert.assertEquals(6, newAccounts.get(1).getVersion());
	   Assert.assertEquals(1, oldAccounts.get(2).getVersion());
	   Assert.assertEquals(2, newAccounts.get(2).getVersion());
	   Assert.assertEquals(2, oldAccounts.get(3).getVersion());
	   Assert.assertEquals(3, newAccounts.get(3).getVersion());
	   

	   Assert.assertEquals(10 - 5, newAccounts.get(0).getAmount().intValue());
	   Assert.assertEquals(15 - 5, newAccounts.get(1).getAmount().intValue());
	   Assert.assertEquals(0 + 5, newAccounts.get(2).getAmount().intValue());
	   Assert.assertEquals(1 + 5, newAccounts.get(3).getAmount().intValue());
	   
	   for(int i = 0;i<oldAccounts.size();++i) {
		   Assert.assertEquals(newAccounts.get(i).getId(), oldAccounts.get(i).getId());
	   }
	   
	   Mockito.verifyNoMoreInteractions(accountDao);
   }


}
