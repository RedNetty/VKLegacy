package me.kayaba.guilds.util;

import me.kayaba.guilds.manager.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

import java.lang.reflect.*;
import java.util.*;

public class CompatibilityUtils {
    private static Method getOnlinePlayersMethod;

    static {
        try {
            getOnlinePlayersMethod = Server.class.getMethod("getOnlinePlayers");
        } catch (NoSuchMethodException e) {
            LoggerUtils.exception(e);
        }
    }


    @SuppressWarnings("unchecked")
    public static Collection<Player> getOnlinePlayers() {
        Collection<Player> collection = new HashSet<>();

        try {
            if (getOnlinePlayersMethod.getReturnType().equals(Collection.class)) {
                collection = ((Collection) getOnlinePlayersMethod.invoke(Bukkit.getServer()));
            } else {
                Player[] array = ((Player[]) getOnlinePlayersMethod.invoke(Bukkit.getServer()));
                Collections.addAll(collection, array);
            }
        } catch (Exception e) {
            LoggerUtils.exception(e);
        }

        return collection;
    }


    @SuppressWarnings("deprecation")
    public static ItemStack getItemInMainHand(Player player) {
        if (ConfigManager.getServerVersion().isOlderThan(ConfigManager.ServerVersion.MINECRAFT_1_9_R1)) {
            return player.getItemInHand();
        } else {
            return player.getInventory().getItemInMainHand();
        }
    }


    public static Inventory getClickedInventory(InventoryClickEvent event) {
        int slot = event.getRawSlot();
        InventoryView view = event.getView();

        if (slot < 0) {
            return null;
        } else if (view.getTopInventory() != null && slot < view.getTopInventory().getSize()) {
            return view.getTopInventory();
        } else {
            return view.getBottomInventory();
        }
    }
}
