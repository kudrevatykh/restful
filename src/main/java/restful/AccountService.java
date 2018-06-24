package restful;

import java.math.BigDecimal;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import restful.dao.AccountDao;
import restful.dto.Account;

@Path("/account/{id}")
public class AccountService {
	
	private @Context Application app;

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response createAccount(@PathParam("id") String id,
			@DefaultValue("0") @QueryParam("initialAmount") BigDecimal amount) {
		Account account = getDao().createAccount(id, amount);
		return Response.ok().entity(account).build();

	}
	
	@GET
	public Response getAccount(@PathParam("id") String id) {
		Account account = getDao().getAccount(id);
		if(account!=null) {
			return Response.ok().entity(account).type(MediaType.APPLICATION_JSON_TYPE).build();
		} else {
			return Response.status(Status.NOT_FOUND).entity("not found").type(MediaType.TEXT_PLAIN_TYPE).build();
		}

	}
	
	private AccountDao getDao() {
		return ((App)app).getAccountDao();
	}

}