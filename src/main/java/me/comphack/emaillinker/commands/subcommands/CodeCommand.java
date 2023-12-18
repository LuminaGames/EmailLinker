package me.comphack.emaillinker.commands.subcommands;

import me.comphack.emaillinker.Emaillinker;
import me.comphack.emaillinker.api.events.EmailLinkAttempt;
import me.comphack.emaillinker.api.events.EmailLinkSuccess;
import me.comphack.emaillinker.commands.SubCommand;
import me.comphack.emaillinker.database.Database;
import me.comphack.emaillinker.utils.HashingUtils;
import me.comphack.emaillinker.utils.UserCache;
import me.comphack.emaillinker.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.UUID;

public class CodeCommand extends SubCommand {

    private HashingUtils hashingUtils = new HashingUtils();
    private Utils utils = new Utils();
    private Database database = new Database();
    private UserCache cache = new UserCache(JavaPlugin.getPlugin(Emaillinker.class), "cache.yml");

    @Override
    public String getName() {
        return "code";
    }

    @Override
    public String getDescription() {
        return "Enter the code received on your email address";
    }

    @Override
    public String getSyntax() {
        return "/email code <Code>";
    }

    @Override
    public void perform(Player player, String[] args) throws SQLException {
        UUID uuid = player.getUniqueId();
        String username = player.getName();


        if(player.hasPermission("emaillinker.command.code") || player.hasPermission("emaillinker.command.*")) {
            if(hashingUtils.hasPendingVerification(uuid)) {
                if(!database.hasLinkedEmail(uuid)) {
                    if(args.length <= 1) {
                        player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.no_code_provided")));
                    } else {
                        try {
                            if(hashingUtils.verifyPasswordHash(args[1], uuid)){
                                cache.reloadCache();
                                database.setEmail(username, uuid, cache.getYaml().getString(uuid.toString() + ".emailAddress"));
                                player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.verification_success")));
                                Bukkit.getServer().getPluginManager().callEvent(new EmailLinkAttempt(player, cache.getYaml().getString(uuid.toString() + ".emailAddress"), true));
                                Bukkit.getServer().getPluginManager().callEvent(new EmailLinkSuccess(player, cache.getYaml().getString(uuid.toString() + ".emailAddress")));
                                cache.getYaml().set(uuid.toString(), null);
                                cache.save();
                            } else {
                                player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.code_not_matched")));
                                Bukkit.getServer().getPluginManager().callEvent(new EmailLinkAttempt(player, cache.getYaml().getString(uuid.toString() + ".emailAddress"), false));

                            }
                        } catch (NoSuchAlgorithmException e) {
                            player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.internal_plugin_error")));
                            e.printStackTrace();
                        }
                    }
                } else {
                    player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.email_already_linked")));
                }
            } else {
                player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.no_pending_verification")));
            }
        } else {
            player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.no_permission")));
        }

    }
}
