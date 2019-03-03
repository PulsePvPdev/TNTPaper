package kr.kieran.tntpaper.utilities;

import kr.kieran.tntpaper.TNTPaper;
import kr.kieran.tntpaper.utilities.nbt.NBTItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.logging.Level;

public class ItemUtilities {

    private TNTPaper plugin;

    public ItemUtilities(TNTPaper plugin) {
        this.plugin = plugin;
    }

    public ItemStack createTNTPaper(int amount, int maximum) {
        Material material = Material.getMaterial(plugin.getDataUtils().tntPaperMaterial);
        if (material == null) {
            plugin.getLogger().log(Level.SEVERE, "Material for TNT paper is invalid.");
            return null;
        }

        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();

        List<String> preLore;
        if (maximum == -1) {
            preLore = plugin.getDataUtils().tntPaperLore;
        } else {
            preLore = plugin.getDataUtils().tntPaperLoreMax;
        }
        if (preLore != null) {
            for (int i = 0; i < preLore.size(); ++i) {
                preLore.set(i, plugin.getDataUtils().c(preLore.get(i)));
            }
            meta.setLore(preLore);
        }

        List<String> lore = meta.getLore();
        for (int i = 0; i < lore.size(); ++i) {
            lore.set(i, lore.get(i).replace("%amount%", String.valueOf(amount)).replace("%max%", String.valueOf(maximum)));
        }
        meta.setLore(lore);

        meta.setDisplayName(plugin.getDataUtils().tntPaperName);

        if (plugin.getDataUtils().tntPaperEnchanted) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        stack.setItemMeta(meta);

        NBTItem nbtItem = plugin.getItemNBTAPI().getNBTItem(stack);
        nbtItem.setInteger("tntpaper", amount);
        nbtItem.setInteger("maxtnt", maximum);
        int currentId = plugin.getConfigUtils().getConfig().getInt("item.current-id");
        nbtItem.setInteger("paperid", currentId);
        plugin.getConfigUtils().getConfig().set("item.current-id", currentId + 1);
        plugin.getConfigUtils().saveConfigurations();

        return nbtItem.getItem();
    }

    public int getTNTBalance(ItemStack stack) {
        NBTItem nbtItem = plugin.getItemNBTAPI().getNBTItem(stack);
        return nbtItem.getInteger("tntpaper");
    }

    public int getMaximumTNT(ItemStack stack) {
        NBTItem nbtItem = plugin.getItemNBTAPI().getNBTItem(stack);
        return nbtItem.getInteger("maxtnt");
    }

    public boolean isTNTPaper(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        NBTItem nbtItem = plugin.getItemNBTAPI().getNBTItem(stack);
        return nbtItem.hasKey("tntpaper");
    }

}
