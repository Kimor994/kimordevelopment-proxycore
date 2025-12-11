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
import pl.kimor.development.system.StaffChatListener;

import java.io.IOException;
import java.io.InputStream;
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
    private static long start = System.currentTimeMillis();
    public static long getStartTime(){
        return start;
    }
    private final static int version = 1;

    private static Config config;

    private static ProxyServer srv;
    private static Path dd;

    public static Path getDataDir() {
        return dd;
    }

    public static ProxyServer getInstance() {
        return srv;
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
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(file).build();

        Config.initialise(loader);
    }
    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        logger.info("Loading ProxyCore version "+version);
        CommandManager manager = getInstance().getCommandManager();
        manager.register(manager.metaBuilder("core-reload").plugin(this).build(),new ReloadCommand());
        manager.register(manager.metaBuilder("ping").plugin(this).build(), new PingCommand());
        manager.register(manager.metaBuilder("players").plugin(this).build(), new PlayersCommand());
        manager.register(manager.metaBuilder("find").plugin(this).build(),new FindCommand());
        manager.register(manager.metaBuilder("server")
                .aliases("srv")
                .plugin(this)
                .build(),
                new ServerCommand());
        manager.register(manager.metaBuilder("alert")
                        .aliases("broadcast")
                        .plugin(this)
                        .build(),
                new AlertCommand());
        manager.register(manager.metaBuilder("send")
                        .plugin(this)
                        .build(),
                new SendCommand());
        IgnoreList.initialise();
        manager.register(manager.metaBuilder("message").plugin(this).aliases("msg","tell").build(),new MsgCommand());
        manager.register(manager.metaBuilder("reply").plugin(this).aliases("r").build(),new ReplyCommand());
        manager.register(manager.metaBuilder("ignore").plugin(this).build(),new IgnoreCommand());
        manager.register(manager.metaBuilder("unignore").plugin(this).build(),new UnignoreCommand());
        getInstance().getEventManager().register(this, new StaffChatListener());
    }
}
