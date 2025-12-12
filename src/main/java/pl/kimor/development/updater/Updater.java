package pl.kimor.development.updater;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.velocitypowered.api.plugin.Plugin;
import pl.kimor.development.ProxyCore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class Updater {

    private final URL updateCheckUrl = new URL("https://api.github.com/repos/Kimor994/kimordevelopment-proxycore/releases/latest");

    public Updater() throws MalformedURLException {
    }
    private String latestVersion = null;
    public String getLatestVersion(){
        return latestVersion;
    }
    public boolean updateAvailable() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) updateCheckUrl.openConnection();
        connection.setRequestProperty("User-Agent","kimordevelopment-ProxyCore/Updater");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder json = new StringBuilder();
        String ln;
        while ((ln=reader.readLine())!=null){
            json.append(ln);
        }
        String jsonInput = json.toString();
        JsonElement root = JsonParser.parseString(jsonInput);
        JsonObject rootObject = root.getAsJsonObject();
        String tagName = rootObject.get("tag_name").getAsString();
        connection.disconnect();
        String[] latestVersion = tagName.split("\\.");
        String[] currentVersion = ProxyCore.class.getAnnotation(Plugin.class).version().split("\\.");
        for(int i=0; i<3;){
            if(Integer.parseInt(latestVersion[i])>Integer.parseInt(currentVersion[i])){
                this.latestVersion=tagName;
                return true;
            }
            i++;
        }
        return false;
    }
    public void downloadUpdate() throws IOException, URISyntaxException {
        HttpURLConnection connection = (HttpURLConnection) updateCheckUrl.openConnection();
        connection.setRequestProperty("User-Agent","kimordevelopment-ProxyCore/Updater");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder json = new StringBuilder();
        String ln;
        while ((ln=reader.readLine())!=null){
            json.append(ln);
        }
        String jsonInput = json.toString();
        JsonElement root = JsonParser.parseString(jsonInput);
        JsonObject rootObject = root.getAsJsonObject();
        List<JsonElement> assetsList = rootObject.getAsJsonArray("assets").asList();
        JsonObject assetsObject = assetsList.get(0).getAsJsonObject();
        connection.disconnect();

        String latestRelease = assetsObject.get("browser_download_url").getAsString();
        HttpURLConnection downloader =  (HttpURLConnection) new URL(latestRelease).openConnection();
        Files.copy(downloader.getInputStream(), Path.of(ProxyCore.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()), StandardCopyOption.REPLACE_EXISTING);
        downloader.disconnect();
    }

}
