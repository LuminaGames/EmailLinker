package me.comphack.emaillinker;

import me.comphack.emaillinker.commands.CommandManager;
import me.comphack.emaillinker.database.Database;
import me.comphack.emaillinker.utils.Metrics;
import me.comphack.emaillinker.utils.UserCache;
import me.comphack.emaillinker.utils.Utils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Emaillinker extends JavaPlugin {

    private Utils utils;
    private Database database;

    @Override
    public void onEnable() {
        utils = new Utils();
        getConfig().options().copyDefaults(true);
        saveConfig();
        utils.sendPluginLog("info", "Loaded Configuration Files");
        registerCommands();
        registerEvents();
        utils.sendPluginLog("info", "Loaded Events & Commands");
        new UserCache(this, "cache.yml");
        database = new Database();
        database.setupJDBC();
        database.PluginDatabase();
        utils.sendPluginLog("info", "Loaded Database");
        int pluginId = 16771;
        if(getConfig().getBoolean("enable-bstats")) {
            Metrics metrics = new Metrics(this, pluginId);
            utils.sendPluginLog("info", "Loaded bStats");
        }

        startupMessage();
    }


    @Override
    public void onDisable() {
        File cacheFile = new File(getDataFolder() + "/cache.yml");
        if(cacheFile.exists()) {
            cacheFile.delete();
            utils.sendPluginLog("info", "Cleared the cache files. Players with pending verification should use the email command again.");
        }


    }

    public void registerCommands() {
        getCommand("email").setExecutor(new CommandManager());
    }

    public void startupMessage() {
        getLogger().info("░█▀▀░█▄█░█▀█░▀█▀░█░░░░░█░░░▀█▀░█▀█░█░█░█▀▀░█▀▄");
        getLogger().info("░█▀▀░█░█░█▀█░░█░░█░░░░░█░░░░█░░█░█░█▀▄░█▀▀░█▀▄");
        getLogger().info("░▀▀▀░▀░▀░▀░▀░▀▀▀░▀▀▀░░░▀▀▀░▀▀▀░▀░▀░▀░▀░▀▀▀░▀░▀");

    }

    public void registerEvents() {
    }

}
