package kr.kieran.tntpaper.utilities;

import kr.kieran.tntpaper.TNTPaper;
import org.bukkit.ChatColor;

import java.util.List;

public class DataUtilities {

    private TNTPaper plugin = TNTPaper.getInstance();

    public String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public String noPermission = c(plugin.getConfigUtils().getLang().getString("no-permission"));
    public String invalidUsage = c(plugin.getConfigUtils().getLang().getString("invalid-usage"));
    public String invalidPlayer = c(plugin.getConfigUtils().getLang().getString("invalid-player"));
    public String configReloaded = c(plugin.getConfigUtils().getLang().getString("config-reloaded"));
    public String tntIdsReset = c(plugin.getConfigUtils().getLang().getString("tnt-ids-reset"));
    public String infiniteTntPaperSent = c(plugin.getConfigUtils().getLang().getString("infinite-tnt-paper-sent"));
    public String infiniteTntPaperReceived = c(plugin.getConfigUtils().getLang().getString("infinite-tnt-paper-received"));
    public String maximumTntPaperSent = c(plugin.getConfigUtils().getLang().getString("maximum-tnt-paper-sent"));
    public String maximumTntPaperReceived = c(plugin.getConfigUtils().getLang().getString("maximum-tnt-paper-received"));
    public String notInFactionClaim = c(plugin.getConfigUtils().getLang().getString("not-in-faction-claim"));
    public String tntPaperLoaded = c(plugin.getConfigUtils().getLang().getString("tnt-paper-loaded"));
    public String tntPaperDistributed = c(plugin.getConfigUtils().getLang().getString("tnt-paper-distributed"));
    public String loadOnCooldown = c(plugin.getConfigUtils().getLang().getString("load-on-cooldown"));
    public String distributeOnCooldown = c(plugin.getConfigUtils().getLang().getString("distribute-on-cooldown"));
    public String noTntDistributed = c(plugin.getConfigUtils().getLang().getString("no-tnt-distributed"));
    public String noTntLoaded = c(plugin.getConfigUtils().getLang().getString("no-tnt-loaded"));
    public List<String> helpMessage = plugin.getConfigUtils().getLang().getStringList("help-message");

    String tntPaperMaterial = plugin.getConfigUtils().getConfig().getString("item.material");
    String tntPaperName = c(plugin.getConfigUtils().getConfig().getString("item.name"));
    boolean tntPaperEnchanted = plugin.getConfigUtils().getConfig().getBoolean("item.enchanted");
    public boolean tntPaperFactionOnly = plugin.getConfigUtils().getConfig().getBoolean("faction-only");
    public int tntPaperRadius = plugin.getConfigUtils().getConfig().getInt("radius");
    public int loadCooldown = plugin.getConfigUtils().getConfig().getInt("load-cooldown");
    public int distributeCooldown = plugin.getConfigUtils().getConfig().getInt("distribute-cooldown");
    List<String> tntPaperLore = plugin.getConfigUtils().getConfig().getStringList("item.lore");
    List<String> tntPaperLoreMax = plugin.getConfigUtils().getConfig().getStringList("item.lore-max");

}
