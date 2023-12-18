package me.comphack.emaillinker.commands.subcommands;

import me.comphack.emaillinker.Emaillinker;
import me.comphack.emaillinker.commands.SubCommand;
import me.comphack.emaillinker.database.Database;
import me.comphack.emaillinker.utils.HashingUtils;
import me.comphack.emaillinker.utils.UserCache;
import me.comphack.emaillinker.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.UUID;

public class DisconnectCommand extends SubCommand {

    private Utils utils = new Utils();
    private Database database = new Database();

    @Override
    public String getName() {
        return "disconnect";
    }

    @Override
    public String getDescription() {
        return "Disconnect your linked email from the server";
    }

    @Override
    public String getSyntax() {
        return "/email disconnect <OldEmail>";
    }

    @Override
    public void perform(Player player, String[] args) throws SQLException {
        UUID uuid = player.getUniqueId();
        if(player.hasPermission("emaillinker.command.code") || player.hasPermission("emaillinker.command.*")) {
            if(args.length <= 1) {
                player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.no_email_provided")));
            } else {
                if (database.hasLinkedEmail(uuid)) {
                    if (database.checkEmail(uuid, args[1])) {
                        database.disconnectEmail(uuid);
                        player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.disconnect_success")));
                    } else {
                        player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.no_email_linked")));
                    }
                } else {
                    player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.no_email_linked")));
                }
            }
        }
    }
}
