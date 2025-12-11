package pl.kimor.development.command.conversation;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import pl.kimor.development.Config;
import pl.kimor.development.ConfigKey;
import pl.kimor.development.Utils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ReplyCommand implements SimpleCommand {


    @Override
    public void execute(Invocation invocation) {

        List<String> arguments = Arrays.asList(invocation.arguments());
        Player author = (Player) invocation.source();
        if (!MessagingSystem.getConversations().containsKey(author)) {
            author.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_NO_PLAYER_TO_REPLY)));
            return;
        }
        Player to = (Player)MessagingSystem.getConversations().get(author);
        List<String> words = arguments;
        if (to == null) {
            author.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_RECIPIENT_OFFLINE)));
            return;
        }
        try {
            Player p2 = (Player)MessagingSystem.getConversations().get(author);
            if (IgnoreList.ignores(p2, author)) {
                author.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_IGNORED)));
                return;
            }
            if (IgnoreList.ignores(author, p2)) {
                author.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_CANT_MESSAGE_BLOCKED_PLAYER).replaceAll("%blocked%",p2.getUsername())));
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (words.size() < 1) {
            author.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_NO_MESSAGE)));
            return;
        }
        if (!to.isActive()) {
            author.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_RECIPIENT_OFFLINE)));
        } else {
            author.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_SENT_TO_SOMEONE)
                    .replaceAll("%recepient%", to.getUsername())
                    .replaceAll("%sender%",author.getUsername())
                    .replaceAll("%message%", MessagingSystem.createMessageFromList(arguments))
            ));
            to.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_RECEIVED_FROM_SOMEONE)
                    .replaceAll("%recepient%", to.getUsername())
                    .replaceAll("%sender%",author.getUsername())
                    .replaceAll("%message%", MessagingSystem.createMessageFromList(arguments))
            ));
            if (MessagingSystem.getConversations().containsKey(to)) {
                MessagingSystem.getConversations().remove(to);
                MessagingSystem.getConversations().put(to, author);
            } else {
                MessagingSystem.getConversations().put(to, author);
            }
        }
    }
}
