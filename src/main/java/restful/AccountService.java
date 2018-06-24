package restful;

import java.math.BigDecimal;
import java.util.Optional;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
	
	private AccountDao dao;
	
	private static Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

	@Context
	public void setApp(Application app) {
		this.setDao(((App) app).getAccountDao());
	}
	
	public void setDao(AccountDao dao) {
		this.dao = dao;
	}


	@PUT
	public Response createAccount(
			@NotNull @Size(min=1) @PathParam("id") String id,
			@DefaultValue("0") @DecimalMin("0") @QueryParam("initialAmount") BigDecimal amount
			) {
		LOGGER.debug("creating account {}", id);
		Optional<Account> account = dao.createAccount(id, amount);
		return account.map(
				a->Response.ok().entity(a).type(MediaType.APPLICATION_JSON_TYPE).build()
		).orElseGet(
				()->Response.status(Status.CONFLICT).entity("already exists").type(MediaType.TEXT_PLAIN_TYPE).build()
		);

	}
	
	@GET
	public Response getAccount(@NotNull @Size(min=1) @PathParam("id") String id) {
		Optional<Account> account = dao.getAccount(id);
		return account.map(
				a->Response.ok().entity(a).type(MediaType.APPLICATION_JSON_TYPE).build()
		).orElseGet(
				() -> Response.status(Status.NOT_FOUND).entity("not found").type(MediaType.TEXT_PLAIN_TYPE).build()
		);

	}
	
}