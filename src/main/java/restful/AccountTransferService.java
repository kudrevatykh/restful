package restful;

import java.math.BigDecimal;
import java.util.Optional;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import restful.dao.AccountDao;
import restful.dto.Account;

@Path("/transfer")
public class AccountTransferService {
	
	private App app;

	@Context
	public void setApp(Application app) {
		this.app = (App) app;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response transfer(
			@NotNull @Size(min=1) @FormParam("from") String fromId,
			@NotNull @Size(min=1) @FormParam("to") String toId,
			@NotNull @DecimalMin(value = "0", inclusive=false) @FormParam("amount") BigDecimal amount) {
		Optional<Account> from = getDao().getAccount(fromId);
		if(!from.isPresent()) {
			return Response.status(Status.NOT_FOUND).entity("from account not found").build();
		}
		Optional<Account> to = getDao().getAccount(toId);
		if(!to.isPresent()) {
			return Response.status(Status.NOT_FOUND).entity("to account not found").build();
		}
		while(true) {
			if(from.get().getAmount().compareTo(amount)<0) {
				return Response.status(Status.CONFLICT).entity("not enough funds").build();
			}
			Account acc = from.get();
			Account result = new Account(acc.getId(), acc.getAmount().subtract(amount), acc.getVersion()+1);
			if(getDao().replace(acc, result)) {
				break;
			} else {
				from = getDao().getAccount(fromId);
			}
		}
		while(true) {
			Account acc = to.get();
			Account result = new Account(acc.getId(), acc.getAmount().add(amount), acc.getVersion()+1);
			if(getDao().replace(acc, result)) {
				break;
			} else {
				to = getDao().getAccount(toId);
			}
		}
		return Response.ok().build();
		
	}
	
	private AccountDao getDao() {
		return app.getAccountDao();
	}


}
