package me.comphack.emaillinker.commands;

import me.comphack.emaillinker.Emaillinker;
import me.comphack.emaillinker.database.Database;
import me.comphack.emaillinker.utils.*;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;



import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class EmailCommand implements CommandExecutor {


    private HashingUtils hashingUtils = new HashingUtils();
    private Utils utils = new Utils();
    private Database database = new Database();
    private UserCache cache = new UserCache(JavaPlugin.getPlugin(Emaillinker.class), "cache.yml");


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String otp = hashingUtils.getOTP();
        String HOST = utils.getPlugin().getConfig().getString("smtp.hostname");
        int PORT = utils.getPlugin().getConfig().getInt("smtp.port");
        String userName = utils.getPlugin().getConfig().getString("smtp.username");
        boolean SSL_FLAG = utils.getPlugin().getConfig().getBoolean("smtp.usessl");
        String password = utils.getPlugin().getConfig().getString("smtp.password");
        String fromAddress = utils.getPlugin().getConfig().getString("smtp.fromaddress");
        String toAddress = args[0];
        String subject = utils.getPlugin().getConfig().getString("email-settings.subject")
                .replace("{servername}", utils.getPlugin().getConfig().getString("email-settings.server-name"));

        Player player = (Player) sender;
        String message =  utils.getPlugin().getConfig().getString("email-settings.body")
                .replace("{code}", otp)
                .replace("{player}", player.getName())
                .replace("{servername}", utils.getPlugin().getConfig().getString("email-settings.servername"));
        UUID uuid = player.getUniqueId();
        String username = player.getName();
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.player_only_command")));
        } else if (!player.hasPermission("emaillinker.command.email")) {

            player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.no_permission")));
            return true;
        }

        if(args[0].equalsIgnoreCase("disconnect")) {


        }

        if(args[0].equalsIgnoreCase("resend")) {
            if(!database.hasLinkedEmail(uuid)) {
                if(args[1] == null) {
                    player.sendMessage("No Email Entered");
                }
                    try {
                        HtmlEmail email = new HtmlEmail();
                        email.setHostName(HOST);
                        email.setSmtpPort(PORT);
                        email.setAuthenticator(new DefaultAuthenticator(userName, password));
                        email.setSSLOnConnect(SSL_FLAG);
                        email.setFrom(fromAddress);
                        email.setSubject(subject);
                        email.setHtmlMsg(message);
                        email.addTo(toAddress);
                        email.send();
                        player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.email_send_success")));

                        cache.getYaml().set(uuid.toString(), uuid.toString());
                        cache.getYaml().set(uuid.toString() + ".username", username);
                        cache.getYaml().set(uuid.toString() + ".hashedCode", hashingUtils.hashVerificationCode(otp));
                        cache.getYaml().set(uuid.toString() + ".emailAddress", args[1]);
                        cache.save();
                    } catch (Exception ex) {
                        player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.email_error")));
                        ex.printStackTrace();
                    }
            }
        }

        if (!database.hasLinkedEmail(uuid)) {
            if (args[0].equalsIgnoreCase("code")) {
                try {
                    //Check if the given code matches the SHA Hash on the system.
                    if (hashingUtils.verifyPasswordHash(args[1], uuid)) {
                        database.setEmail(username, uuid, cache.getYaml().getString(uuid.toString() + ".emailAddress"));
                        player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.verification_success")));
                    } else {
                        player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.code_not_matched")));
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            return true;
            }
            if (!hashingUtils.hasPendingVerification(uuid)) {

                try {
                    HtmlEmail email = new HtmlEmail();
                    email.setHostName(HOST);
                    email.setSmtpPort(PORT);
                    email.setAuthenticator(new DefaultAuthenticator(userName, password));
                    email.setSSLOnConnect(SSL_FLAG);
                    email.setFrom(fromAddress);
                    email.setSubject(subject);
                    email.setHtmlMsg(message);
                    email.addTo(toAddress);
                    email.send();
                    player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.email_send_success")));
                    cache.getYaml().set(uuid.toString(), uuid.toString());
                    cache.getYaml().set(uuid.toString() + ".username", username);
                    cache.getYaml().set(uuid.toString() + ".hashedCode", hashingUtils.hashVerificationCode(otp));
                    cache.getYaml().set(uuid.toString() + ".emailAddress", args[0]);
                    cache.save();
                } catch (Exception ex) {
                    player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.email_error")));
                    ex.printStackTrace();
                }

            } else {
                player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.already_pending_verification")));
            }
            return false;
        } else {
            player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.email_already_linked")));
        }
        return false;
    }
}
