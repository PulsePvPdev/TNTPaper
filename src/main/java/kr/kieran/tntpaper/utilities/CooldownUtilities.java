package kr.kieran.tntpaper.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownUtilities {

    private Map<UUID, Long> loadCooldownMap;
    private Map<UUID, Long> distributeCooldownMap;

    public CooldownUtilities() {
        loadCooldownMap = new HashMap<>();
        distributeCooldownMap = new HashMap<>();
    }

    public void putLoadCooldown(UUID uuid, long time) {
        long value = System.currentTimeMillis() + time * 1000L;
        loadCooldownMap.put(uuid, value);
    }

    public boolean hasLoadCooldown(UUID uuid) {
        if (!loadCooldownMap.containsKey(uuid)) {
            return false;
        }
        long value = loadCooldownMap.get(uuid);
        return value > System.currentTimeMillis();
    }

    public void removeLoadCooldown(UUID uuid) {
        loadCooldownMap.remove(uuid);
    }

    public long getLoadMillisecondsLeft(UUID uuid) {
        if (!hasLoadCooldown(uuid)) {
            return -1L;
        }
        return loadCooldownMap.get(uuid) - System.currentTimeMillis();
    }

    public void putDistributeCooldown(UUID uuid, long time) {
        long value = System.currentTimeMillis() + time * 1000L;
        distributeCooldownMap.put(uuid, value);
    }

    public void removeDistributeCooldown(UUID uuid) {
        distributeCooldownMap.remove(uuid);
    }

    public boolean hasDistributeCooldown(UUID uuid) {
        if (!distributeCooldownMap.containsKey(uuid)) {
            return false;
        }
        long value = distributeCooldownMap.get(uuid);
        return value > System.currentTimeMillis();
    }

    public long getDistributeMillisecondsLeft(UUID uuid) {
        if (!hasDistributeCooldown(uuid)) {
            return -1L;
        }
        return distributeCooldownMap.get(uuid) - System.currentTimeMillis();
    }

}
