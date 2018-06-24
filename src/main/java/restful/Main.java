package restful;

import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.slf4j.LoggerFactory;

import io.undertow.Undertow;
import restful.dao.AccountDaoImpl;

public class Main {

	public static void main(String[] args) {
		UndertowJaxrsServer server = new UndertowJaxrsServer();
        Undertow.Builder serverBuilder = Undertow.builder().addHttpListener(8080, "0.0.0.0");
        server.start(serverBuilder);
        LoggerFactory.getLogger(Main.class).info("started server");
        try {
            deploy(server);
        } catch (Exception e) {
        	server.stop();
        	throw e;
        }
        
	}
	private static void deploy(UndertowJaxrsServer server) {
		App app = new App();
		app.setAccountDao(new AccountDaoImpl());
        server.deploy(app);
	}

}
