package me.comphack.emaillinker.utils;


import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UserCache {

    // Path to the config file
    private Path file;

    // Path to the data directory
    private Path dir;

    // The configuration for the config
    private YamlConfiguration yaml;

    public UserCache(JavaPlugin plugin, String name) {

        // Sets the directories location
        dir = plugin.getDataFolder().toPath();

        // If it doesn't exist, create it, and replace the value
        // with the newly created directory
        if(!Files.exists(dir)) {
            try {
                dir = Files.createDirectory(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Saves the file from the resources directory to the data directory
        plugin.saveResource(name, false);

        // Creates the file in the data directory
        file = dir.resolve(name);

        // New configuration
        yaml = new YamlConfiguration();

        // Loads the file data into the configuration
        try {
            yaml.load(file.toString());
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }




    // Saves the configuration data
    public void save() {
        try {
            yaml.save(file.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Simple Getter
    public YamlConfiguration getYaml() {
        return yaml;
    }

    public void reloadCache() {
        try{
            yaml.load(file.toString());
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }


    // Gets the path to the data directory
    public Path getDataPath() {
        return dir;
    }
}
