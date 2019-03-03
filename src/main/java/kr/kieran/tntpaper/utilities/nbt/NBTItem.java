package kr.kieran.tntpaper.utilities.nbt;

import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class NBTItem extends NBTCompound {

    private ItemStack bukkitItem;

    public NBTItem(ItemStack item) {
        super(null, null);
        if (item == null) {
            throw new NullPointerException("ItemStack can't be null!");
        }
        bukkitItem = item.clone();
    }

    protected Object getCompound() {
        return NBTReflectionUtil.getItemRootNBTTagCompound(Objects.requireNonNull(ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, bukkitItem)));
    }

    protected void setCompound(Object compound) {
        Object stack = ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, bukkitItem);
        ReflectionMethod.ITEMSTACK_SET_TAG.run(stack, compound);
        bukkitItem = (ItemStack) ReflectionMethod.ITEMSTACK_BUKKITMIRROR.run(null, stack);
    }

    public ItemStack getItem() {
        return bukkitItem;
    }

    protected void setItem(ItemStack item) {
        bukkitItem = item;
    }

    /**
     * This may return true even when the NBT is empty.
     *
     * @return Does the ItemStack have a NBTCompound.
     */
    public boolean hasNBTData() {
        return getCompound() != null;
    }

    public static NBTContainer convertItemtoNBT(ItemStack item) {
        return NBTReflectionUtil.convertNMSItemtoNBTCompound(Objects.requireNonNull(ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, item)));
    }

    public static ItemStack convertNBTtoItem(NBTCompound comp) {
        return (ItemStack) ReflectionMethod.ITEMSTACK_BUKKITMIRROR.run(null, NBTReflectionUtil.convertNBTCompoundtoNMSItem(comp));
    }

}
