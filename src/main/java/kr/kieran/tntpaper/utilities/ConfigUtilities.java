package kr.kieran.tntpaper.utilities;

import kr.kieran.tntpaper.TNTPaper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.logging.Level;

public class ConfigUtilities {

    private TNTPaper plugin;
    private File configFile, langFile;
    private FileConfiguration config, lang;

    public ConfigUtilities(TNTPaper plugin) {
        this.plugin = plugin;
        loadConfigurations();
    }

    private void loadConfigurations() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        langFile = new File(plugin.getDataFolder(), "lang.yml");

        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        if (!langFile.exists()) {
            plugin.saveResource("lang.yml", false);
        }

        config = new YamlConfiguration();
        lang = new YamlConfiguration();
        try {
            config.load(configFile);
            lang.load(langFile);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load configuration files.");
            plugin.getLogger().log(Level.SEVERE, "Error: " + e.getMessage());
        }
    }

    public void reloadConfigurations() {
        try {
            config.load(configFile);
            lang.load(langFile);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to reload configuration files.");
            plugin.getLogger().log(Level.SEVERE, "Error: " + e.getMessage());
        }
    }

    public void saveConfigurations() {
        try {
            config.save(configFile);
            lang.save(langFile);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save configuration files.");
            plugin.getLogger().log(Level.SEVERE, "Error: " + e.getMessage());
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    FileConfiguration getLang() {
        return lang;
    }

}
