package pl.kimor.development.system;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import pl.kimor.development.Config;
import pl.kimor.development.ConfigKey;
import pl.kimor.development.ProxyCore;
import pl.kimor.development.Utils;

public class StaffChatListener {
    @Subscribe(priority = 10)
    public void onChat(PlayerChatEvent event){
        Player player = event.getPlayer();
        if (!player.hasPermission("kimordevelopment.proxy.staffchat")){
            return;
        }
        if (!event.getMessage().startsWith(Config.getString(ConfigKey.CFG_STAFFCHAT_MSG_SYMBOL))){
            return;
        }
        String finalMsg = event.getMessage().replaceFirst(Config.getString(ConfigKey.CFG_STAFFCHAT_MSG_SYMBOL),"");
        event.setResult(PlayerChatEvent.ChatResult.denied());
        ProxyCore.getInstance().getAllPlayers().forEach(p-> {
            if (!p.hasPermission("kimordevelopment.proxy.staffchat")){
                return;
            }
            p.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_STAFFCHAT_MSG_FORMAT)
                    .replaceAll("%message%",finalMsg)
                    .replaceAll("%admin%",player.getUsername())
            ));
        });
    }
}
