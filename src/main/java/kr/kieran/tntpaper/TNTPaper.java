package kr.kieran.tntpaper;

import kr.kieran.tntpaper.commands.TNTPaperCommand;
import kr.kieran.tntpaper.listeners.PlayerListeners;
import kr.kieran.tntpaper.listeners.TNTPaperListener;
import kr.kieran.tntpaper.utilities.*;
import kr.kieran.tntpaper.utilities.nbt.ItemNBTAPI;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class TNTPaper extends JavaPlugin {

    private static TNTPaper instance;
    private ItemNBTAPI itemNBTAPI;
    private ConfigUtilities configUtilities;
    private DataUtilities dataUtilities;
    private ItemUtilities itemUtilities;
    private FactionUtilities factionUtilities;
    private CooldownUtilities cooldownUtilities;

    @Override
    public void onEnable() {
        instance = this;
        if (getServer().getPluginManager().getPlugin("Factions") == null) {
            getLogger().severe("Factions must be installed for TNTPaper to work.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        itemNBTAPI = new ItemNBTAPI(this);
        configUtilities = new ConfigUtilities(this);
        dataUtilities = new DataUtilities();
        itemUtilities = new ItemUtilities(this);
        factionUtilities = new FactionUtilities();
        cooldownUtilities = new CooldownUtilities();

        getCommand("tntpaper").setExecutor(new TNTPaperCommand(this));
        getServer().getPluginManager().registerEvents(new TNTPaperListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);
        getLogger().info(getDescription().getName() + " version " + getDescription().getVersion() + " has been enabled.");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        getLogger().info(getDescription().getName() + " version " + getDescription().getVersion() + " has been disabled.");
        instance = null;
    }

    public ConfigUtilities getConfigUtils() {
        return configUtilities;
    }

    public DataUtilities getDataUtils() {
        return dataUtilities;
    }

    public ItemUtilities getItemUtils() {
        return itemUtilities;
    }

    public FactionUtilities getFactionUtils() {
        return factionUtilities;
    }

    public CooldownUtilities getCooldownUtils() {
        return cooldownUtilities;
    }

    public ItemNBTAPI getItemNBTAPI() {
        return itemNBTAPI;
    }

    public static TNTPaper getInstance() {
        return instance;
    }

}
