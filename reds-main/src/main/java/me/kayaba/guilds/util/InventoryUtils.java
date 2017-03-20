package me.kayaba.guilds.util;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

import java.util.*;

public final class InventoryUtils {
    private InventoryUtils() {

    }


    public static void removeItems(Player player, List<ItemStack> items) {
        for (ItemStack item : items) {
            if (player.getGameMode() != GameMode.CREATIVE || item.hasItemMeta()) {
                player.getInventory().removeItem(item);
            }
        }
    }


    public static boolean containsItems(Inventory inventory, List<ItemStack> items) {
        return getMissingItems(inventory, items).isEmpty();
    }


    public static List<ItemStack> getMissingItems(Inventory inventory, List<ItemStack> items) {
        final List<ItemStack> missing = new ArrayList<>();

        if (items != null && inventory.getType() != InventoryType.CREATIVE) {
            for (ItemStack item : items) {
                if (!containsAtLeast(inventory, item, item.getAmount())) {
                    ItemStack missingItemStack = item.clone();
                    missingItemStack.setAmount(item.getAmount() - getTotalAmountOfItemStackInInventory(inventory, item));
                    missing.add(missingItemStack);
                }
            }
        }

        return missing;
    }


    public static int getTotalAmountOfItemStackInInventory(Inventory inventory, ItemStack itemStack) {
        int amount = 0;

        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() != Material.AIR && ItemStackUtils.isSimilar(itemStack, item)) {
                amount += item.getAmount();
            }
        }

        return amount;
    }


    public static boolean isEmpty(Inventory inventory) {
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                return false;
            }
        }
        return true;
    }


    public static boolean containsAtLeast(Inventory inventory, ItemStack itemStack, int amount) {
        return getTotalAmountOfItemStackInInventory(inventory, itemStack) >= amount;
    }
}
