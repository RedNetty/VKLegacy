package me.bpweber.practiceserver.profession;

import me.bpweber.practiceserver.PracticeServer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Mining
        implements Listener {
    ConcurrentHashMap<Location, Integer> regenores = new ConcurrentHashMap<Location, Integer>();
    HashMap<Location, Material> oretypes = new HashMap<Location, Material>();

    public void onEnable() {
        PracticeServer.log.info("[Mining] has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents((Listener) this, PracticeServer.plugin);
        new BukkitRunnable() {

            public void run() {
                for (Location loc : Mining.this.regenores.keySet()) {
                    int time = Mining.this.regenores.get(loc);
                    if (time < 1) {
                        Mining.this.regenores.remove(loc);
                        loc.getBlock().setType(Mining.this.oretypes.get(loc));
                        continue;
                    }
                    Mining.this.regenores.put(loc, --time);
                }
            }
        }.runTaskTimer(PracticeServer.plugin, 20, 20);
    }

    public void onDisable() {
        regenOre();
        PracticeServer.log.info("[Mining] has been disabled.");
    }

    public void regenOre() {
        for (Location loc : Mining.this.regenores.keySet()) {
            Mining.this.regenores.remove(loc);
            loc.getBlock().setType(Mining.this.oretypes.get(loc));
        }
    }

    public static ItemStack ore(int tier) {
        Material m = null;
        ChatColor cc = ChatColor.WHITE;
        String name = "";
        String lore = "";
        if (tier == 1) {
            m = Material.COAL_ORE;
            name = "Coal";
            lore = "A chunk of coal ore.";
        }
        if (tier == 2) {
            m = Material.EMERALD_ORE;
            name = "Emerald";
            lore = "An unrefined piece of emerald ore.";
            cc = ChatColor.GREEN;
        }
        if (tier == 3) {
            m = Material.IRON_ORE;
            name = "Iron";
            lore = "A piece of raw iron.";
            cc = ChatColor.AQUA;
        }
        if (tier == 4) {
            m = Material.DIAMOND_ORE;
            name = "Diamond";
            lore = "A sharp chunk of diamond ore.";
            cc = ChatColor.LIGHT_PURPLE;
        }
        if (tier == 5) {
            m = Material.GOLD_ORE;
            name = "Gold";
            lore = "A sparkling piece of gold ore.";
            cc = ChatColor.YELLOW;
        }
        ItemStack is = new ItemStack(m);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(cc + name + " Ore");
        is.setAmount(2);
        im.setLore(Arrays.asList(ChatColor.GRAY.toString() + ChatColor.ITALIC + lore));
        is.setItemMeta(im);
        return is;
    }


    @SuppressWarnings("deprecation")
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (!p.getInventory().getItemInMainHand().getType().name().contains("_PICKAXE")) {
            return;
        }
        Material m = e.getBlock().getType();
        if (m == Material.COAL_ORE || m == Material.EMERALD_ORE || m == Material.IRON_ORE || m == Material.DIAMOND_ORE || m == Material.GOLD_ORE) {
            Random random = new Random();
            int dura = random.nextInt(2000);
            int fail = random.nextInt(100);
            if (dura < p.getInventory().getItemInMainHand().getType().getMaxDurability()) {
                p.getInventory().getItemInMainHand().setDurability((short) (p.getInventory().getItemInMainHand().getDurability() + 1));
            }
            if (p.getInventory().getItemInMainHand().getDurability() >= p.getInventory().getItemInMainHand().getType().getMaxDurability()) {
                p.setItemInHand(null);
            }
            if (p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getType() == Material.AIR) {
                return;
            }
            p.updateInventory();
            this.oretypes.put(e.getBlock().getLocation(), m);
            int oretier = ProfessionMechanics.getOreTier(m);
            int level = ProfessionMechanics.getPickaxeLevel(p.getInventory().getItemInMainHand());
            if (oretier > 0 && oretier <= ProfessionMechanics.getPickaxeTier(p.getInventory().getItemInMainHand())) {
                e.setCancelled(true);
                e.getBlock().setType(Material.STONE);
                if (p.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
                    p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                }
                this.regenores.put(e.getBlock().getLocation(), oretier * 30);
                if (fail < ProfessionMechanics.getFailPercent(oretier, level)) {
                    this.addToInv(p, Mining.ore(oretier));
                    int gemfind = random.nextInt(100);
                    int dore = random.nextInt(100);
                    int tore = random.nextInt(100);
                    if (gemfind < ProfessionMechanics.getPickEnchants(p.getInventory().getItemInMainHand(), "GEM FIND")) {
                        ItemStack gem;
                        ItemMeta gm;
                        int gemamt = 0;
                        if (oretier == 1) {
                            gemamt = random.nextInt(32) + 1;
                        }
                        if (oretier == 2) {
                            gemamt = random.nextInt(33) + 32;
                        }
                        if (oretier == 3) {
                            gemamt = random.nextInt(65) + 64;
                        }
                        if (oretier == 4) {
                            gemamt = random.nextInt(100) + 50;
                        }
                        if (oretier == 5) {
                            gemamt = random.nextInt(200) + 100;
                        }
                        p.sendMessage("          " + ChatColor.YELLOW + ChatColor.BOLD + "FOUND " + gemamt + " GEM(s)");
                        while (gemamt > 0) {
                            gem = new ItemStack(Material.EMERALD, 64);
                            gm = gem.getItemMeta();
                            gm.setDisplayName(ChatColor.WHITE + "Gem");
                            gm.setLore(Arrays.asList(ChatColor.GRAY + "The currency of Andalucia"));
                            gem.setItemMeta(gm);
                            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), gem);
                            gemamt -= 64;
                        }
                        gem = new ItemStack(Material.EMERALD, gemamt);
                        gm = gem.getItemMeta();
                        gm.setDisplayName(ChatColor.WHITE + "Gem");
                        gm.setLore(Arrays.asList(ChatColor.GRAY + "The currency of Andalucia"));
                        gem.setItemMeta(gm);
                        e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), gem);
                    }
                    if (dore < ProfessionMechanics.getPickEnchants(p.getInventory().getItemInMainHand(), "DOUBLE ORE")) {
                        p.sendMessage("          " + ChatColor.YELLOW + ChatColor.BOLD + "DOUBLE ORE DROP" + ChatColor.YELLOW + " (2x)");
                        this.addToInv(p, Mining.ore(oretier));
                    }
                    if (tore < ProfessionMechanics.getPickEnchants(p.getInventory().getItemInMainHand(), "TRIPLE ORE")) {
                        p.sendMessage("          " + ChatColor.YELLOW + ChatColor.BOLD + "TRIPLE ORE DROP" + ChatColor.YELLOW + " (3x)");
                        this.addToInv(p, Mining.ore(oretier));
                        this.addToInv(p, Mining.ore(oretier));
                    }
                    int xp = ProfessionMechanics.getExpFromOre(oretier);
                    ProfessionMechanics.addExp(p, p.getInventory().getItemInMainHand(), xp);
                } else {
                    e.setCancelled(true);
                    p.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "You fail to gather any ore.");
                    e.getBlock().getWorld().playEffect(e.getBlock().getLocation(), Effect.STEP_SOUND, Material.STONE);
                }
            } else {
                e.setCancelled(true);
                p.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "You cannot mine this ore.");
            }
        }
    }

    public void addToInv(Player p, ItemStack is) {
        ItemStack[] arritemStack = p.getInventory().getContents();
        int n = arritemStack.length;
        int n2 = 0;
        while (n2 < n) {
            ItemStack i = arritemStack[n2];
            if (i != null && i.getType() != Material.AIR && (i.getAmount()) < 64 && i.getType() == is.getType() && i.getItemMeta().equals(is.getItemMeta())) {
                p.getInventory().addItem(new ItemStack[]{is});
                return;
            }
            ++n2;
        }
        int slot = p.getInventory().firstEmpty();
        if (slot != -1) {
            p.getInventory().setItem(slot, is);
        } else {
            p.getWorld().dropItemNaturally(p.getLocation(), is);
        }
    }

}

