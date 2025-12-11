package pl.kimor.development.command.conversation;

import com.velocitypowered.api.proxy.Player;
import pl.kimor.development.ProxyCore;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IgnoreList {

    private static File getIgnoreListFile(){
        return new File(ProxyCore.getDataDir().toFile(),"ignorelist.db");
    }

    private static Connection connection;
    public static Connection getConnection() {
        return connection;
    }

    private static void updateData(String sql) throws SQLException {
        Statement s = connection.createStatement();
        s.executeUpdate(sql);
        s.close();
    }
    private static ResultSet searchForData(String sql) throws SQLException {
        Statement s = connection.createStatement();
        return s.executeQuery(sql);
    }

    public static void initialise() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class.forName("org.sqlite.JDBC").newInstance();
        ProxyCore.getDataDir().toFile().mkdir();
        connection= DriverManager.getConnection("jdbc:sqlite:"+getIgnoreListFile().getAbsolutePath());
        ResultSet res = searchForData("SELECT name FROM sqlite_master WHERE type = 'table' AND name LIKE 'ignorelist'");
        if (res.next()){
            return;
        }
        updateData("CREATE TABLE ignorelist(who varchar(16) NOT NULL, ignored varchar(16) NOT NULL)");
    }

    public static List<String> getIgnoredPlayers(Player player) throws SQLException {
        ResultSet res = searchForData("SELECT ignored FROM ignorelist WHERE who = '"+player.getUsername()+"'");
        List<String> players =  new ArrayList<>();
        while(res.next()){
            players.add(res.getString("ignored"));
        }
        res.close();
        return players;
    }
    public static boolean makePlayerIgnoreOther(Player who, Player ignored) throws SQLException {
        if (!ignores(who,ignored)){
            updateData("INSERT INTO ignorelist(who,ignored) VALUES ('"+who.getUsername()+"','"+ignored.getUsername()+"')");
            return true;
        }
        return false;
    }
    public static boolean ignores(Player who, Player target) throws SQLException {
        return getIgnoredPlayers(who).contains(target.getUsername());
    }
    public static boolean makePlayerUnignoreOther(Player who, Player target) throws SQLException {
        if (ignores(who,target)){
            updateData("DELETE FROM ignorelist WHERE who='"+who.getUsername()+"' AND ignored='"+target.getUsername()+"'");
            return true;
        }
        return false;
    }

    public static void shutdown() throws SQLException {
        if (!connection.isClosed()){
            connection.close();
        }
    }
}