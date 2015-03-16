package nl.salp.warcraft4j.webapp.guice;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import nl.salp.warcraft4j.webapp.Warcraft4JConfig;
import nl.salp.warcraft4j.webapp.Warcraft4JDefaultConfig;
import nl.salp.warcraft4j.webapp.system.SystemService;
import nl.salp.warcraft4j.webapp.system.SystemServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

/**
 * TODO Document class.
 */
public class Warcraft4JRestServicesModule extends ServletModule {
    /** The logger instance. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Warcraft4JRestServicesModule.class);

    private final Warcraft4JConfig config;

    public Warcraft4JRestServicesModule() {
        config = new Warcraft4JDefaultConfig();
    }

    @Override
    protected void configureServlets() {
        bind(Warcraft4JConfig.class).toInstance(config);
        bind(SystemService.class).to(SystemServiceImpl.class);

        ResourceConfig rc = new PackagesResourceConfig(config.getServicePackages());
        for (Class<?> resource : rc.getClasses()) {
            LOGGER.debug(format("Binding service package resource %s", resource.getName()));
            bind(resource);
        }

        String restApiUri = format("%s*", config.getRestServiceBaseUri());
        LOGGER.debug(format("Serving '%s' with GuiceContainer.class", restApiUri));
        serve(restApiUri).with(GuiceContainer.class);
    }

}
