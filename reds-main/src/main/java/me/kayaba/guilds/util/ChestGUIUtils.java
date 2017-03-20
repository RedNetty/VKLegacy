package me.kayaba.guilds.util;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import org.bukkit.*;
import org.bukkit.inventory.*;

import java.util.*;

public class ChestGUIUtils {
    public static final List<GPlayer> guiContinueList = new ArrayList<>();

    private ChestGUIUtils() {

    }


    public static int getChestSize(int count) {
        return count == 0 ? 9 : (count / 9) * 9 + (count % 9 == 0 ? 0 : 9);
    }


    public static void openGUIInventory(GPlayer nPlayer, GUIInventory guiInventory) {
        if (nPlayer.isOnline()) {
            nPlayer.setGuiInventory(guiInventory);

            if (!guiContinueList.contains(nPlayer)) {
                guiContinueList.add(nPlayer);
            }

            guiInventory.getInventory().clear();
            guiInventory.getExecutors().clear();
            guiInventory.generateContent();
            addBackItem(guiInventory);

            nPlayer.getPlayer().openInventory(guiInventory.getInventory());

            guiContinueList.remove(nPlayer);

            guiInventory.onOpen();
        }
    }


    public static Inventory createInventory(int size, String title) {
        if (title.length() > 32) {
            title = title.substring(0, 32);
        }

        return Bukkit.createInventory(null, size, title);
    }


    public static Inventory createInventory(int size, MessageWrapper title) {
        return createInventory(size, title.get());
    }


    public static void addBackItem(GUIInventory guiInventory) {
        Inventory inventory = guiInventory.getInventory();
        GPlayer nPlayer = guiInventory.getViewer();

        ItemStack lastItem = inventory.getItem(inventory.getSize() - 1);
        if ((lastItem == null || lastItem.getType() == Material.AIR) && nPlayer.getGuiInventoryHistory().size() > 1) {
            inventory.setItem(inventory.getSize() - 1, Message.INVENTORY_GUI_BACK.getItemStack());
        }
    }
}
