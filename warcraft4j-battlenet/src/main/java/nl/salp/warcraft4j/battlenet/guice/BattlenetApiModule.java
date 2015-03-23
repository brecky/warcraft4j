package nl.salp.warcraft4j.battlenet.guice;

import com.google.inject.AbstractModule;
import nl.salp.warcraft4j.battlenet.BattlenetApiConfig;
import nl.salp.warcraft4j.battlenet.api.BattlenetHttpApi;
import nl.salp.warcraft4j.battlenet.api.BattlenetApi;
import nl.salp.warcraft4j.battlenet.service.BattlenetPlayerCharacterService;
import nl.salp.warcraft4j.service.PlayerCharacterService;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Guice module for the Battle.NET API dependencies.
 * <p/>
 * All core service implementations are bound with a {@link Battlenet} annotation.
 *
 * @author Barre Dijkstra
 */
public class BattlenetApiModule extends AbstractModule {
    /** The location of the Battle.NET API config file. */
    private final String configFile;


    /**
     * Create a new Guice Battle.NET API module instance using the default config file location.
     */
    public BattlenetApiModule() {
        this.configFile = null;
    }

    /**
     * Create a new Guice Battle.NET API module instance.
     *
     * @param configFile The location of the Battle.NET API config file.
     */
    public BattlenetApiModule(String configFile) {
        this.configFile = configFile;
    }

    @Override
    protected void configure() {
        if (isEmpty(configFile)) {
            bind(BattlenetApiConfig.class).toProvider(BattlenetApiConfigFileProvider.class);
        } else {
            bind(BattlenetApiConfig.class).toProvider(new BattlenetApiConfigFileProvider(configFile));
        }
        bind(BattlenetApi.class).to(BattlenetHttpApi.class);

        bind(PlayerCharacterService.class).annotatedWith(Battlenet.class).to(BattlenetPlayerCharacterService.class);
    }
}
