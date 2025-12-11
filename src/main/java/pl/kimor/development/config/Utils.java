package pl.kimor.development.config;

import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static boolean permitted(CommandSource src, String perm){
        boolean p = src.hasPermission(perm);
        if(!p){
            src.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_GLOBAL_NO_PERMS)));
        }
        return p;
    }
    public static Component coloredText(String text){
        return MiniMessage.miniMessage().deserialize(text);
    }
    public static List<Component> coloredList(List<String> list){
        return list.stream().map(Utils::coloredText).collect(Collectors.toList());
    }
    public static Component join(Component comp1, Component comp2, String delimiter){
        String s1 = MiniMessage.miniMessage().serialize(comp1);
        String s2 = MiniMessage.miniMessage().serialize(comp2);
        return MiniMessage.miniMessage().deserialize(s1+delimiter+s2);
    }
}
