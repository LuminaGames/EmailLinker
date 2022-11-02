package me.comphack.emaillinker.database;

import me.comphack.emaillinker.utils.Utils;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;

public class Database {
    static Connection connection;
    private Utils utils = new Utils();
    private String Url;
    private String host, port, database, username, password;


    public void setupJDBC() {
        database = utils.getPlugin().getConfig().getString("database.database");
        username = utils.getPlugin().getConfig().getString("database.username");
        password = utils.getPlugin().getConfig().getString("database.password");
        host = utils.getPlugin().getConfig().getString("database.host");
        port = utils.getPlugin().getConfig().getString("database.port");
        if (utils.getPlugin().getConfig().getBoolean("database.enabled")) {
            Url = "jdbc:mysql://" + host + ":" + port + "/" + database;
            Bukkit.getLogger().info("Using Storage Method [MySQL]");
        } else {
            Bukkit.getLogger().info("Using Storage Method [SQLite]");
            File database = new File(Bukkit.getServer().getPluginManager().getPlugin("EmailLinker").getDataFolder(), "database.db");
            if (!database.exists()) {
                try {
                    database.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            Url = "jdbc:sqlite:" + database;
        }
        try {
            connection = DriverManager.getConnection(Url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    public void PluginDatabase() {
        String SQL = "CREATE TABLE IF NOT EXISTS " +
                "emails ( " +
                "username VARCHAR(20), " +
                "uuid VARCHAR(36) ," +
                "email_address VARCHAR(255));";
        try {
            connection.createStatement().execute(SQL);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasLinkedEmail(UUID player)  {
        try {
            String sql = "SELECT * FROM emails WHERE uuid=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, player.toString());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void setEmail(String username, UUID uuid, String EmailAddr) {
        try {
            String sql = "INSERT INTO emails (username, uuid, email_address)" +
                    " VALUES (?, ? ,?);";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, uuid.toString());
            ps.setString(3, EmailAddr);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnectEmail(UUID player) {
        try {
            String sql = "DELETE FROM emails WHERE uuid=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, player.toString());
            ps.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkEmail(UUID player, String email) {
        try{
            String sql = "SELECT * FROM emails where uuid=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, player.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.getString("email_address").equalsIgnoreCase(email)) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
