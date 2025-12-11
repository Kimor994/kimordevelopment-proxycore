package pl.kimor.development.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.util.HashMap;
import java.util.List;

public class Config {
    private static CommentedConfigurationNode nx;
    private static YamlConfigurationLoader ldr;
    public static void initialise(YamlConfigurationLoader loader) throws ConfigurateException {
        ldr = loader ;
        load();
    }
    private static HashMap<ConfigKey, Object> keys = new HashMap<ConfigKey, Object>();
    public static void load() throws ConfigurateException {
        nx = ldr.load();
        keys.clear();
        keys.put(ConfigKey.AUTO_UPDATE, nx.node("updater", "auto-update").raw());
        keys.put(ConfigKey.MODULES_ENABLED, nx.node("enabled-modules").raw());
        keys.put(ConfigKey.CFG_GLOBAL_INCORRECT_ARGS, nx.node("messages", "global", "incorrect-arguments").raw());
        keys.put(ConfigKey.CFG_GLOBAL_NO_PERMS, nx.node("messages", "global", "no-permissions").raw());
        keys.put(ConfigKey.CFG_GLOBAL_BAD_SERVER, nx.node("messages", "global", "incorrect-server").raw());
        keys.put(ConfigKey.CFG_GLOBAL_PLAYER_NOT_FOUND, nx.node("messages", "global", "player-not-found").raw());
        keys.put(ConfigKey.CFG_GLOBAL_EXECUTABLE_BY_PL, nx.node("messages", "global", "executable-by-player").raw());
        keys.put(ConfigKey.CFG_ALERTS_SCREEN_ENABLE, nx.node("messages", "alerts", "screen", "enable").raw());
        keys.put(ConfigKey.CFG_ALERTS_SCREEN_TITLE, nx.node("messages", "alerts", "screen", "title").raw());
        keys.put(ConfigKey.CFG_ALERTS_SCREEN_SUBTITLE, nx.node("messages", "alerts", "screen", "subtitle").raw());
        keys.put(ConfigKey.CFG_ALERTS_CHAT_ENABLE, nx.node("messages", "alerts", "chat", "enable").raw());
        keys.put(ConfigKey.CFG_ALERTS_CHAT_LINES, nx.node("messages", "alerts", "chat", "lines").raw());
        keys.put(ConfigKey.CFG_FIND_LINES, nx.node("messages", "find", "lines").raw());
        keys.put(ConfigKey.CFG_PLAYERS_SERVER_INVALID, nx.node("messages", "players", "server-data-invalid").raw());
        keys.put(ConfigKey.CFG_PLAYERS_MESSAGE_HEADER, nx.node("messages", "players", "lines", "message-header").raw());
        keys.put(ConfigKey.CFG_PLAYERS_SERVER_SUBLIST_HEADER, nx.node("messages", "players", "lines", "server-sublist-header").raw());
        keys.put(ConfigKey.CFG_PLAYERS_SERVER_SUBLIST_ELEMENT, nx.node("messages", "players", "lines", "server-sublist-element").raw());
        keys.put(ConfigKey.CFG_SEND_NO_TARGET, nx.node("messages", "send", "no-target-server").raw());
        keys.put(ConfigKey.CFG_SEND_NO_SOURCE, nx.node("messages", "send", "no-source-server").raw());
        keys.put(ConfigKey.CFG_SEND_SUCCESS, nx.node("messages", "send", "players-sent").raw());
        keys.put(ConfigKey.CFG_MSG_PLAYER_TO_IGNORE_DOESNT_EXIST, nx.node("messages", "messaging", "player-to-ignore-doesnt-exist").raw());
        keys.put(ConfigKey.CFG_MSG_PLAYER_IS_OFFLINE, nx.node("messages", "messaging", "player-is-offline").raw());
        keys.put(ConfigKey.CFG_MSG_CANT_IGNORE_SELF, nx.node("messages", "messaging", "cant-ignore-self").raw());
        keys.put(ConfigKey.CFG_MSG_ALREADY_IGNORED, nx.node("messages", "messaging", "already-ignored").raw());
        keys.put(ConfigKey.CFG_MSG_NOW_IGNORING, nx.node("messages", "messaging", "now-ignoring").raw());
        keys.put(ConfigKey.CFG_MSG_CANT_SEND_TO_SELF, nx.node("messages", "messaging", "cant-send-to-self").raw());
        keys.put(ConfigKey.CFG_MSG_IGNORED, nx.node("messages", "messaging", "ignored").raw());
        keys.put(ConfigKey.CFG_MSG_SENT_TO_SOMEONE, nx.node("messages", "messaging", "sent-to-someone").raw());
        keys.put(ConfigKey.CFG_MSG_RECEIVED_FROM_SOMEONE, nx.node("messages", "messaging", "received-from-someone").raw());
        keys.put(ConfigKey.CFG_MSG_NO_MESSAGE, nx.node("messages", "messaging", "no-message").raw());
        keys.put(ConfigKey.CFG_MSG_NO_PLAYER_TO_REPLY, nx.node("messages", "messaging", "no-player-to-reply").raw());
        keys.put(ConfigKey.CFG_MSG_CANT_MESSAGE_BLOCKED_PLAYER, nx.node("messages", "messaging", "cant-message-blocked-player").raw());
        keys.put(ConfigKey.CFG_MSG_RECIPIENT_OFFLINE, nx.node("messages", "messaging", "recipient-offline").raw());
        keys.put(ConfigKey.CFG_MSG_NOT_IGNORING, nx.node("messages", "messaging", "not-ignoring").raw());
        keys.put(ConfigKey.CFG_MSG_UNIGNORED, nx.node("messages", "messaging", "unignored").raw());
        keys.put(ConfigKey.CFG_PING_BASE_MESSAGE, nx.node("messages", "ping", "message").raw());
        keys.put(ConfigKey.CFG_PING_BANDWITH_GOOD_MESSAGE, nx.node("messages", "ping", "components", "bandwith-good", "message").raw());
        keys.put(ConfigKey.CFG_PING_BANDWITH_GOOD_COLOR, nx.node("messages", "ping", "components", "bandwith-good", "color").raw());
        keys.put(ConfigKey.CFG_PING_BANDWITH_MEDIUM_MESSAGE, nx.node("messages", "ping", "components", "bandiwth-medium", "message").raw());
        keys.put(ConfigKey.CFG_PING_BANDWITH_MEDIUM_COLOR, nx.node("messages", "ping", "components", "bandiwth-medium", "color").raw());
        keys.put(ConfigKey.CFG_PING_BANDWITH_BAD_MESSAGE, nx.node("messages", "ping", "components", "bandwith-bad", "message").raw());
        keys.put(ConfigKey.CFG_PING_BANDWITH_BAD_COLOR, nx.node("messages", "ping", "components", "bandwith-bad", "color").raw());
        keys.put(ConfigKey.CFG_PING_BANDWITH_SHITTY_MESSAGE, nx.node("messages", "ping", "components", "bandwidth-shitty", "message").raw());
        keys.put(ConfigKey.CFG_PING_BANDWITH_SHITTY_COLOR, nx.node("messages", "ping", "components", "bandwidth-shitty", "color").raw());
        keys.put(ConfigKey.CFG_STAFFCHAT_MSG_SYMBOL, nx.node("messages", "staff-chat", "message-symbol").raw());
        keys.put(ConfigKey.CFG_STAFFCHAT_MSG_FORMAT, nx.node("messages", "staff-chat", "message-format").raw());

    }
    public static boolean getBoolean(ConfigKey key){
        return Boolean.parseBoolean(keys.get(key).toString());
    }
    public static Object getObject(ConfigKey key){
        return keys.get(key);
    }
    public static String getString(ConfigKey key){
        return keys.get(key).toString();
    }
    public static int getInt(ConfigKey key){
        return Integer.parseInt(keys.get(key).toString());
    }
    public static List<Object> getList(ConfigKey key){
        return (List<Object>)keys.get(key);
    }

}
