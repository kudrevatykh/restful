package restful;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.test.TestPortProvider;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import restful.dao.AccountDao;
import restful.dto.Account;

public class AppTest {
	
	private static UndertowJaxrsServer server;

	   @BeforeClass
	   public static void init() throws Exception
	   {
	      server = new UndertowJaxrsServer().start();
	   }

	   @AfterClass
	   public static void stop() throws Exception
	   {
	      server.stop();
	   }

	   @Test
	   public void testApplicationPath() throws Exception
	   {
		  App app = new App();
		  AccountDao dao = Mockito.mock(AccountDao.class);
		  Mockito.when(dao.createAccount(Mockito.anyString(), Mockito.any())).thenReturn(Optional.of(new Account()));
		  app.setAccountDao(dao);
	      server.deploy(app);
	      Client client = ClientBuilder.newClient();
	      StatusType status = client.target(TestPortProvider.generateURL("/account/1"))
	                         .request().put(null).getStatusInfo();
	      Assert.assertEquals(Status.OK, status.toEnum());
	      status  = client.target(TestPortProvider.generateURL("/account/2?initialAmount=100"))
          .request().put(null).getStatusInfo();
	      Assert.assertEquals(Status.OK, status.toEnum());
	      
	      Mockito.when(dao.getAccount(Mockito.anyString())).thenReturn(Optional.of(new Account("1", BigDecimal.TEN, 1)));
	      Mockito.when(dao.replace(Mockito.any(), Mockito.any())).thenReturn(true);
	      
	      Map<String, String> params = new HashMap<>();
	      params.put("from", "2");
	      params.put("to", "1");
	      params.put("amount", "10");
	      Response response = client.target(TestPortProvider.generateURL("/transfer"))
          .request().post(Entity.form(new MultivaluedHashMap<>(params)));
          status = response.getStatusInfo();
	      Assert.assertEquals(response.readEntity(String.class) ,Status.OK, status.toEnum());
	      client.close();
	      Mockito.verify(dao, Mockito.times(2)).createAccount(Mockito.anyString(), Mockito.any());
	      Mockito.verify(dao, Mockito.times(2)).getAccount(Mockito.anyString());
	      Mockito.verify(dao, Mockito.times(2)).replace(Mockito.any(), Mockito.any());
	      Mockito.verifyNoMoreInteractions(dao);
	   }


}
