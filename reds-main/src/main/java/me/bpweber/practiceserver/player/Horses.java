/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.AttributeInstance
 *  net.minecraft.server.v1_7_R4.EntityLiving
 *  net.minecraft.server.v1_7_R4.GenericAttributes
 *  net.minecraft.server.v1_7_R4.IAttribute
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity
 *  org.bukkit.entity.AnimalTamer
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Horse
 *  org.bukkit.entity.Horse$Color
 *  org.bukkit.entity.Horse$Style
 *  org.bukkit.entity.Horse$Variant
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Vehicle
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.vehicle.VehicleExitEvent
 *  org.bukkit.inventory.HorseInventory
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package me.bpweber.practiceserver.player;

import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.ModerationMechanics.ModerationMechanics;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Setrank;
import me.bpweber.practiceserver.money.Money;
import me.bpweber.practiceserver.pvp.Alignments;
import me.bpweber.practiceserver.utils.ParticleEffect;
import me.bpweber.practiceserver.vendors.ItemVendors;
import net.minecraft.server.v1_9_R2.GenericAttributes;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftLivingEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Horses
        implements Listener {
    public static HashMap<String, Integer> mounting = new HashMap<String, Integer>();
    static HashMap<String, Integer> horsetier = new HashMap<String, Integer>();
    static HashMap<String, Location> mountingloc = new HashMap<String, Location>();
    static HashMap<String, ItemStack> buyingitem = new HashMap<String, ItemStack>();
    static HashMap<String, Integer> buyingprice = new HashMap<String, Integer>();

    public void onEnable() {
        PracticeServer.log.info("[Horses] has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents((Listener) this, PracticeServer.plugin);
        new BukkitRunnable() {

            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (!p.isOnline() || !Horses.mounting.containsKey(p.getName())) continue;
                    ParticleEffect.SPELL.display(0.0f, 0.0f, 0.0f, 0.5f, 80, p.getLocation().add(0.0, 0.15, 0.0), 20.0);
                    if (Horses.mounting.get(p.getName()) == 0) {
                        ParticleEffect.CRIT.display(0.0f, 0.0f, 0.0f, 0.5f, 80, p.getLocation().add(0.0, 1.0, 0.0), 20.0);
                        Horses.mounting.remove(p.getName());
                        Horses.mountingloc.remove(p.getName());
                        Horses.horse(p, Horses.horsetier.get(p.getName()));
                        continue;
                    }
                    if (Horses.mounting.get(p.getName()) == 5) {
                        String name = Horses.mount(Horses.horsetier.get(p.getName()), false).getItemMeta().getDisplayName();
                        p.sendMessage(ChatColor.BOLD + "SUMMONING " + name + ChatColor.WHITE + " ... " + Horses.mounting.get(p.getName()) + "s");
                        Horses.mounting.put(p.getName(), Horses.mounting.get(p.getName()) - 1);
                        continue;
                    }
                    p.sendMessage(ChatColor.BOLD + "SUMMONING" + ChatColor.WHITE + " ... " + Horses.mounting.get(p.getName()) + "s");
                    Horses.mounting.put(p.getName(), Horses.mounting.get(p.getName()) - 1);
                }
            }
        }.runTaskTimer(PracticeServer.plugin, 20, 20);
    }

    public void onDisable() {
        PracticeServer.log.info("[Horses] has been disabled.");
    }

    public static ItemStack mount(int tier, boolean inshop) {
        ItemStack is = new ItemStack(Material.SADDLE);
        ItemMeta im = is.getItemMeta();
        String name = ChatColor.GREEN + "Old Horse Mount";
        String req = "";
        ArrayList<String> lore = new ArrayList<String>();
        String line = "An old brown starter horse.";
        int speed = 120;
        int jump = 0;
        int price = 3000;
        if (tier == 3) {
            name = ChatColor.AQUA + "Traveler's Horse Mount";
            req = ChatColor.GREEN + "Old Horse Mount";
            line = "A standard healthy horse.";
            speed = 140;
            jump = 105;
            price = 7000;
        }
        if (tier == 4) {
            name = ChatColor.LIGHT_PURPLE + "Knight's Horse Mount";
            req = ChatColor.AQUA + "Traveler's Horse Mount";
            line = "A fast well-bred horse.";
            speed = 160;
            jump = 110;
            price = 15000;
        }
        if (tier == 5) {
            name = ChatColor.YELLOW + "War Stallion Mount";
            req = ChatColor.LIGHT_PURPLE + "Knight's Horse Mount";
            line = "A trusty powerful steed.";
            speed = 200;
            jump = 120;
            price = 30000;
        }
        im.setDisplayName(name);
        lore.add(ChatColor.RED + "Speed: " + speed + "%");
        if (jump > 0) {
            lore.add(ChatColor.RED + "Jump: " + jump + "%");
        }
        if (req != "" && inshop) {
            lore.add(ChatColor.RED.toString() + ChatColor.BOLD + "REQ: " + req);
        }
        lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + line);
        lore.add(ChatColor.GRAY + "Permenant Untradeable");
        if (inshop) {
            lore.add(ChatColor.GREEN + "Price: " + ChatColor.WHITE + price + "g");
        }
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    public static int getMountTier(ItemStack is) {
        if (is != null && is.getType() == Material.SADDLE && is.getItemMeta().hasDisplayName()) {
            String name = is.getItemMeta().getDisplayName();
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
        }
        return 0;
    }

    public static Horse horse(Player p, int tier) {
        double speed = 0.25;
        double jump = 0.75;
        if (tier == 3) {
            speed = 0.3;
            jump = 0.85;
        }
        if (tier == 4) {
            speed = 0.35;
            jump = 0.95;
        }
        if (tier == 5) {
            speed = 0.4;
            jump = 1.05;
        }
        Horse h = (Horse) p.getWorld().spawnEntity(p.getLocation(), EntityType.HORSE);
        h.setVariant(Horse.Variant.HORSE);
        h.setAdult();
        h.setTamed(true);
        h.setOwner((AnimalTamer) p);
        h.setColor(Horse.Color.BROWN);
        h.setAgeLock(true);
        h.setStyle(Horse.Style.NONE);
        h.setDomestication(100);
        h.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        if (tier == 3) {
            h.getInventory().setArmor(new ItemStack(Material.IRON_BARDING));
        }
        if (tier == 4) {
            h.getInventory().setArmor(new ItemStack(Material.DIAMOND_BARDING));
        }
        if (tier == 5) {
            h.getInventory().setArmor(new ItemStack(Material.GOLD_BARDING));
        }
        if (ModerationMechanics.isSub(p)) {
            h.getInventory().setArmor(null);
            if (Setrank.ranks.get(p.getName()).equalsIgnoreCase("sub")) {
                h.setVariant(Horse.Variant.UNDEAD_HORSE);
            }
            if (Setrank.ranks.get(p.getName()).equalsIgnoreCase("sub+")) {
                h.setColor(Horse.Color.GRAY);
                h.setFireTicks(Integer.MAX_VALUE);
                h.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1));
            }
            if (Setrank.ranks.get(p.getName()).equalsIgnoreCase("sub++")) {
                h.setVariant(Horse.Variant.SKELETON_HORSE);
            }
        }
        h.setMaxHealth(20.0);
        h.setHealth(20.0);
        h.setJumpStrength(jump);
        ((CraftLivingEntity) h).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(speed);
        h.setPassenger((Entity) p);
        return h;
    }

    @EventHandler
    public void onAnimalTamerClick(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Player && e.getRightClicked().hasMetadata("NPC")) {
            Player at = (Player) e.getRightClicked();
            Player p = e.getPlayer();
            if (at.getName().equalsIgnoreCase("animal tamer")) {
                Inventory inv = Bukkit.createInventory((InventoryHolder) null, (int) 9, (String) "Animal Tamer");
                inv.addItem(new ItemStack[]{Horses.mount(2, true)});
                inv.addItem(new ItemStack[]{Horses.mount(3, true)});
                inv.addItem(new ItemStack[]{Horses.mount(4, true)});
                inv.addItem(new ItemStack[]{Horses.mount(5, true)});
                p.openInventory(inv);
                p.playSound(p.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 1.0f, 1.0f);
            }
        }
    }

    @EventHandler
    public void onBuyHorse(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (p.getOpenInventory().getTopInventory().getTitle().contains("Animal Tamer")) {
            List<?> lore;
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.SADDLE && e.getCurrentItem().getItemMeta().hasLore() && ((String) (lore = e.getCurrentItem().getItemMeta().getLore()).get(lore.size() - 1)).contains("Price:")) {
                int price = ItemVendors.getPriceFromLore(e.getCurrentItem());
                if (Money.hasEnoughGems(p, price)) {
                    int currtier = 0;
                    ItemStack[] arritemStack = p.getInventory().getContents();
                    int n = arritemStack.length;
                    int n2 = 0;
                    while (n2 < n) {
                        ItemStack is = arritemStack[n2];
                        if (Horses.getMountTier(is) > currtier) {
                            currtier = Horses.getMountTier(is);
                        }
                        ++n2;
                    }
                    int newtier = Horses.getMountTier(e.getCurrentItem());
                    if (currtier == 0) {
                        currtier = 1;
                    }
                    if (newtier == currtier + 1) {
                        p.sendMessage(ChatColor.GRAY + "The '" + e.getCurrentItem().getItemMeta().getDisplayName() + ChatColor.GRAY + "' costs " + ChatColor.GREEN + ChatColor.BOLD + price + " GEM(s)" + ChatColor.GRAY + ".");
                        p.sendMessage(ChatColor.GRAY + "This item is non-refundable. type " + ChatColor.GREEN + ChatColor.BOLD + "Y" + ChatColor.GRAY + " to confirm.");
                        buyingitem.put(p.getName(), Horses.mount(newtier, false));
                        buyingprice.put(p.getName(), price);
                        p.closeInventory();
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "You do not have enough gems to purchase this mount.");
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "COST: " + ChatColor.RED + price + ChatColor.BOLD + "G");
                    p.closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getWhoClicked().getOpenInventory().getTopInventory().getTitle().contains("Horse")) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(EntityDamageEvent e) {
        if (e.getDamage() <= 0.0) {
            return;
        }
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL || e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
                if (p.isInsideVehicle() && p.getVehicle().getType() == EntityType.HORSE) {
                    e.setDamage(0.0);
                    e.setCancelled(true);
                }
            } else if (p.isInsideVehicle() && p.getVehicle().getType() == EntityType.HORSE) {
                p.getVehicle().remove();
                p.teleport(p.getVehicle().getLocation().add(0.0, 1.0, 0.0));
            }
        }
        if (e.getEntity() instanceof Horse) {
            Horse h = (Horse) e.getEntity();
            if (e.getCause() != EntityDamageEvent.DamageCause.FALL && e.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION) {
                EntityDamageByEntityEvent evt;
                Entity p = h.getPassenger();
                if (e instanceof EntityDamageByEntityEvent && (evt = (EntityDamageByEntityEvent) e).getDamager() instanceof Player && p instanceof Player) {
                    Player d = (Player) evt.getDamager();
                    ArrayList<String> toggles = Toggles.getToggles(d.getName());
                    ArrayList<String> buddies = Buddies.getBuddies(d.getName());
                    if (buddies.contains(((Player) p).getName().toLowerCase()) && !toggles.contains("ff")) {
                        e.setDamage(0.0);
                        e.setCancelled(true);
                        return;
                    }
                    if (toggles.contains("pvp")) {
                        e.setDamage(0.0);
                        e.setCancelled(true);
                        return;
                    }
                    if (!Alignments.neutral.containsKey(((Player) p).getName()) && !Alignments.chaotic.containsKey(((Player) p).getName()) && toggles.contains("chaos")) {
                        e.setDamage(0.0);
                        e.setCancelled(true);
                        return;
                    }
                }
                h.remove();
                if (p != null) {
                    p.teleport(h.getLocation().add(0.0, 1.0, 0.0));
                }
            }
            e.setDamage(0.0);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamager(EntityDamageByEntityEvent e) {
        Player p;
        if (e.getDamage() <= 0.0) {
            return;
        }
        if (e.getDamager() instanceof Player && (p = (Player) e.getDamager()).getVehicle() != null && p.getVehicle().getType() == EntityType.HORSE) {
            p.getVehicle().remove();
            p.teleport(p.getVehicle().getLocation().add(0.0, 1.0, 0.0));
        }
    }

    @EventHandler
    public void onDismount(VehicleExitEvent e) {
        if (e.getExited() instanceof Player && e.getVehicle() instanceof Horse) {
            e.getVehicle().remove();
        }
    }

    @EventHandler
    public void onMountSummon(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK || p.getInventory().getItemInMainHand() == null || Horses.getMountTier(p.getInventory().getItemInMainHand()) <= 0 || p.getVehicle() != null || mounting.containsKey(p.getName()))) {
            mounting.put(p.getName(), 5);
            mountingloc.put(p.getName(), p.getLocation());
            horsetier.put(p.getName(), Horses.getMountTier(p.getInventory().getItemInMainHand()));
        }
    }

    @EventHandler
    public void onCancelDamager(EntityDamageByEntityEvent e) {
        Player p;
        if (e.getDamager() instanceof Player && e.getEntity() instanceof LivingEntity && mounting.containsKey((p = (Player) e.getDamager()).getName())) {
            mounting.remove(p.getName());
            mountingloc.remove(p.getName());
            p.sendMessage(ChatColor.RED + "Mount Summon - " + ChatColor.BOLD + "CANCELLED");
        }
    }

    @EventHandler
    public void onCancelDamage(EntityDamageEvent e) {
        Player p;
        if (e.getDamage() <= 0.0) {
            return;
        }
        if (e.getEntity() instanceof Player && mounting.containsKey((p = (Player) e.getEntity()).getName())) {
            mounting.remove(p.getName());
            mountingloc.remove(p.getName());
            p.sendMessage(ChatColor.RED + "Mount Summon - " + ChatColor.BOLD + "CANCELLED");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (mounting.containsKey(p.getName())) {
            mounting.remove(p.getName());
            mountingloc.remove(p.getName());
        }
        if (p.getVehicle() != null && p.getVehicle().getType() == EntityType.HORSE) {
            p.getVehicle().remove();
            p.teleport(p.getVehicle().getLocation().add(0.0, 1.0, 0.0));
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        if (mounting.containsKey(p.getName())) {
            mounting.remove(p.getName());
            mountingloc.remove(p.getName());
        }
        p.eject();
    }

    @EventHandler
    public void onCancelMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (mounting.containsKey(p.getName()) && (mountingloc.get(p.getName())).distanceSquared(e.getTo()) >= 2.0) {
            mounting.remove(p.getName());
            p.sendMessage(ChatColor.RED + "Mount Summon - " + ChatColor.BOLD + "CANCELLED");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPromptChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (buyingitem.containsKey(p.getName()) && buyingprice.containsKey(p.getName())) {
            e.setCancelled(true);
            int price = buyingprice.get(p.getName());
            ItemStack is = buyingitem.get(p.getName());
            if (e.getMessage().equalsIgnoreCase("y")) {
                if (!Money.hasEnoughGems(p, price)) {
                    p.sendMessage(ChatColor.RED + "You do not have enough gems to purchase this mount.");
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "COST: " + ChatColor.RED + price + ChatColor.BOLD + "G");
                    buyingprice.remove(p.getName());
                    buyingitem.remove(p.getName());
                    return;
                }
                if (p.getInventory().contains(Material.SADDLE)) {
                    p.getInventory().remove(Material.SADDLE);
                }
                if (p.getInventory().firstEmpty() == -1) {
                    p.sendMessage(ChatColor.RED + "No space available in inventory. Type 'cancel' or clear some room.");
                    return;
                }
                Money.takeGems(p, price);
                p.getInventory().setItem(p.getInventory().firstEmpty(), is);
                p.sendMessage(ChatColor.RED + "-" + price + ChatColor.BOLD + "G");
                p.sendMessage(ChatColor.GREEN + "Transaction successful.");
                p.sendMessage(ChatColor.GRAY + "You are now the proud owner of a mount -- " + ChatColor.UNDERLINE + "to summon your new mount, simply right click with the saddle in your player's hand.");
                buyingprice.remove(p.getName());
                buyingitem.remove(p.getName());
            } else {
                p.sendMessage(ChatColor.RED + "Purchase - " + ChatColor.BOLD + "CANCELLED");
                buyingprice.remove(p.getName());
                buyingitem.remove(p.getName());
                return;
            }
        }
    }

}

