package kr.kieran.tntpaper.listeners;

import kr.kieran.tntpaper.TNTPaper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

public class TNTPaperListener implements Listener {

    private TNTPaper plugin;

    public TNTPaperListener(TNTPaper plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDispense(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        int radius = plugin.getDataUtils().tntPaperRadius;
        int halfRadius = radius / 2;
        boolean factionOnly = plugin.getDataUtils().tntPaperFactionOnly;
        ItemStack hand = event.getItem();
        if (hand != null && plugin.getItemUtils().isTNTPaper(hand)) {
            event.setCancelled(true);
            int playerX = player.getLocation().getBlockX();
            int playerY = player.getLocation().getBlockY();
            int playerZ = player.getLocation().getBlockZ();
            int maxTNT = plugin.getItemUtils().getMaximumTNT(hand);
            if (event.hasBlock() && (event.getClickedBlock().getType().equals(Material.CHEST) || event.getClickedBlock().getType().equals(Material.TRAPPED_CHEST) || event.getClickedBlock().getType().equals(Material.DISPENSER)) && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (plugin.getCooldownUtils().hasLoadCooldown(player.getUniqueId())) {
                    player.sendMessage(plugin.getDataUtils().loadOnCooldown.replace("%time%", String.valueOf(new DecimalFormat("0.0").format(plugin.getCooldownUtils().getLoadMillisecondsLeft(player.getUniqueId()) / 1000.0))));
                    return;
                }
                Block block = event.getClickedBlock();
                Location blockLocation = block.getLocation();
                boolean insideFaction = plugin.getFactionUtils().isInFactionClaim(player, blockLocation);
                boolean insideForeign = plugin.getFactionUtils().isInForeignClaim(player, blockLocation);
                if (factionOnly && !insideFaction) {
                    player.sendMessage(plugin.getDataUtils().notInFactionClaim);
                    return;
                }
                if (insideForeign) {
                    player.sendMessage(plugin.getDataUtils().notInFactionClaim);
                    return;
                }
                Set<Inventory> inventories = new HashSet<>();
                if (event.getClickedBlock().getType().equals(Material.CHEST) || event.getClickedBlock().getType().equals(Material.TRAPPED_CHEST)) {
                    Chest chest = (Chest) block.getState();
                    if (chest.getInventory() instanceof DoubleChestInventory) {
                        DoubleChest doubleChest = (DoubleChest) chest.getInventory().getHolder();
                        inventories.add(doubleChest.getInventory());
                    } else {
                        inventories.add(chest.getBlockInventory());
                    }
                } else {
                    Dispenser dispenser = (Dispenser) block.getState();
                    inventories.add(dispenser.getInventory());
                    for (int x = playerX - halfRadius; x < playerX + halfRadius; ++x) {
                        for (int y = playerY - halfRadius; y < playerY + halfRadius; ++y) {
                            for (int z = playerZ - halfRadius; z < playerZ + halfRadius; ++z) {
                                Location location = new Location(player.getWorld(), x, y, z);
                                Block selected = location.getBlock();
                                if (selected.getType().equals(Material.DISPENSER) && plugin.getFactionUtils().isInFactionClaim(player, location)) {
                                    Dispenser selectedDispenser = (Dispenser) selected.getState();
                                    inventories.add(selectedDispenser.getInventory());
                                }
                            }
                        }
                    }
                }
                int total = 0;
                int currentTNTBalance = plugin.getItemUtils().getTNTBalance(hand);
                for (Inventory inventory : inventories) {
                    if (inventory == null) {
                        continue;
                    }
                    for (ItemStack stack : inventory.getContents().clone()) {
                        if (stack != null) {
                            if (stack.getType().equals(Material.TNT)) {
                                if ((maxTNT != -1) && currentTNTBalance + total >= maxTNT) {
                                    break;
                                }
                                int amount = stack.getAmount();
                                int currentTotal = currentTNTBalance + total;
                                if ((maxTNT != -1) && currentTotal + amount >= maxTNT) {
                                    amount = maxTNT - currentTotal;
                                }
                                if (amount <= 0) {
                                    break;
                                }
                                ItemStack remove = stack.clone();
                                remove.setAmount(amount);
                                inventory.removeItem(remove);
                                total += remove.getAmount();
                            }
                        }
                    }
                }
                if (total > 0) {
                    ItemStack newPaper = plugin.getItemUtils().createTNTPaper(currentTNTBalance + total, maxTNT);
                    player.setItemInHand(newPaper);
                    player.sendMessage(plugin.getDataUtils().tntPaperLoaded.replace("%tnt%", String.valueOf(total)));
                } else {
                    player.sendMessage(plugin.getDataUtils().noTntLoaded);
                }
                plugin.getCooldownUtils().putLoadCooldown(player.getUniqueId(), plugin.getDataUtils().loadCooldown);
            } else {
                if (plugin.getCooldownUtils().hasDistributeCooldown(player.getUniqueId())) {
                    player.sendMessage(plugin.getDataUtils().distributeOnCooldown.replace("%time%", String.valueOf(new DecimalFormat("0.0").format(plugin.getCooldownUtils().getDistributeMillisecondsLeft(player.getUniqueId()) / 1000.0))));
                    return;
                }
                plugin.getCooldownUtils().putDistributeCooldown(player.getUniqueId(), plugin.getDataUtils().distributeCooldown);
                int noteBalance = plugin.getItemUtils().getTNTBalance(hand);
                int totalDispensers = 0;
                int totalTNT = 0;
                int dispenserCount = 0;
                for (int x = playerX - halfRadius; x < playerX + halfRadius; ++x) {
                    for (int y = playerY - halfRadius; y < playerY + halfRadius; ++y) {
                        for (int z = playerZ - halfRadius; z < playerZ + halfRadius; ++z) {
                            Location location = new Location(player.getWorld(), x, y, z);
                            Block block = location.getBlock();
                            if (block.getType().equals(Material.DISPENSER)) {
                                ++dispenserCount;
                            }
                        }
                    }
                }
                if (dispenserCount <= 0) {
                    player.sendMessage(plugin.getDataUtils().noTntDistributed);
                    return;
                }
                int amountForEach = noteBalance / dispenserCount;
                if (amountForEach < 0) {
                    amountForEach = 1;
                }
                for (int x = playerX - halfRadius; x < playerX + halfRadius; ++x) {
                    for (int y = playerY - halfRadius; y < playerY + halfRadius; ++y) {
                        for (int z = playerZ - halfRadius; z < playerZ + halfRadius; ++z) {
                            Location location = new Location(player.getWorld(), x, y, z);
                            Block block = location.getBlock();
                            if (block.getType().equals(Material.DISPENSER)) {
                                boolean insideFaction = plugin.getFactionUtils().isInFactionClaim(player, location);
                                boolean insideForeign = plugin.getFactionUtils().isInForeignClaim(player, location);
                                if (!factionOnly || (insideFaction && !insideForeign)) {
                                    Dispenser dispenser = (Dispenser) block.getState();
                                    Inventory inventory = dispenser.getInventory();
                                    boolean countedDispenser = false;
                                    int dispenserAmount = 0;
                                    for (int i = 0; i < inventory.getSize(); ++i) {
                                        ItemStack stack = inventory.getItem(i);
                                        if (stack == null || stack.getType().equals(Material.TNT)) {
                                            int amountCanHold = (stack == null) ? inventory.getMaxStackSize() : (inventory.getMaxStackSize() - stack.getAmount());
                                            if (amountCanHold > 0) {
                                                int amountToPut = amountCanHold;
                                                if (noteBalance < amountToPut) {
                                                    amountToPut = noteBalance;
                                                }
                                                if (amountToPut > amountForEach) {
                                                    amountToPut = amountForEach;
                                                }
                                                if (dispenserAmount >= amountForEach) {
                                                    break;
                                                }
                                                if (dispenserAmount + amountToPut > amountForEach) {
                                                    amountToPut = amountForEach - dispenserAmount;
                                                }
                                                dispenserAmount += amountToPut;
                                                inventory.addItem(new ItemStack(Material.TNT, amountToPut));
                                                totalTNT += amountToPut;
                                                if (!countedDispenser) {
                                                    ++totalDispensers;
                                                    countedDispenser = true;
                                                }
                                                noteBalance -= amountToPut;
                                                if (noteBalance <= 0) {
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (noteBalance <= 0) {
                    ItemStack newPaper = plugin.getItemUtils().createTNTPaper(0, maxTNT);
                    player.getInventory().setItemInHand(newPaper);
                } else {
                    ItemStack newPaper = plugin.getItemUtils().createTNTPaper(noteBalance, maxTNT);
                    player.getInventory().setItemInHand(newPaper);
                }
                if (totalTNT > 0) {
                    player.sendMessage(plugin.getDataUtils().tntPaperDistributed.replace("%tnt%", String.valueOf(totalTNT)).replace("%dispensers%", String.valueOf(totalDispensers)).replace("%radius%", String.valueOf(radius)));
                }
            }
        }
    }

}
