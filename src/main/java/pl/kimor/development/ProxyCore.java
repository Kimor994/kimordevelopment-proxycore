package pl.kimor.development;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import pl.kimor.development.command.admin.*;
import pl.kimor.development.command.conversation.*;
import pl.kimor.development.command.players.PingCommand;
import pl.kimor.development.config.Config;
import pl.kimor.development.config.ConfigKey;
import pl.kimor.development.config.ModulesManager;
import pl.kimor.development.system.StaffChatListener;
import pl.kimor.development.updater.Updater;

import javax.annotation.processing.SupportedSourceVersion;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

@Plugin(
        id = "kimordevelopment-velocity-proxy-core",
        name = "kimordevelopment-velocity-proxy-core",
        version = "1.0.0",
        description = "Plugin implementujący wiele niezbędnych rzeczy do serwera velocity",
        authors = {"kimor__"}
)
public class ProxyCore {
    private static Config config;

    private static ProxyServer srv;
    private static Path dd;

    public static Path getDataDir() {
        return dd;
    }

    public static ProxyServer getInstance() {
        return srv;
    }
    private static ProxyCore core;
    public  static ProxyCore getCore(){
        return core;
    }
    @Inject
    public ProxyCore(ProxyServer server, @DataDirectory Path dataDirectory) throws IOException {
        srv=server;
        dd=dataDirectory;
        if (Files.notExists(dd)){
            Files.createDirectory(dd);
        }
        Path file = dd.resolve("config.yml");
        if (!Files.exists(file)){
            try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.yml")){
                Files.copy(in,file);
            }
        }
        core=this;
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(file).build();
        Config.initialise(loader);
    }
    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        Updater updater = new Updater();
        if (updater.updateAvailable()){
            logger.info("There is an update available! ("+updater.getLatestVersion()+"). Install it with: /core-update");
        } else {
            logger.info("There is no update available. You are using latest version.");
        }
        CommandManager manager = getInstance().getCommandManager();
        manager.register(manager.metaBuilder("core-reload").plugin(this).build(),new ReloadCommand());
        manager.register(manager.metaBuilder("core-update").plugin(this).build(),new UpdateCommand());
        ModulesManager.loadEnabledModules();
    }
}
