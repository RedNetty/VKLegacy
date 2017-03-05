package me.bpweber.practiceserver.Crates;

import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.drops.CreateDrop;
import me.bpweber.practiceserver.vendors.MerchantMechanics;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class CratesMain implements Listener {
    public ArrayList<Player> meme = new ArrayList<Player>();
    public ArrayList<Player> yes = new ArrayList<Player>();
    public HashMap<Player, Integer> tier = new HashMap<Player, Integer>();

    public void onEnable() {
        PracticeServer.log.info("[PracticeServer] Crates Enabled");
        Bukkit.getServer().getPluginManager().registerEvents(this, PracticeServer.plugin);
        new BukkitRunnable() {

            public void run() {
                String prefix = ChatColor.GRAY + "[" + ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "AR" + ChatColor.GRAY + "]";
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    p.sendMessage(prefix + ChatColor.GREEN + " Help pay for the server today @ AutismRealms.buycraft.net! Buy Gem Pouches, Ranks, And Unbans!");
                }
            }
        }.runTaskTimerAsynchronously(PracticeServer.plugin, 2200, 2200);

    }

    public void onDisable() {
        PracticeServer.log.info("[PracticeServer] Crates Disabled");

    }

    public void doFirework(Player p) {
        Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(Color.YELLOW).withFade(Color.YELLOW).with(FireworkEffect.Type.BURST).trail(true).build();
        fwm.addEffect(effect);
        fwm.setPower(0);
        fw.setFireworkMeta(fwm);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.25f);
    }

    @EventHandler
    public void onInvClickA(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        ItemStack item = null;
        if (e.getWhoClicked() instanceof Player) {
            if (e.getCurrentItem() != null || e.getWhoClicked().getGameMode() == GameMode.SURVIVAL) {
                if (e.getInventory().getHolder() != null && e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    if (e.getInventory().getHolder() == p && e.getCurrentItem().getType() == Material.TRAPPED_CHEST) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Basic")) {
                            item = unlockCrate(1);
                        } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Medium")) {
                            item = unlockCrate(2);
                        } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("War")) {
                            item = unlockCrate(3);
                        }else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Ancient")) {
                            item = unlockCrate(4);
                        }
                        if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Basic") ||
                                e.getCurrentItem().getItemMeta().getDisplayName().contains("Medium") ||
                                e.getCurrentItem().getItemMeta().getDisplayName().contains("War") || e.getCurrentItem().getItemMeta().getDisplayName().contains("Ancient")){
                            if (e.getCurrentItem().getAmount() == 1) {
                                e.setCurrentItem(null);
                            }
                            if (e.getCurrentItem().getAmount() > 1) {
                                e.getCurrentItem().setAmount(e.getCurrentItem().getAmount() - 1);
                            }
                            if (!p.isOp() && p.getGameMode() == GameMode.SURVIVAL) {
                                p.getInventory().addItem(item);
                                doFirework(p);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void crateScrap(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (meme.contains(e.getPlayer())) {
            e.setCancelled(true);
            if (e.getMessage().equalsIgnoreCase("Y") || e.getMessage().equalsIgnoreCase("Yes")) {
                int tiers = tier.get(p);
                meme.remove(p);
                tier.remove(p);
                if (tiers == 1) {
                    final ItemStack scrap = CraftItemStack.asCraftCopy(MerchantMechanics.T1_scrap);
                    scrap.setAmount(120);
                    p.getInventory().addItem(scrap);
                }
                if (tiers == 2) {
                    final ItemStack scrap = CraftItemStack.asCraftCopy(MerchantMechanics.T2_scrap);
                    scrap.setAmount(60);
                    p.getInventory().addItem(scrap);
                }
                if (tiers == 3) {
                    final ItemStack scrap = CraftItemStack.asCraftCopy(MerchantMechanics.T3_scrap);
                    scrap.setAmount(30);
                    p.getInventory().addItem(scrap);
                }
                if (tiers == 4) {
                    final ItemStack scrap = CraftItemStack.asCraftCopy(MerchantMechanics.T4_scrap);
                    scrap.setAmount(15);
                    p.getInventory().addItem(scrap);
                }
                if (tiers == 5) {
                    final ItemStack scrap = CraftItemStack.asCraftCopy(MerchantMechanics.T5_scrap);
                    scrap.setAmount(5);
                    p.getInventory().addItem(scrap);
                }
            } else {
                e.getPlayer().getInventory().addItem(createCrate(tier.get(p)));
                tier.remove(p);
                meme.remove(p);
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if (e.getPlayer().getInventory().getItemInMainHand() != null) {
                if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.TRAPPED_CHEST && !meme.contains(e.getPlayer())) {

                    meme.add(e.getPlayer());
                    e.getPlayer().sendMessage(ChatColor.GREEN + "Are you sure you want to scrap this item? " +
                            ChatColor.BOLD + "'Y'" + ChatColor.GREEN + " or " + ChatColor.RED.toString() + ChatColor.BOLD + "'N'");
                    if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Legendary")) {
                        tier.put(e.getPlayer(), 5);
                    } else if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Ancient")) {
                        tier.put(e.getPlayer(), 4);
                    } else if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("War")) {
                        tier.put(e.getPlayer(), 3);
                    } else if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Medium")) {

                    } else if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Basic")) {
                        tier.put(e.getPlayer(), 1);
                    }
                    if (e.getPlayer().getInventory().getItemInMainHand().getAmount() == 1) {
                        e.getPlayer().getInventory().setItemInMainHand(null);
                    }
                    if (e.getPlayer().getInventory().getItemInMainHand().getAmount() > 1) {
                        e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            if (e.getCurrentItem() != null || e.getWhoClicked().getGameMode() == GameMode.SURVIVAL) {
                if (e.getInventory().getHolder() == p && e.getCurrentItem().getType() == Material.TRAPPED_CHEST && e.getCursor().getType() == Material.TRIPWIRE_HOOK) {
                    ItemStack item1 = new ItemStack(Material.AIR);
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Legendary")) {
                        item1 = unlockCrate(5);
                    }
                    doFirework(p);
                    if (e.getCursor().getAmount() == 1) {
                        e.setCursor(null);
                    }
                    if (e.getCurrentItem().getAmount() == 1) {
                        e.setCurrentItem(null);
                    }
                    if (e.getCursor().getAmount() > 1) {
                        e.getCursor().setAmount(e.getCursor().getAmount() - 1);
                    }
                    if (e.getCurrentItem().getAmount() > 1) {
                        e.getCurrentItem().setAmount(e.getCurrentItem().getAmount() - 1);
                    }
                    if (!p.isOp() && p.getGameMode() == GameMode.SURVIVAL) {
                        p.getInventory().addItem(item1);
                    }
                }
            }
        }
    }

    public static ItemStack createKey() {
        ItemStack crate = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta cm = crate.getItemMeta();
        new Random();
        cm.setDisplayName(ChatColor.AQUA + "Crate key");
        cm.setLore(Arrays.asList(ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "This key is used for locked crates."));
        crate.setItemMeta(cm);
        return crate;

    }

    public static ItemStack unlockCrate(int tier) {
        Random r = new Random();
        ItemStack i;
        int item = r.nextInt(8);
        int rarityd = r.nextInt(200);
        int rarity = 0;
        if (rarityd <= 15) {
            rarity = 2;
        }
        if (rarityd > 15 && rarityd <= 50) {
            rarity = 1;
        }
        if (rarityd > 50 && rarityd <= 85 || rarityd >= 200) {
            rarity = 0;
        }
        if (rarityd > 85 && rarityd <= 100) {
            rarity = 3;
        }
        switch (tier) {
            case 5:
                i = CreateDrop.createDrop(5, item, rarity);
                break;
            case 4:
                i = CreateDrop.createDrop(4, item, rarity);
                break;
            case 3:
                i = CreateDrop.createDrop(3, item, rarity);
                break;
            case 2:
                i = CreateDrop.createDrop(2, item, rarity);
                break;
            case 1:
                i = CreateDrop.createDrop(1, item, rarity);
                break;
            default:
                i = CreateDrop.createDrop(1, item, rarity);
                break;

        }
        return i;

    }

    public static ItemStack createCrate(int tier) {
        ItemStack crate = new ItemStack(Material.TRAPPED_CHEST);
        ItemMeta cm = crate.getItemMeta();
        String fLine = "";
        switch (tier) {
            case 5:
                cm.setDisplayName(ChatColor.YELLOW + "Legendary Loot Crate");
                fLine = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "Inside:" + ChatColor.YELLOW + " Randomized Piece of Tier 5";
                break;
            case 4:
                cm.setDisplayName(ChatColor.LIGHT_PURPLE + "Ancient Loot Crate");
                fLine = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "Inside:" + ChatColor.LIGHT_PURPLE + " Randomized Piece of Tier 4";
                break;
            case 3:
                cm.setDisplayName(ChatColor.AQUA + "War Loot Crate");
                fLine = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "Inside:" + ChatColor.AQUA + " Randomized Piece of Tier 3";
                break;
            case 2:
                cm.setDisplayName(ChatColor.GREEN + "Medium Loot Crate");
                fLine = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "Inside:" + ChatColor.GREEN + " Randomized Piece of Tier 2";
                break;
            case 1:
                cm.setDisplayName(ChatColor.WHITE + "Basic Loot Crate");
                fLine = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "Inside:" + ChatColor.WHITE + " Randomized Piece of Tier 1";
                break;
            default:
                cm.setDisplayName(ChatColor.WHITE + "Basic Loot Crate");
                fLine = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "Inside:" + ChatColor.WHITE + " Randomized Piece of Tier 1";
                break;
        }
        if (tier > 2) {
            cm.setLore(Arrays.asList(fLine, ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "Looks like it needs a key to be opened."));
        }
        if (tier <= 2) {
            cm.setLore(Arrays.asList(fLine, ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "Unlocked loot chest."));
        }
        crate.setItemMeta(cm);
        return crate;
    }
}