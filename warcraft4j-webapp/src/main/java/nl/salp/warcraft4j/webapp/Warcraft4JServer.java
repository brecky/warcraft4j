package nl.salp.warcraft4j.webapp;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import com.sun.jersey.guice.spi.container.GuiceComponentProviderFactory;
import nl.salp.warcraft4j.webapp.guice.Warcraft4JRestServicesModule;
import org.glassfish.grizzly.http.server.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

import static java.lang.String.format;

/**
 * TODO Document class.
 */
public class Warcraft4JServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Warcraft4JServer.class);

    static final URI BASE_URI = getBaseURI();

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost").port(9998).build();
    }

    public void startServer() {
        Warcraft4JRestServicesModule restServicesModule = new Warcraft4JRestServicesModule();
        Injector injector = Guice.createInjector(restServicesModule);

        Warcraft4JConfig config = injector.getInstance(Warcraft4JConfig.class);

        final ResourceConfig rc = new PackagesResourceConfig(config.getServicePackages());
        final IoCComponentProviderFactory ioc = new GuiceComponentProviderFactory(rc, injector);

        try {
            String baseUrl = BASE_URI + config.getRestServiceBaseUri();
            LOGGER.debug(format("Starting server for URL %s", baseUrl));
            final HttpServer server = GrizzlyServerFactory.createHttpServer(baseUrl, rc, ioc);
            server.start();

            Thread.currentThread().join();
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Warcraft4J server error", e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String... args) {
        new Warcraft4JServer().startServer();
    }

}
