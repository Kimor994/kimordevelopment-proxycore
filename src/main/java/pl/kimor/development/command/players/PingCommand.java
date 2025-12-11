package pl.kimor.development.command.players;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import pl.kimor.development.Config;
import pl.kimor.development.ConfigKey;
import pl.kimor.development.Utils;

public class PingCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        if (!(source instanceof Player)){
            source.sendMessage(Utils.coloredText("<red>Komenda jest wykonywalna tylko przez graczy."));
            return;
        }
        Player player = (Player) source;
        int ping = (int)player.getPing();
        player.sendMessage(buildComponent(ping));
    }

    public Component buildComponent(int ping){

        String comment;
        String color;
        String base = Config.getString(ConfigKey.CFG_PING_BASE_MESSAGE);

        if (ping<=65){
            comment=Config.getString(ConfigKey.CFG_PING_BANDWITH_GOOD_MESSAGE);
            color=Config.getString(ConfigKey.CFG_PING_BANDWITH_GOOD_COLOR);
        } else if (ping<=150){
            comment=Config.getString(ConfigKey.CFG_PING_BANDWITH_MEDIUM_MESSAGE);
            color=Config.getString(ConfigKey.CFG_PING_BANDWITH_MEDIUM_COLOR);
        } else if (ping<=300){
            comment=Config.getString(ConfigKey.CFG_PING_BANDWITH_BAD_MESSAGE);
            color=Config.getString(ConfigKey.CFG_PING_BANDWITH_BAD_COLOR);
        } else{
            comment=Config.getString(ConfigKey.CFG_PING_BANDWITH_SHITTY_MESSAGE);
            color=Config.getString(ConfigKey.CFG_PING_BANDWITH_SHITTY_COLOR);
        }
        return Utils.coloredText(base
                .replaceAll("%color%",color)
                .replaceAll("%comment%",comment)
                .replaceAll("%ping%",String.valueOf(ping)
                )
        );
    }
}
