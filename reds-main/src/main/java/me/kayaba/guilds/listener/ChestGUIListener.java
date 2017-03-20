package me.kayaba.guilds.listener;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

public class ChestGUIListener extends AbstractListener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = CompatibilityUtils.getClickedInventory(event);
        if (inventory == null
                || event.getCurrentItem() == null
                || (!inventory.equals(event.getView().getTopInventory()) && event.getClick() != ClickType.SHIFT_LEFT && event.getClick() != ClickType.SHIFT_RIGHT)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        GPlayer nPlayer = PlayerManager.getPlayer(player);
        GUIInventory guiInventory = nPlayer.getGuiInventory();

        if (guiInventory != null) {
            event.setCancelled(true);

            if (event.getSlot() == inventory.getSize() - 1 && event.getCurrentItem().equals(Message.INVENTORY_GUI_BACK.getItemStack())) {
                player.closeInventory();
                return;
            }

            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                guiInventory.onClick(event);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        final GPlayer nPlayer = PlayerManager.getPlayer(event.getPlayer());
        if (nPlayer.getGuiInventory() != null && !ChestGUIUtils.guiContinueList.contains(nPlayer)) {
            if (nPlayer.getGuiInventoryHistory().size() == 1) {
                nPlayer.setGuiInventory(null);
            } else {
                nPlayer.removeLastGUIInventoryHistory();

                PracticeServer.runTask(new Runnable() {
                    @Override
                    public void run() {
                        nPlayer.getGuiInventory().open(nPlayer);
                    }
                });
            }
        }
    }
}
