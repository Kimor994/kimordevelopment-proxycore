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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SendCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        CommandSource source = invocation.source();
        if (!Utils.permitted(invocation.source(),"kimordevelopment.proxy.send")){
            return;
        }
        if (args.length!=2){
            source.sendMessage(Utils.coloredText(""));
            return;
        }
        String arg1=args[0];
        String arg2=args[1];
        List<String> allServers = ProxyCore.getInstance().getAllServers().stream().map(server -> server.getServerInfo().getName()).toList();
        List<String> allPlayers = ProxyCore.getInstance().getAllPlayers().stream().map(Player::getUsername).toList();
        if (Arrays.asList("all","*","proxy").contains(arg1.toLowerCase())){
            Optional<RegisteredServer> server = ProxyCore.getInstance().getServer(arg2);
            if (server.isPresent()){
                ProxyCore.getInstance().getAllPlayers().forEach(player -> {
                    player.createConnectionRequest(server.get()).connect();
                });
            } else {
                source.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_GLOBAL_BAD_SERVER)));
            }
        } else if (allServers.contains(arg1)){
            Optional<RegisteredServer> optionalSrc = ProxyCore.getInstance().getServer(arg1);
            Optional<RegisteredServer> optionalTarget = ProxyCore.getInstance().getServer(arg2);
            if (optionalSrc.isEmpty()){
                source.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_SEND_NO_SOURCE)));
                return;
            }
            if (optionalTarget.isEmpty()){
                source.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_SEND_NO_TARGET)));
                return;
            }
            optionalSrc.get().getPlayersConnected().forEach(player -> {
                player.createConnectionRequest(optionalTarget.get()).connect();
            });
            source.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_SEND_SUCCESS)
                    .replaceAll("%source%",optionalSrc.get().getServerInfo().getName())
                    .replaceAll("%target%",optionalTarget.get().getServerInfo().getName())
            ));
        } else {
            if (!allPlayers.contains(arg1)){
                source.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_GLOBAL_PLAYER_NOT_FOUND)));
                return;
            }
            Player player = ProxyCore.getInstance().getPlayer(arg1).get();
            Optional<RegisteredServer> optionalTarget = ProxyCore.getInstance().getServer(arg2);
            if (optionalTarget.isEmpty()){
                source.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_SEND_NO_TARGET)));
                return;
            }
            player.createConnectionRequest(optionalTarget.get()).connect();
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        List<String> suggestions = new ArrayList<>();
        for(Player player : ProxyCore.getInstance().getAllPlayers()){
            suggestions.add(player.getUsername());
        }
        suggestions.add("all");
        suggestions.add("*");
        suggestions.add("proxy");
        for(RegisteredServer server : ProxyCore.getInstance().getAllServers()){
            suggestions.add(server.getServerInfo().getName());
        }
        String arg = switch (args.length) {
            case 1 -> args[0];
            case 2 -> args[1];
            default -> "";
        };
        return suggestions.stream().filter(suggestion -> suggestion.toLowerCase().startsWith(arg)).collect(Collectors.toList());
    }
}
