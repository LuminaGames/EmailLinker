package me.comphack.emaillinker;

import me.comphack.emaillinker.commands.EmailCommand;
import me.comphack.emaillinker.database.Database;
import me.comphack.emaillinker.utils.UserCache;
import me.comphack.emaillinker.utils.Utils;
import org.bukkit.plugin.java.JavaPlugin;

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
        
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerCommands() {
        getCommand("email").setExecutor(new EmailCommand());
    }

    public void registerEvents() {

    }

}
