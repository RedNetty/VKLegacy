package me.bpweber.practiceserver.DonationMechanics.Nametags;

import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.chat.ChatMechanics;
import me.bpweber.practiceserver.item.Items;
import me.bpweber.practiceserver.profession.ProfessionMechanics;
import net.minecraft.server.v1_9_R2.ItemArmor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

/**
 * Created by jaxon on 3/14/2017.
 */
public class Nametag implements Listener {
    public static HashMap<String, ItemStack> item_name_change = new HashMap<String, ItemStack>();
    public static ItemStack item_ownership_tag;

    static {
        item_ownership_tag = Items.signNewCustomItem(Material.ENCHANTED_BOOK, String.valueOf(ChatColor.GOLD.toString()) + "Item Name Tag", String.valueOf(ChatColor.GOLD.toString()) + "Uses: " + ChatColor.GRAY + "1" + "," + ChatColor.GRAY.toString() + ChatColor.ITALIC + "Apply to any weapon or armor piece" + "," + ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "to give it a custom display name." + "," + ChatColor.GRAY + "Permanent Untradeable");
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, PracticeServer.plugin);
    }

    public static int getEmptyInventorySlots(final Player pl) {
        int empty_slots = 0;
        for (final ItemStack is : pl.getInventory()) {
            if (is == null || is.getType() == Material.AIR) {
                ++empty_slots;
            }
        }
        return empty_slots;
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        final Player pl = e.getPlayer();
        if (item_name_change.containsKey(pl.getName()) && getEmptyInventorySlots(pl) >= 2) {
            pl.getInventory().addItem(item_name_change.get(pl.getName()));
        }
        item_name_change.remove(pl.getName());
    }

    public static int getItemTier(final ItemStack i) {
        try {
            final String name = i.getItemMeta().getDisplayName();
            if (name.contains(ChatColor.GREEN.toString())) {
                return 2;
            }
            if (name.contains(ChatColor.AQUA.toString())) {
                return 3;
            }
            if (name.contains(ChatColor.LIGHT_PURPLE.toString())) {
                return 4;
            }
            if (name.contains(ChatColor.YELLOW.toString())) {
                return 5;
            }
            if (name.contains(ChatColor.WHITE.toString())) {
                return 1;
            }
            return 1;
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    public boolean isItemOwnershipTag(final ItemStack is) {
        return is != null && is.getType() == Material.ENCHANTED_BOOK && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && ChatColor.stripColor(is.getItemMeta().getDisplayName()).equalsIgnoreCase("Item Name Tag");
    }

    public static ChatColor getTierColor(final int tier) {
        if (tier == 1) {
            return ChatColor.WHITE;
        }
        if (tier == 2) {
            return ChatColor.GREEN;
        }
        if (tier == 3) {
            return ChatColor.AQUA;
        }
        if (tier == 4) {
            return ChatColor.LIGHT_PURPLE;
        }
        if (tier == 5) {
            return ChatColor.YELLOW;
        }
        return ChatColor.WHITE;
    }

    public boolean isArmororWeapon(ItemStack is) {
        return is.getType().name().contains("_SWORD") || is.getType().name().contains("_AXE") || is.getType().name().contains("_SPADE") || is.getType().name().contains("_HOE") || CraftItemStack.asNMSCopy(is).getItem() instanceof ItemArmor;
    }

    public boolean containsSymbols(final String s) {
        return s.contains("-") || s.contains("[") || s.contains("]") || s.contains("%") || s.contains("$") || s.contains("#") || s.contains("@") || s.contains("!") || s.contains("^") || s.contains("+") || s.contains("=");
    }

    @EventHandler
    public void onNameChoose(AsyncPlayerChatEvent e) {
        Player pl = e.getPlayer();
        if (item_name_change.containsKey(pl.getName())) {
            e.setCancelled(true);
            String msg = e.getMessage();
            if (msg.equalsIgnoreCase("cancel")) {
                final int free_slots = getEmptyInventorySlots(pl);
                if (free_slots >= 2) {
                    item_name_change.remove(pl.getName());
                    ItemStack nametag = item_ownership_tag;
                    nametag.setAmount(1);
                    pl.getInventory().addItem(nametag);
                    pl.getInventory().addItem(item_name_change.get(pl.getName()));
                    pl.sendMessage(ChatColor.RED + "Item Name Change - " + ChatColor.BOLD + "CANCELLED");
                    return;
                }
                pl.sendMessage(ChatColor.RED + "Please ensure you have empty space in your inventory before trying to cancel this operation.");
            } else {
                msg = ChatColor.stripColor(ChatMechanics.censorMessage(msg));
                if (this.containsSymbols(msg)) {
                    pl.sendMessage(ChatColor.RED + "Your message cannot contain symbols. Please type your name again.");
                    return;
                }
                if (msg.length() > 40) {
                    pl.sendMessage(ChatColor.RED + "Your message is " + msg.length() + "/40 characters. Please shorten it.");
                    return;
                }
                final ItemStack is = item_name_change.get(pl.getName());
                ItemMeta im = is.getItemMeta();
                final ChatColor cc = getTierColor(getItemTier(is));
                String i_name = String.valueOf(cc.toString()) + ChatColor.ITALIC + msg + ChatColor.RESET + cc.toString() + ChatColor.BOLD + " EC";
                if (im.hasDisplayName()) {
                    final String old_name = im.getDisplayName();
                    if (old_name.startsWith(String.valueOf(ChatColor.RED.toString()) + "[+")) {
                        final String scrolled_status = old_name.substring(0, old_name.indexOf(" "));
                        i_name = String.valueOf(scrolled_status) + " " + i_name;
                    }
                }
                im.setDisplayName(i_name);
                is.setItemMeta(im);
                pl.getInventory().addItem(is);
                pl.playSound(pl.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0f, 1.2f);
                pl.updateInventory();
                item_name_change.remove(pl.getName());
            }
        }
    }

    @EventHandler
    public void onItemNametagUse(final InventoryClickEvent e) {
        final Player pl = (Player) e.getWhoClicked();
        if (e.getInventory().getName().equalsIgnoreCase("container.crafting") && e.getCursor() != null && this.isItemOwnershipTag(e.getCursor()) && e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
            ItemStack o_current = e.getCurrentItem();
            e.setCancelled(true);
            final ItemStack x_current = e.getCurrentItem();
            if (ProfessionMechanics.isSkillItem(o_current)) {
                pl.sendMessage(ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " modify this item.");
                return;
            }
            if (!isArmororWeapon(o_current)) {
                pl.sendMessage(ChatColor.RED + "You " + ChatColor.UNDERLINE + "can" + ChatColor.RED + " only use this on Armor or Weapons!");
                return;
            }
            if (o_current.getAmount() > 1) {
                pl.sendMessage(ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " use this on stacked items.");
                return;
            }
            x_current.setAmount(1);
            item_name_change.put(pl.getName(), x_current);
            final ItemStack is = e.getCursor();
            ItemStack return_is = new ItemStack(Material.AIR);
            if (is.getAmount() > 1) {
                final int current_amount = is.getAmount();
                is.setAmount(current_amount - 1);
                return_is = is;
            }
            e.setCursor(return_is);
            e.setCurrentItem(new ItemStack(Material.AIR));
            if (o_current.getAmount() <= 1) {
                o_current = new ItemStack(Material.AIR);
            } else if (o_current.getAmount() > 1) {
                o_current = e.getCurrentItem();
                final int current_amount = o_current.getAmount();
                o_current.setAmount(current_amount - 1);
                pl.getInventory().addItem(o_current);
                pl.updateInventory();
            }
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PracticeServer.plugin, new Runnable() {
                @Override
                public void run() {
                    pl.closeInventory();
                    pl.sendMessage(ChatColor.GOLD + "Please enter up to a " + ChatColor.UNDERLINE + "40 CHARACTER" + ChatColor.GOLD + " name to assign this item.");
                    pl.sendMessage(ChatColor.RED + "This opperation is non-refundable and non-reversable, type 'cancel' to void it.");
                }
            }, 2L);
        }
    }
}
