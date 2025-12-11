package pl.kimor.development.command.admin;

import com.velocitypowered.api.command.SimpleCommand;
import org.spongepowered.configurate.ConfigurateException;
import pl.kimor.development.Config;
import pl.kimor.development.Utils;

public class ReloadCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        if (Utils.permitted(invocation.source(),"kimordevelopment.proxy.reload")){
            invocation.source().sendMessage(Utils.coloredText("<yellow>Reloading the configuration..."));
            try {
                Config.load();
            } catch (ConfigurateException e) {
                invocation.source().sendMessage(Utils.coloredText("<red>There was an error while reloading the configuration! Check the console."));
                throw new RuntimeException(e);
            }
            invocation.source().sendMessage(Utils.coloredText("<green>Configuration reloaded successfully!"));
        }
    }
}
