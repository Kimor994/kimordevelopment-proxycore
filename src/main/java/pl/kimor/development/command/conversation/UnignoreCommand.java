package pl.kimor.development.command.conversation;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import pl.kimor.development.Config;
import pl.kimor.development.ConfigKey;
import pl.kimor.development.ProxyCore;
import pl.kimor.development.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UnignoreCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        Player player = (Player) invocation.source();
        Optional<Player> targetPlayer = ProxyCore.getInstance().getPlayer(invocation.arguments()[0]);
        if (targetPlayer.isEmpty()){
            player.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_NOT_IGNORING)));
            return;
        } else {

            Player target = targetPlayer.get();
            if (!target.isActive()) {
                player.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_PLAYER_IS_OFFLINE)));
                return;
            }
            try {
                if (IgnoreList.ignores(player, target)) {
                    player.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_UNIGNORED).replaceAll("%target%",target.getUsername())));
                    IgnoreList.makePlayerUnignoreOther(player, target);
                } else {
                    player.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_NOT_IGNORING)));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        Player player = (Player)invocation.source();
        List<String> suggestions = new ArrayList<>();
        if (invocation.arguments().length == 0){
            try {
                return IgnoreList.getIgnoredPlayers(player);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        String search = invocation.arguments()[0];
        try {
            for (String s : IgnoreList.getIgnoredPlayers(player)) {
                if (s.toLowerCase().startsWith(search.toLowerCase()) || s.toUpperCase().startsWith(search.toUpperCase()) || s.startsWith(search))
                    suggestions.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suggestions;
    }
}
