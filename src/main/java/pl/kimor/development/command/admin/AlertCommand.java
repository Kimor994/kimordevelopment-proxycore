package pl.kimor.development.command.admin;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import pl.kimor.development.config.Config;
import pl.kimor.development.config.ConfigKey;
import pl.kimor.development.ProxyCore;
import pl.kimor.development.config.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlertCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        if (!Utils.permitted(invocation.source(),"kimordevelopment.proxy.alert")){
            return;
        }
        String[] args = invocation.arguments();
        String finalMsg = "";
        String serverName = args[0];
        for(int i=1; i<args.length; i++){
            finalMsg=finalMsg+" "+args[i];
        }
        Component component = Utils.coloredText(finalMsg);
        switch (serverName){
            case "all", "*", "proxy":
                if(Config.getBoolean(ConfigKey.CFG_ALERTS_SCREEN_ENABLE)){
                    ProxyCore.getInstance().showTitle(Title.title(
                            Utils.coloredText(Config.getString(ConfigKey.CFG_ALERTS_SCREEN_TITLE).replaceAll("%message%",finalMsg)),
                            Utils.coloredText(Config.getString(ConfigKey.CFG_ALERTS_SCREEN_SUBTITLE).replaceAll("%message%",finalMsg))
                    ));
                }
                if (Config.getBoolean(ConfigKey.CFG_ALERTS_CHAT_ENABLE)){
                    for(Object o : Config.getList(ConfigKey.CFG_ALERTS_CHAT_LINES)){
                        String msg = o.toString().replaceAll("%message%",finalMsg);
                        ProxyCore.getInstance().sendMessage(Utils.coloredText(msg));
                    }
                }
                break;
            default:
                RegisteredServer server;
                if (ProxyCore.getInstance().getServer(serverName).isEmpty()){
                    invocation.source().sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_GLOBAL_BAD_SERVER)));
                    return;
                } else {
                    server= ProxyCore.getInstance().getServer(serverName).get();
                }
                if (Config.getBoolean(ConfigKey.CFG_ALERTS_SCREEN_ENABLE)){
                    server.showTitle(Title.title(
                            Utils.coloredText(Config.getString(ConfigKey.CFG_ALERTS_SCREEN_TITLE).replaceAll("%message%",finalMsg)),
                            Utils.coloredText(Config.getString(ConfigKey.CFG_ALERTS_SCREEN_SUBTITLE).replaceAll("%message%",finalMsg))
                    ));
                }
                if (Config.getBoolean(ConfigKey.CFG_ALERTS_CHAT_ENABLE)){
                    for(Object o : Config.getList(ConfigKey.CFG_ALERTS_CHAT_LINES)){
                        String msg = o.toString().replaceAll("%message%",finalMsg);
                        server.sendMessage(Utils.coloredText(msg));
                    }
                }
        }
    }
    @Override
    public List<String> suggest(Invocation invocation) {
        if (invocation.arguments().length == 1){
            String server = invocation.arguments()[0];
            List<String> suggestions = new ArrayList<>();
            suggestions.add("*");
            suggestions.add("all");
            suggestions.add("proxy");
            for(RegisteredServer s : ProxyCore.getInstance().getAllServers()){
                if (s.getServerInfo().getName().toLowerCase().startsWith(server.toLowerCase())){
                    suggestions.add(s.getServerInfo().getName());
                }
            }
            return suggestions;
        } else if (invocation.arguments().length == 0){
            List<String> options = ProxyCore.getInstance().getAllServers().stream().map(server -> server.getServerInfo().getName()).collect(Collectors.toList());
            options.add("*");
            options.add("all");
            options.add("proxy");
            return options;
        }
        return List.of();
    }
}
