package restful;

import java.math.BigDecimal;
import java.util.Optional;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import restful.dao.AccountDao;
import restful.dto.Account;

@Path("/account/{id}")
public class AccountService {
	
	private App app;
	
	private static Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

	@Context
	public void setApp(Application app) {
		this.app = (App) app;
	}

	@PUT
	public Response createAccount(
			@PathParam("id") String id,
			@DefaultValue("0") @QueryParam("initialAmount") BigDecimal amount
			) {
		LOGGER.debug("creating account {}", id);
		Optional<Account> account = getDao().createAccount(id, amount);
		return account.map(
				a->Response.ok().entity(a).type(MediaType.APPLICATION_JSON_TYPE).build()
		).orElseGet(
				()->Response.status(Status.CONFLICT).entity("already exists").type(MediaType.TEXT_PLAIN_TYPE).build()
		);

	}
	
	@GET
	public Response getAccount(@PathParam("id") String id) {
		Optional<Account> account = getDao().getAccount(id);
		return account.map(
				a->Response.ok().entity(a).type(MediaType.APPLICATION_JSON_TYPE).build()
		).orElseGet(
				() -> Response.status(Status.NOT_FOUND).entity("not found").type(MediaType.TEXT_PLAIN_TYPE).build()
		);

	}
	
	private AccountDao getDao() {
		return app.getAccountDao();
	}

}