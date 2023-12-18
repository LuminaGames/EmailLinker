package me.comphack.emaillinker.commands.subcommands;

import me.comphack.emaillinker.Emaillinker;
import me.comphack.emaillinker.api.events.EmailLinkEvent;
import me.comphack.emaillinker.commands.SubCommand;
import me.comphack.emaillinker.database.Database;
import me.comphack.emaillinker.utils.HashingUtils;
import me.comphack.emaillinker.utils.UserCache;
import me.comphack.emaillinker.utils.Utils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.UUID;

public class LinkCommand extends SubCommand {
    private HashingUtils hashingUtils = new HashingUtils();
    private Utils utils = new Utils();
    private Database database = new Database();
    private UserCache cache = new UserCache(JavaPlugin.getPlugin(Emaillinker.class), "cache.yml");


    @Override
    public String getName() {
        return "link";
    }

    @Override
    public String getDescription() {
        return "Link your email with the server";
    }

    @Override
    public String getSyntax() {
        return "/email link <EmailAddress>";
    }

    @Override
    public void perform(Player player, String[] args) {
        String verificationCode = hashingUtils.getOTP();
        String smtp_username = utils.getPlugin().getConfig().getString("smtp.username");
        String smtp_password = utils.getPlugin().getConfig().getString("smtp.password");
        String smtp_hostname = utils.getPlugin().getConfig().getString("smtp.hostname");
        boolean useSSL = utils.getPlugin().getConfig().getBoolean("smtp.useSSL");
        String fromAddress = utils.getPlugin().getConfig().getString("smtp.fromaddress");
        int port = utils.getPlugin().getConfig().getInt("smtp.port");
        String subject = utils.getPlugin().getConfig().getString("email-settings.subject")
                .replace("{servername}", utils.getPlugin().getConfig().getString("email-settings.server-name"));
        String message = utils.getPlugin().getConfig().getString("email-settings.body")
                .replace("{code}", verificationCode)
                .replace("{player}", player.getName());
        UUID playerUUID = player.getUniqueId();
        String username = player.getName();

        if(player.hasPermission("emaillinker.command.link") || player.hasPermission("emaillinker.command.*")){
            if (args.length <= 1) {
                player.sendMessage("No email provided");
            } else {
                if (!database.hasLinkedEmail(playerUUID)) {
                    if (!hashingUtils.hasPendingVerification(playerUUID)) {
                        Thread thread = new Thread(() -> {
                            try {
                                HtmlEmail email = new HtmlEmail();
                                email.setHostName(smtp_hostname);
                                email.setSmtpPort(port);
                                email.setAuthenticator(new DefaultAuthenticator(smtp_username, smtp_password));
                                email.setSSLOnConnect(useSSL);
                                email.setFrom(fromAddress);
                                email.setSubject(subject);
                                email.setHtmlMsg(message);
                                email.addTo(args[1]);
                                email.send();

                                cache.getYaml().set(playerUUID.toString(), playerUUID.toString());
                                cache.getYaml().set(playerUUID.toString() + ".username", username);
                                cache.getYaml().set(playerUUID.toString() + ".hashedCode", hashingUtils.hashVerificationCode(verificationCode));
                                cache.getYaml().set(playerUUID.toString() + ".emailAddress", args[1]);
                                Bukkit.getPluginManager().callEvent(new EmailLinkEvent(player, args[1]));
                                cache.save();
                                player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.email_send_success")));
                            } catch (EmailException e) {
                                e.printStackTrace();
                            }
                        });
                        thread.start();

                    } else {
                        // you have already linked email
                        player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.already_pending_verification")));
                    }
                } else {
                    // has already a pending verification
                    player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.email_already_linked")));
                }
            }
        } else {
            // no permission
            player.sendMessage(utils.color(player, utils.getPlugin().getConfig().getString("messages.no_permission")));
        }
    }
}
