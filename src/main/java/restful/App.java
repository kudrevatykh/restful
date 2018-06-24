package restful;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import restful.dao.AccountDao;

public class App extends Application {
	
	private AccountDao accountDao;
	
	 @Override
     public Set<Class<?>> getClasses()
     {
        HashSet<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(AccountService.class);
        return classes;
     }

	public AccountDao getAccountDao() {
		return accountDao;
	}

	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

}
