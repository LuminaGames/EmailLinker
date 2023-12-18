package me.comphack.emaillinker.commands.subcommands;

import me.comphack.emaillinker.commands.SubCommand;
import me.comphack.emaillinker.utils.Utils;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class AdminCommand extends SubCommand {

    Utils utils = new Utils();

    @Override
    public String getName() {
        return "admin";
    }

    @Override
    public String getDescription() {
        return "Admin Help Command";
    }

    @Override
    public String getSyntax() {
        return "/email admin";
    }

    @Override
    public void perform(Player player, String[] args) throws SQLException {
        for (String string : utils.getPlugin().getConfig().getStringList("messages.admin-help-1")) {
            player.sendMessage(utils.color(player, string));
        }
    }
}
