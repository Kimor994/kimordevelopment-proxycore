package pl.kimor.development.updater;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Updater {

    private final URL updateCheckUrl = new URL("https://api.github.com/repos/Kimor994/kimordevelopment-proxycore/releases/latest");

    public Updater() throws MalformedURLException {
    }

}
