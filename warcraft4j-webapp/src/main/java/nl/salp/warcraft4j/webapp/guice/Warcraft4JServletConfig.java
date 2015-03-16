package nl.salp.warcraft4j.webapp.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import nl.salp.warcraft4j.webapp.guice.Warcraft4JRestServicesModule;

import static java.lang.String.format;

/**
 * Guice web configuration for the Warcraft4J web application.
 *
 * @author Barre Dijkstra
 */
public class Warcraft4JServletConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new Warcraft4JRestServicesModule());
    }
}