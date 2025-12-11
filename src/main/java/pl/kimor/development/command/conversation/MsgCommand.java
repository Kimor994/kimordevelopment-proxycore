package pl.kimor.development.command.conversation;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import pl.kimor.development.config.Config;
import pl.kimor.development.config.ConfigKey;
import pl.kimor.development.ProxyCore;
import pl.kimor.development.config.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MsgCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        List<String> arguments = Arrays.asList(invocation.arguments());
        Player author = (Player)invocation.source();
        Player recipient = null;
        for (Player players : ProxyCore.getInstance().getAllPlayers()) {
            String name = players.getUsername();
            if (name.toLowerCase().startsWith(((String)arguments.get(0)).toLowerCase()) || name
                    .toUpperCase().startsWith(((String)arguments.get(0)).toUpperCase())) {
                recipient = players;
                break;
            }
        }
        List<String> messages = new ArrayList<>();
        for (int i = 1; i < invocation.arguments().length; i++)
            messages.add(invocation.arguments()[i]);
        if (!messages.isEmpty()) {
            if (recipient == author) {
                author.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_CANT_SEND_TO_SELF)));
                return;
            }
            if (recipient == null) {
                author.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_PLAYER_IS_OFFLINE)));
                return;
            }
            try {
                if (IgnoreList.ignores(recipient, author)) {
                    author.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_IGNORED)));
                    return;
                } else if (IgnoreList.ignores(author,recipient)){
                    author.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_CANT_MESSAGE_BLOCKED_PLAYER)));
                    return;
                }
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (!recipient.isActive()) {
                author.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_RECIPIENT_OFFLINE).replaceAll("%recipient%",recipient.getUsername())));
            } else {
                if (MessagingSystem.getConversations().containsKey(author)) {
                    MessagingSystem.getConversations().remove(author);
                    MessagingSystem.getConversations().put(author, recipient);
                } else {
                    MessagingSystem.getConversations().put(author, recipient);
                }
                author.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_SENT_TO_SOMEONE)
                        .replaceAll("%recepient%", recipient.getUsername())
                        .replaceAll("%sender%",author.getUsername())
                        .replaceAll("%message%", MessagingSystem.createMessageFromList(messages))
                ));
                recipient.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_RECEIVED_FROM_SOMEONE)
                        .replaceAll("%recepient%", recipient.getUsername())
                        .replaceAll("%sender%",author.getUsername())
                        .replaceAll("%message%", MessagingSystem.createMessageFromList(messages))
                ));
                if (MessagingSystem.getConversations().containsKey(recipient)) {
                    MessagingSystem.getConversations().remove(recipient);
                    MessagingSystem.getConversations().put(recipient, author);
                } else {
                    MessagingSystem.getConversations().put(recipient, author);
                }
            }
        } else {
            author.sendMessage(Utils.coloredText(Config.getString(ConfigKey.CFG_MSG_NO_MESSAGE)));
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)){
            return List.of();
        }
        if (invocation.arguments().length == 0){
            return ProxyCore.getInstance().getAllPlayers().stream().map(Player::getUsername).collect(Collectors.toList());
        } else if (invocation.arguments().length == 2){
            return List.of();
        }
        String search = invocation.arguments()[0];
        return ProxyCore.getInstance()
                .getAllPlayers()
                .stream()
                .filter
                (p -> p.getUsername().toLowerCase().startsWith(search.toLowerCase()))
                .collect(Collectors.toList())
                .stream()
                .map(Player::getUsername)
                .collect(Collectors.toList());
    }
}
