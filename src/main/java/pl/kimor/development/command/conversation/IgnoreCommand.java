package pl.kimor.development.command.conversation;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import pl.kimor.development.Config;
import pl.kimor.development.ConfigKey;
import pl.kimor.development.ProxyCore;
import pl.kimor.development.Utils;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class IgnoreCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        Player player = (Player)invocation.source();
        Optional<Player> optionalTarget = ProxyCore.getInstance().getPlayer(invocation.arguments()[0]);
        if (optionalTarget.isEmpty()){
            player.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_PLAYER_TO_IGNORE_DOESNT_EXIST)));
            return;
        }
        Player targetPlayer = optionalTarget.get();
        if (!targetPlayer.isActive()) {
            player.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_PLAYER_IS_OFFLINE)));
            return;
        }
        if (player == targetPlayer) {
            player.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_CANT_IGNORE_SELF)));
            return;
        }
        try {
            if (IgnoreList.ignores(player, targetPlayer)) {
                player.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_ALREADY_IGNORED)));
            } else {
                player.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_IGNORED).replaceAll("%player%",targetPlayer.getUsername())));
                IgnoreList.makePlayerIgnoreOther(player, targetPlayer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)){
            return List.of();
        }
        List<String> online = ProxyCore.getInstance().getAllPlayers().stream().map(Player::getUsername).collect(Collectors.toList());
        try {
            online.remove(IgnoreList.getIgnoredPlayers(player));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (invocation.arguments().length == 0){
            return online;
        } else if (invocation.arguments().length == 1){
            String search = invocation.arguments()[0];
            return online.stream().filter(f -> f.toLowerCase().startsWith(search.toLowerCase())).collect(Collectors.toList());
        }
        return List.of();
    }
}
