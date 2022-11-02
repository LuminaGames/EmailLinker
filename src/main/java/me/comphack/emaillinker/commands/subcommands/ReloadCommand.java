package me.comphack.emaillinker.commands.subcommands;

import me.comphack.emaillinker.Emaillinker;
import me.comphack.emaillinker.commands.SubCommand;
import me.comphack.emaillinker.utils.HashingUtils;
import me.comphack.emaillinker.utils.UserCache;
import me.comphack.emaillinker.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class ReloadCommand extends SubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads the plugin configuration";
    }

    @Override
    public String getSyntax() {
        return "/email reload";
    }

    @Override
    public void perform(Player player, String[] args) throws SQLException {
        if (player.hasPermission("emaillinker.command.reload") || player.hasPermission("emaillinker.command.*")) {
            Utils utils = new Utils();
            UserCache cache = new UserCache(JavaPlugin.getPlugin(Emaillinker.class), "cache.yml");
            cache.reloadCache();
            player.sendMessage(utils.color("&aReloaded cache.yml"));
            utils.getPlugin().reloadConfig();
            player.sendMessage(utils.color("&aReloaded config.yml"));
            player.sendMessage(utils.color("&cNote: Some changes requires a server restart."));
        }
    }
}
