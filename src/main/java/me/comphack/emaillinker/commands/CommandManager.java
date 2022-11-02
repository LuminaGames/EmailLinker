package me.comphack.emaillinker.commands;

import me.comphack.emaillinker.commands.subcommands.*;
import me.comphack.emaillinker.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;

public class CommandManager implements CommandExecutor {

    private ArrayList<SubCommand> subcommands = new ArrayList<>();
    private Utils utils = new Utils();
    public CommandManager() {
        subcommands.add(new LinkCommand());
        subcommands.add(new HelpCommand());
        subcommands.add(new ReloadCommand());
        subcommands.add(new CodeCommand());
        subcommands.add(new DisconnectCommand());
        subcommands.add(new ResendCommand());
        subcommands.add(new AdminCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 0) {
                for (int i = 0; i < getSubcommands().size(); i++) {
                    if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                        try {
                            getSubcommands().get(i).perform(p, args);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (args.length == 0) {
                p.sendMessage("");
                p.sendMessage(utils.color("&b&lEmail Linker &7developed by &b&lCOMPHACK"));
                p.sendMessage(utils.color("&b&lPlugin Version: &f" + utils.getPluginVersion()));
                p.sendMessage(utils.color("&bUse /emaillinker help for using any subcommands."));
                p.sendMessage("");
            }
        } else {
            sender.sendMessage(utils.color(utils.getPlugin().getConfig().getString("messages.player_only_command")
                    .replace("{prefix}", utils.color(utils.getPlugin().getConfig().getString("messages.prefix"
                    )))));
        }

        return true;
    }

    public ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }


}
