package me.comphack.emaillinker.utils;

import me.comphack.emaillinker.Emaillinker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Utils {


        public void sendPluginLog(String type, String log) {if(type.equalsIgnoreCase("info")) {
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&7[&bLinker&7] &f" + log));
        } else if(type.equalsIgnoreCase("error")) {
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&7[&bLinker&7] &c" + log));
                }
        }

        public String color(String s) {
                if (s == null) return "";
                return ChatColor.translateAlternateColorCodes('&', s);
        }


        public Emaillinker getPlugin() {
                Emaillinker emaillinker = JavaPlugin.getPlugin(Emaillinker.class);
                return emaillinker;
        }

        public String getPluginVersion() {
                String ver = Bukkit.getServer().getPluginManager().getPlugin("EmailLinker").getDescription().getVersion();
                return ver;
        }

        public void sendEmailCode(String email) {

        }

}
