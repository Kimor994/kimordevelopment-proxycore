package pl.kimor.development.command.admin;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import pl.kimor.development.Config;
import pl.kimor.development.ConfigKey;
import pl.kimor.development.ProxyCore;
import pl.kimor.development.Utils;

import java.util.Collection;

public class PlayersCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        if (!Utils.permitted(invocation.source(),"kimordevelopment.proxy.players")){
            return;
        }
        CommandSource source = invocation.source();
        int proxyPlayers = ProxyCore.getInstance().getPlayerCount();
        source.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_PLAYERS_MESSAGE_HEADER).replaceAll("%all%",String.valueOf(proxyPlayers))));
        for(RegisteredServer server : ProxyCore.getInstance().getAllServers()){
            Collection<Player> players = server.getPlayersConnected();
            String name = server.getServerInfo().getName();
            int online = players.size();
            source.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_PLAYERS_SERVER_SUBLIST_HEADER)
                    .replaceAll("%subserver_name%",name)
                    .replaceAll("%subserver_count%",String.valueOf(online))
            ));
            players.forEach(player -> {
                source.sendMessage(Utils.coloredText(
                        Config.getString(ConfigKey.CFG_PLAYERS_SERVER_SUBLIST_ELEMENT)
                        .replaceAll("%subserver_player_name%",player.getUsername())
                        .replaceAll("%subserver_player_client%",player.getClientBrand() == null ? "???" : player.getClientBrand())
                ));
            });
        }
    }
}
