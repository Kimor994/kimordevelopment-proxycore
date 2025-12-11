package pl.kimor.development.command.admin;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import pl.kimor.development.config.Config;
import pl.kimor.development.config.ConfigKey;
import pl.kimor.development.ProxyCore;
import pl.kimor.development.config.Utils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FindCommand implements SimpleCommand {


    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        if (!Utils.permitted(sender,"kimordevelopment.proxy.find")){
            return;
        }
        if (invocation.arguments().length != 1){
            invocation.source().sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_GLOBAL_INCORRECT_ARGS)));
            return;
        }
        String name = invocation.arguments()[0];
        Optional<Player> optionalPlayer = ProxyCore.getInstance().getPlayer(name);
        if (optionalPlayer.isEmpty()){
            invocation.source().sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_GLOBAL_PLAYER_NOT_FOUND)));
            return;
        }
        Player player = optionalPlayer.get();
        String locale = player.getEffectiveLocale().getLanguage();
        String country = player.getEffectiveLocale().getCountry();
        String server=Config.getString(ConfigKey.CFG_PLAYERS_SERVER_INVALID);
        if (player.getCurrentServer().isPresent()){
            server=player.getCurrentServer().get().getServerInfo().getName();
        }
        for(Object o : Config.getList(ConfigKey.CFG_FIND_LINES)){
            String ln = o.toString();
            ln=ln
                    .replaceAll("%player%", player.getUsername())
                    .replaceAll("%uuid%",player.getUniqueId().toString())
                    .replaceAll("%ip%",player.getRemoteAddress().getAddress().getHostAddress())
                    .replaceAll("%server%",server)
                    .replaceAll("%language_code%",locale)
                    .replaceAll("%language_country%",country)
                    .replaceAll("%version_name%",player.getProtocolVersion().getVersionIntroducedIn())
                    .replaceAll("%version_protocol%",String.valueOf(player.getProtocolVersion().getProtocol()))
                    .replaceAll("%client%",String.valueOf(player.getClientBrand()));
            sender.sendMessage(Utils.coloredText(ln));
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length == 0){
            return ProxyCore.getInstance().getAllPlayers().stream().map(player -> player.getUsername()).collect(Collectors.toList());
        } else if (args.length == 1){
            return ProxyCore.getInstance().getAllPlayers()
                    .stream().map(player -> player.getUsername()).collect(Collectors.toList())
                    .stream().filter(username -> username.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }
        return List.of();
    }
}
