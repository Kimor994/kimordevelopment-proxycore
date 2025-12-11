package pl.kimor.development.command.admin;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import pl.kimor.development.Config;
import pl.kimor.development.ConfigKey;
import pl.kimor.development.ProxyCore;
import pl.kimor.development.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ServerCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        if (!(source instanceof Player)){
            source.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_GLOBAL_EXECUTABLE_BY_PL)));
            return;
        }
        if (!Utils.permitted(source,"kimordevelopment.proxy.server")){
            return;
        }
        Player player = (Player) source;
        if (invocation.arguments().length==1){
            Optional<RegisteredServer> server = ProxyCore.getInstance().getServer(invocation.arguments()[0]);
            if (server.isPresent()){
                player.createConnectionRequest(server.get()).connect();
            } else {
                player.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_GLOBAL_BAD_SERVER)));
            }
        } else {
            player.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_GLOBAL_INCORRECT_ARGS)));
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        if (invocation.arguments().length == 1){
            String server = invocation.arguments()[0];
            List<String> suggestions = new ArrayList<>();
            for(RegisteredServer s : ProxyCore.getInstance().getAllServers()){
                if (s.getServerInfo().getName().toLowerCase().startsWith(server.toLowerCase())){
                    suggestions.add(s.getServerInfo().getName());
                }
            }
            return suggestions;
        } else if (invocation.arguments().length == 0){
            List<String> options = ProxyCore.getInstance().getAllServers().stream().map(server -> server.getServerInfo().getName()).collect(Collectors.toList());
            return options;
        }
        return List.of();
    }
}
