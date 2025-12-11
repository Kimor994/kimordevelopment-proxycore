package pl.kimor.development.command.conversation;

import com.velocitypowered.api.proxy.Player;

import java.util.HashMap;
import java.util.List;

public class MessagingSystem {
    private static HashMap<Player, Player> conversations = new HashMap<>();

    public static HashMap<Player, Player> getConversations() {
        return conversations;
    }

    public static String createMessageFromList(List<String> words) {
        String message = "";
        for (String s : words)
            message = message + " " + s;
        return message;
    }

    public MessagingSystem() {
        conversations = new HashMap<>();
    }
}
