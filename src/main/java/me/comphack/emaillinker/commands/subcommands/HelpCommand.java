package me.comphack.emaillinker.commands.subcommands;

import me.comphack.emaillinker.commands.SubCommand;
import me.comphack.emaillinker.utils.Utils;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class HelpCommand extends SubCommand {

    Utils utils = new Utils();

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Help command for the plugin";
    }

    @Override
    public String getSyntax() {
        return "/emaillinker help";
    }

    @Override
    public void perform(Player player, String[] args) throws SQLException {
        for (String string : utils.getPlugin().getConfig().getStringList("messages.help-1")) {
            player.sendMessage(utils.color(player, string));
        }
    }
}
