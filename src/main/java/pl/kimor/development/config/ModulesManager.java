package pl.kimor.development.config;

import com.velocitypowered.api.command.CommandManager;
import pl.kimor.development.ProxyCore;
import pl.kimor.development.command.admin.*;
import pl.kimor.development.command.conversation.*;
import pl.kimor.development.command.players.PingCommand;
import pl.kimor.development.system.StaffChatListener;

import java.sql.SQLException;

public class ModulesManager {
    
    public static void loadEnabledModules() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        CommandManager manager = ProxyCore.getInstance().getCommandManager();;
        if (Config.getList(ConfigKey.MODULES_ENABLED).contains("PingCommand")){
            manager.register(manager.metaBuilder("ping").plugin(ProxyCore.getCore()).build(), new PingCommand());
        }
        if (Config.getList(ConfigKey.MODULES_ENABLED).contains("PlayersCommand")){
            manager.register(manager.metaBuilder("players").plugin(ProxyCore.getCore()).build(), new PlayersCommand());
        }
        if (Config.getList(ConfigKey.MODULES_ENABLED).contains("FindCommand")){
            manager.register(manager.metaBuilder("find").plugin(ProxyCore.getCore()).build(),new FindCommand());
        }
        if(Config.getList(ConfigKey.MODULES_ENABLED).contains("ServerCommand")){
            manager.register(manager.metaBuilder("server")
                            .aliases("srv")
                            .plugin(ProxyCore.getCore())
                            .build(),
                    new ServerCommand());
        }
        if(Config.getList(ConfigKey.MODULES_ENABLED).contains("AlertCommand")){
            manager.register(manager.metaBuilder("alert")
                            .aliases("broadcast")
                            .plugin(ProxyCore.getCore())
                            .build(),
                    new AlertCommand());
        }
        if(Config.getList(ConfigKey.MODULES_ENABLED).contains("SendCommand")){
            manager.register(manager.metaBuilder("send")
                            .plugin(ProxyCore.getCore())
                            .build(),
                    new SendCommand());
        }
        IgnoreList.initialise();
        if(Config.getList(ConfigKey.MODULES_ENABLED).contains("MsgCommand")){
            manager.register(manager.metaBuilder("message").plugin(ProxyCore.getCore()).aliases("msg","tell").build(),new MsgCommand());
        }
        if(Config.getList(ConfigKey.MODULES_ENABLED).contains("ReplyCommand")){
            manager.register(manager.metaBuilder("reply").plugin(ProxyCore.getCore()).aliases("r").build(),new ReplyCommand());
        }
        if(Config.getList(ConfigKey.MODULES_ENABLED).contains("IgnoreCommand")){
            manager.register(manager.metaBuilder("ignore").plugin(ProxyCore.getCore()).build(),new IgnoreCommand());
        }
        if(Config.getList(ConfigKey.MODULES_ENABLED).contains("UnignoreCommand")) {
            manager.register(manager.metaBuilder("unignore").plugin(ProxyCore.getCore()).build(), new UnignoreCommand());
        }
        if (Config.getList(ConfigKey.MODULES_ENABLED).contains("StaffChat")) {
            ProxyCore.getInstance().getEventManager().register(ProxyCore.getCore(), new StaffChatListener());
        }
    }
    
}
