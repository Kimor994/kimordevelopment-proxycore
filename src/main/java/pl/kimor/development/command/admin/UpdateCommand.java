package pl.kimor.development.command.admin;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import pl.kimor.development.config.Utils;
import pl.kimor.development.updater.Updater;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class UpdateCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        if (!Utils.permitted(invocation.source(),"kimordevelopment.proxy.update")){
            return;
        }
        CommandSource src = invocation.source();
        src.sendMessage(Utils.coloredText("Downloading update..."));
        try {
            Updater updater = new Updater();
            updater.downloadUpdate();
            src.sendMessage(Utils.coloredText("Update have been installed! Restart the proxy server to see changes."));
        } catch (IOException | URISyntaxException e) {
            src.sendMessage(Utils.coloredText("There was an error while downloading latest update."));
            e.printStackTrace();
        }
    }
}
