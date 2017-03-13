package me.bpweber.practiceserver.drops;

import me.bpweber.practiceserver.Crates.CratesMain;
import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.money.GemPouches;
import me.bpweber.practiceserver.money.Money;
import me.bpweber.practiceserver.teleport.TeleportBooks;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;


public class Mobdrops implements Listener {

    public void onEnable() {
        PracticeServer.log.info("[MobDrops] has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents(this, PracticeServer.plugin);
    }

    public void onDisable() {
        PracticeServer.log.info("[MobDrops] has been disabled.");
    }

    @EventHandler
    public void onMobDeath(final EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            e.getDrops().clear();
        }
        e.setDroppedExp(0);
    }

    @EventHandler
    public void onMobDeath(final EntityDamageEvent e) {
        if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof Player)) {
            final LivingEntity s = (LivingEntity) e.getEntity();
            if (e.getDamage() >= s.getHealth()
                    && s.getEquipment().getItemInMainHand() != null
                    && s.getEquipment().getItemInMainHand().getType() != Material.AIR) {
                s.playEffect(EntityEffect.DEATH);
                s.remove();
                final Random random = new Random();
                final int gems = random.nextInt(2) + 1;
                int gemamt = 0;
                final int scrolldrop = random.nextInt(100);
                final int sackdrop = random.nextInt(100);
                boolean dodrop = false;
                boolean elite = false;
                final int rd = random.nextInt(100);
                final int cratedrop = random.nextInt(100);
                final int tsix = random.nextInt(150);
                if (s.getEquipment().getItemInMainHand().getItemMeta().hasEnchants()) {
                    elite = true;
                }
                if (s.getEquipment().getItemInMainHand().getType().name().contains("WOOD_")) {
                    gemamt = random.nextInt(3) + 3;
                    if (elite && !this.isCustomNamedElite(s)) {
                        if (rd < 40) {
                            dodrop = true;
                        }
                    } else if (elite && this.isCustomNamedElite(s)) {
                        if (rd < 35) {
                            dodrop = true;
                        }
                    } else if (rd < 23) {
                        dodrop = true;
                    }
                    if (scrolldrop <= 5) {
                        final int scrolltype = random.nextInt(2);
                        if (scrolltype == 0) {
                            s.getWorld().dropItemNaturally(s.getLocation(), TeleportBooks.cyrennica_book(false));
                        }
                        if (scrolltype == 1) {
                            s.getWorld().dropItemNaturally(s.getLocation(), TeleportBooks.harrison_book(false));
                        }
                    }
                    if (sackdrop <= 5) {
                        s.getWorld().dropItemNaturally(s.getLocation(), GemPouches.gemPouch(1));
                    }
                    if (cratedrop <= 5) {
                        s.getWorld().dropItemNaturally(s.getLocation(), CratesMain.createCrate(1));
                    }

                }
                if (s.getEquipment().getItemInMainHand().getType().name().contains("STONE_")) {
                    gemamt = random.nextInt(5) + 5;
                    if (elite && !this.isCustomNamedElite(s)) {
                        if (rd < 30) {
                            dodrop = true;
                        }
                    } else if (elite && this.isCustomNamedElite(s)) {
                        if (rd < 24) {
                            dodrop = true;
                        }
                    } else if (rd < 20) {
                        dodrop = true;
                    }
                    if (scrolldrop <= 5) {
                        final int scrolltype = random.nextInt(5);
                        if (scrolltype == 0) {
                            s.getWorld().dropItemNaturally(s.getLocation(), TeleportBooks.cyrennica_book(false));
                        }
                        if (scrolltype == 1) {
                            s.getWorld().dropItemNaturally(s.getLocation(), TeleportBooks.harrison_book(false));
                        }
                        if (scrolltype == 2) {
                            s.getWorld().dropItemNaturally(s.getLocation(), TeleportBooks.dark_oak_book(false));
                        }
                        if (scrolltype == 3) {
                            s.getWorld().dropItemNaturally(s.getLocation(), TeleportBooks.trollsbane_book(false));
                        }
                        if (scrolltype == 4) {
                            s.getWorld().dropItemNaturally(s.getLocation(), TeleportBooks.tripoli_book(false));
                        }
                    }
                    if (sackdrop <= 3) {
                        s.getWorld().dropItemNaturally(s.getLocation(), GemPouches.gemPouch(2));
                    }
                    if (cratedrop <= 5) {
                        s.getWorld().dropItemNaturally(s.getLocation(), CratesMain.createCrate(2));
                    }
                }
                if (s.getEquipment().getItemInMainHand().getType().name().contains("IRON_")) {
                    gemamt = random.nextInt(10) + 10;
                    if (elite && !this.isCustomNamedElite(s)) {
                        if (rd < 22) {
                            dodrop = true;
                        }
                    } else if (elite && this.isCustomNamedElite(s)) {
                        if (rd < 18) {
                            dodrop = true;
                        }
                    } else if (rd < 18) {
                        dodrop = true;
                    }
                    if (scrolldrop <= 5) {
                        final int scrolltype = random.nextInt(5);
                        if (scrolltype == 0) {
                            s.getWorld().dropItemNaturally(s.getLocation(), TeleportBooks.cyrennica_book(false));
                        }
                        if (scrolltype == 1) {
                            s.getWorld().dropItemNaturally(s.getLocation(), TeleportBooks.dark_oak_book(false));
                        }
                        if (scrolltype == 2) {
                            s.getWorld().dropItemNaturally(s.getLocation(), TeleportBooks.trollsbane_book(false));
                        }
                        if (scrolltype == 3) {
                            s.getWorld().dropItemNaturally(s.getLocation(), TeleportBooks.gloomy_book(false));
                        }
                        if (scrolltype == 4) {
                            s.getWorld().dropItemNaturally(s.getLocation(), TeleportBooks.crestguard_book(false));
                        }
                    }
                    if (sackdrop <= 5) {
                        s.getWorld().dropItemNaturally(s.getLocation(), GemPouches.gemPouch(3));
                    }
                    if (cratedrop <= 5) {
                        s.getWorld().dropItemNaturally(s.getLocation(), CratesMain.createCrate(3));
                    }
                }
                if (s.getEquipment().getItemInMainHand().getType().name().contains("DIAMOND_")) {
                    gemamt = random.nextInt(30) + 24;
                    if (elite && !this.isCustomNamedElite(s)) {
                        if (rd < 14) {
                            dodrop = true;
                        }
                    } else if (elite && this.isCustomNamedElite(s)) {
                        if (rd < 12) {
                            dodrop = true;
                        }
                    } else if (rd < 9) {
                        dodrop = true;
                    }
                    if (scrolldrop <= 9) {
                        final int scrolltype = random.nextInt(4);
                        if (scrolltype == 0) {
                            s.getWorld().dropItemNaturally(s.getLocation(), TeleportBooks.deadpeaks_book(false));
                        }
                        if (scrolltype == 1) {
                            s.getWorld().dropItemNaturally(s.getLocation(), TeleportBooks.gloomy_book(false));
                        }
                        if (scrolltype == 2) {
                            s.getWorld().dropItemNaturally(s.getLocation(), TeleportBooks.crestwatch_book(false));
                        }
                        if (scrolltype == 3) {
                            s.getWorld().dropItemNaturally(s.getLocation(), TeleportBooks.crestguard_book(false));
                        }
                    }
                    if (sackdrop <= 5) {
                        s.getWorld().dropItemNaturally(s.getLocation(), GemPouches.gemPouch(4));
                    }
                    if (cratedrop <= 5) {
                        s.getWorld().dropItemNaturally(s.getLocation(), CratesMain.createCrate(4));
                    }
                }
                if (s.getEquipment().getItemInMainHand().getType().name().contains("GOLD_")) {
                    gemamt = random.nextInt(50) + 14;
                    if (elite && !this.isCustomNamedElite(s)) {
                        if (rd < 11) {
                            dodrop = true;
                        }
                    } else if (elite && this.isCustomNamedElite(s)) {
                        if (rd < 9) {
                            dodrop = true;
                        }
                    } else if (rd < 6) {
                        dodrop = true;
                    }
                    if (scrolldrop <= 5) {
                        final int scrolltype = random.nextInt(4);
                        if (scrolltype == 0) {
                            s.getWorld().dropItemNaturally(s.getLocation(), TeleportBooks.deadpeaks_book(false));
                        }
                        if (scrolltype == 1) {
                            s.getWorld().dropItemNaturally(s.getLocation(), TeleportBooks.gloomy_book(false));
                        }
                        if (scrolltype == 2) {
                            s.getWorld().dropItemNaturally(s.getLocation(), TeleportBooks.trollsbane_book(false));
                        }
                        if (scrolltype == 3) {
                            s.getWorld().dropItemNaturally(s.getLocation(), TeleportBooks.crestguard_book(false));
                        }
                    }
                    if (sackdrop <= 5) {
                        s.getWorld().dropItemNaturally(s.getLocation(), GemPouches.gemPouch(4));
                    }
                    if (cratedrop <= 5) {
                        s.getWorld().dropItemNaturally(s.getLocation(), CratesMain.createCrate(5));
                    }
                    if (tsix <= 2 && elite) {
                        s.getWorld().dropItemNaturally(s.getLocation(), GemPouches.gemPouch(6));
                    }
                }
                if (gems == 1) {
                    if (gemamt > 0) {
                        ItemStack itemStack = Money.makeGems(1);
                        
                        itemStack.setAmount(gemamt);
                        // Prevent potential gems stuck in ground, higher the Y axis by 1
                        s.getWorld().dropItemNaturally(s.getLocation().add(0, 1, 0), itemStack);
                        s.getWorld().dropItemNaturally(s.getLocation().add(0, 1, 0), itemStack);
                        s.getWorld().dropItemNaturally(s.getLocation().add(0, 1, 0), itemStack);
                        
                    }
                }
                if (dodrop) {
                    if (!this.isCustomNamedElite(s)) {
                        final ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
                        ItemStack[] armorContents;
                        for (int length = (armorContents = s.getEquipment()
                                .getArmorContents()).length, i = 0; i < length; ++i) {
                            final ItemStack is = armorContents[i];
                            if (is != null && is.getType() != Material.AIR && is.hasItemMeta()
                                    && is.getItemMeta().hasLore()) {
                                drops.add(is);
                                drops.add(s.getEquipment().getItemInMainHand());
                            }
                        }
                        final int piece = random.nextInt(drops.size());
                        final ItemStack is2 = drops.get(piece);
                        if (is2.getItemMeta().hasEnchants()
                                && is2.getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_MOBS)) {
                            is2.removeEnchantment(Enchantment.LOOT_BONUS_MOBS);
                        }
                        short dura = (short) random.nextInt(is2.getType().getMaxDurability());
                        if (dura == 0) {
                            dura = 1;
                        }
                        if (dura == is2.getType().getMaxDurability()) {
                            dura = (short) (is2.getType().getMaxDurability() - 1);
                        }
                        is2.setDurability(dura);
                        s.getWorld().dropItemNaturally(s.getLocation(), is2);
                    } else if (s.hasMetadata("type")) {
                        final String type = s.getMetadata("type").get(0).asString();
                        if (type.equalsIgnoreCase("mitsuki") || type.equalsIgnoreCase("copjak")
                                || type.equalsIgnoreCase("kingofgreed") || type.equalsIgnoreCase("skeletonking")
                                || type.equalsIgnoreCase("impa") || type.equalsIgnoreCase("bloodbutcher")
                                || type.equalsIgnoreCase("blayshan") || type.equalsIgnoreCase("jayden") || type.equalsIgnoreCase("kilatan")) {
                            final ItemStack is = EliteDrops.createCustomEliteDrop(type);
                            s.getWorld().dropItemNaturally(s.getLocation(), is);
                        }
                    }
                }
            }
        }
    }

    boolean isCustomNamedElite(final LivingEntity l) {
        if (l.hasMetadata("type")) {
            final String type = l.getMetadata("type").get(0).asString();
            if (type.equalsIgnoreCase("mitsuki") || type.equalsIgnoreCase("copjak") || type.equalsIgnoreCase("impa")
                    || type.equalsIgnoreCase("skeletonking") || type.equalsIgnoreCase("kingofgreed")
                    || type.equalsIgnoreCase("blayshan") || type.equalsIgnoreCase("bloodbutcher")
                    || type.equalsIgnoreCase("jayden") || type.equalsIgnoreCase("kilatan")) {
                return true;
            }
        }
        return false;
    }
}
