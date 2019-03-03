package kr.kieran.tntpaper.listeners;

import kr.kieran.tntpaper.TNTPaper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerListeners implements Listener {

    private TNTPaper plugin;

    public PlayerListeners(TNTPaper plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        handleMemoryLeaks(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        handleMemoryLeaks(event.getPlayer().getUniqueId());
    }

    private void handleMemoryLeaks(UUID uuid) {
        if (plugin.getCooldownUtils().hasDistributeCooldown(uuid)) {
            plugin.getCooldownUtils().removeDistributeCooldown(uuid);
        }
        if (plugin.getCooldownUtils().hasLoadCooldown(uuid)) {
            plugin.getCooldownUtils().removeLoadCooldown(uuid);
        }
    }

}
