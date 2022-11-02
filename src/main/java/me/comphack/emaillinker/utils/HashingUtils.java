package me.comphack.emaillinker.utils;

import me.comphack.emaillinker.Emaillinker;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

public class HashingUtils {

    private final Utils utils = new Utils();
    private final UserCache cache = new UserCache(utils.getPlugin(), "cache.yml");


    public String hashVerificationCode(String code) {
        try{
            return toHexString(getSHA(code));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    public boolean verifyPasswordHash(String hash, UUID player) throws NoSuchAlgorithmException {
        cache.reloadCache();
        //Hashed password which the user has entered which he has received via email
        String userhash = toHexString(getSHA(hash));
        //Hashed password which server has saved (Correct One)
        String systemhash = cache.getYaml().getString(player.toString() + ".hashedCode");
        if(userhash.equalsIgnoreCase(systemhash)) {
            return true;
        }
        return false;
    }

    public String getOTP() {
        int code;
        Random rnd = new Random();
        code = rnd.nextInt(999999);
        return String.valueOf(code);
    }

    public boolean hasPendingVerification(UUID player) {
        cache.reloadCache();
        if(cache.getYaml().contains(player.toString() + ".username")) {
            return true;
        } else {
            return false;
        }
    }
}
