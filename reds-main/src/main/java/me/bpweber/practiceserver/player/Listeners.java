package me.bpweber.practiceserver.player;

import de.Herbystar.TTA.TTA_Methods;
import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Ban;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Setrank;
import me.bpweber.practiceserver.ModerationMechanics.Commands.ToggleTrail;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Vanish;
import me.bpweber.practiceserver.ModerationMechanics.ModerationMechanics;
import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.damage.Damage;
import me.bpweber.practiceserver.enchants.Enchants;
import me.bpweber.practiceserver.item.Journal;
import me.bpweber.practiceserver.mobs.Mobs;
import me.bpweber.practiceserver.player.Stats.StatsMain;
import me.bpweber.practiceserver.pvp.Alignments;
import me.bpweber.practiceserver.teleport.Hearthstone;
import me.bpweber.practiceserver.teleport.TeleportBooks;
import me.bpweber.practiceserver.utils.CheckIP;
import me.bpweber.practiceserver.utils.Particles;
import me.bpweber.practiceserver.utils.StringUtil;
import me.kayaba.guilds.api.basic.Guild;
import me.kayaba.guilds.enums.Permission;
import me.kayaba.guilds.manager.PlayerManager;
import me.konsolas.aac.AAC;
import me.konsolas.aac.api.HackType;
import me.konsolas.aac.api.PlayerViolationCommandEvent;
import me.konsolas.aac.api.PlayerViolationEvent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@SuppressWarnings("deprecation")
public class Listeners
        implements Listener {

    public static HashMap<UUID, Long> named = new HashMap<UUID, Long>();
    HashMap<String, Long> update = new HashMap<String, Long>();
    public static HashMap<String, Long> combat = new HashMap<String, Long>();
    public static HashMap<UUID, Long> mobd = new HashMap<UUID, Long>();
    public static ArrayList<Player> previo = new ArrayList<Player>();
    HashMap<UUID, Long> firedmg = new HashMap<UUID, Long>();


    public void onEnable() {
        PracticeServer.log.info("[Listeners] has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents(this, PracticeServer.plugin);
        new BukkitRunnable() {

            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (!Alignments.isSafeZone(p.getLocation())) continue;
                    p.setFoodLevel(20);
                    p.setSaturation(20.0f);
                }
            }
        }.runTaskTimerAsynchronously(PracticeServer.plugin, 200, 100);
        new BukkitRunnable() {

            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    refreshTabList(p);
                    Random r = new Random();
                    float y = r.nextFloat() - 0.2F;
                    float x = r.nextFloat() - 0.2F;
                    float z = r.nextFloat() - 0.2F;

                    if (!ModerationMechanics.isSub(p) || ToggleTrail.toggletrail.contains(p.getName().toLowerCase()))
                        continue;
                    if (Setrank.ranks.get(p.getName()).equalsIgnoreCase("sub")) {
                        Particles.VILLAGER_HAPPY.display(0.125f, 0.125f, 0.125f, 0.02f, 10, p.getLocation().add(x, y, z), 20.0);
                    }
                    if (Setrank.ranks.get(p.getName()).equalsIgnoreCase("sub+")) {
                        Particles.FLAME.display(0.0f, 0.0f, 0.0f, 0.02f, 10, p.getLocation().add(x, y, z), 20.0);
                    }
                    if (!Setrank.ranks.get(p.getName()).equalsIgnoreCase("sub++")) continue;
                    Particles.SPELL_WITCH.display(0.0f, 0.0f, 0.0f, 1.0f, 10, p.getLocation().add(x, y, z), 20.0);

                }
            }
        }.runTaskTimerAsynchronously(PracticeServer.plugin, 2, 2);
    }

    public void onDisable() {
        PracticeServer.log.info("[Listeners] has been disabled.");
    }

    public void refreshTabList(Player p) {
        int ping = TTA_Methods.getPing(p);
        {
            if (PlayerManager.getPlayer(p.getUniqueId()).hasGuild()) {
                Guild g = PlayerManager.getPlayer(p.getUniqueId()).getGuild();
                TTA_Methods.sendTablist(p, ChatColor.DARK_AQUA.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "Atherial Runes\n"
                                + ChatColor.GRAY + "     ============" + ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + " GUILDS " + ChatColor.GRAY + "============"
                                + ChatColor.DARK_AQUA + "\n\nGuild Name: " + ChatColor.GRAY + "\n" + g.getName() + "\n\n" +
                                ChatColor.DARK_AQUA + "Guild Leader: " + ChatColor.GRAY + "\n" + g.getLeader().getName() + "\n\n" +
                                ChatColor.DARK_AQUA + "Online Players: " + ChatColor.GRAY + "\n" + g.getOnlinePlayers().size() + " / " + g.getPlayers().size() + "\n\n" +
                                ChatColor.DARK_AQUA + "Guild Points: " + ChatColor.GRAY + "\n" + g.getPoints() + "\n\n" +
                                ChatColor.DARK_AQUA + "Guild Lives: " + ChatColor.GRAY + "\n" + g.getLives() + "\n" +
                                ChatColor.GRAY + "==============================\n",
                        ChatColor.GRAY + "   ============" + ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + " INFO " + ChatColor.GRAY + "============\n\n"
                                + ChatColor.DARK_AQUA + "Player Kills: \n" + ChatColor.GRAY + StatsMain.getPlayerKills(p.getUniqueId()) + "\n\n"
                                + ChatColor.DARK_AQUA + "Monster Kills: \n" + ChatColor.GRAY + StatsMain.getMonsterKills(p.getUniqueId()) + "\n\n"
                                + ChatColor.DARK_AQUA + "Alignment: \n" + StatsMain.getAlignment(p) + "\n" + StatsMain.getAlignTime(p) + "\n\n" +
                                ChatColor.DARK_AQUA + "Players: \n" + ChatColor.GRAY + Bukkit.getOnlinePlayers().size() + " / 100\n" +
                                ChatColor.GRAY + "==============================\n");
            } else {
                TTA_Methods.sendTablist(p, ChatColor.DARK_AQUA.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "Atherial Runes\n"
                                + ChatColor.GRAY + "       ============" + ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + " GUILDS " + ChatColor.GRAY + "============"
                                + ChatColor.DARK_AQUA + "\n\nGuild Name: " + ChatColor.GRAY + "\n" + "N/A" + "\n" +
                                ChatColor.GRAY + "==============================\n",
                        ChatColor.GRAY + "   ============" + ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + " INFO " + ChatColor.GRAY + "============\n\n"
                                + ChatColor.DARK_AQUA + "Player Kills: \n" + ChatColor.GRAY + StatsMain.getPlayerKills(p.getUniqueId()) + "\n\n"
                                + ChatColor.DARK_AQUA + "Monster Kills: \n" + ChatColor.GRAY + StatsMain.getMonsterKills(p.getUniqueId()) + "\n\n"
                                + ChatColor.DARK_AQUA + "Alignment: \n" + StatsMain.getAlignment(p) + "\n" + StatsMain.getAlignTime(p) + "\n\n" +
                                ChatColor.DARK_AQUA + "Players: \n" + ChatColor.GRAY + Bukkit.getOnlinePlayers().size() + " / 100\n" +
                                ChatColor.GRAY + "==============================\n");
            }

        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Horse && event.getCause() == DamageCause.FALL)
            event.setCancelled(true);

    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE && !e.getPlayer().getName().equalsIgnoreCase("RedsEmporium")) {
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onMiddleClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (player.isOp() && !event.getWhoClicked().getName().equalsIgnoreCase("RedsEmporium")) {
            if (event.getCurrentItem().getType() == Material.TRAPPED_CHEST || event.getCurrentItem().getType() == Material.TRIPWIRE_HOOK || event.getCurrentItem().getType() == Material.PAPER) {
                player.getInventory().clear();
                event.getCursor().setType(null);
                event.getCurrentItem().setType(null);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (ModerationMechanics.isStaff(p)) {
                        p.sendMessage(ChatColor.RED.toString() + "Staff Member " + player.getName() + " Was found with illegal items in their inventory.");
                    }
                }
            }
        }
    }

    //ANTI-VPN Check to make sure user is not on VPN bn
    @EventHandler
    public void onJoinVPNCHECK(PlayerJoinEvent e) {
        if (!e.getPlayer().isOp()) {
            Player p = e.getPlayer();
            String addr = p.getAddress().getHostName();
            new BukkitRunnable() {
                @Override
                public void run() {
                    boolean bool = CheckIP.calculate(addr);
                    if (bool == true) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                try {
                                    e.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&',
                                            "&6[&bAtherial-Catcher&6]\n &cYou may not log in with a VPN/Proxy! You have been banned!"));
                                } catch (Exception e) {
                                }
                            }

                        }.runTaskLater(PracticeServer.plugin, 40);
                    }
                }
            }.runTaskLaterAsynchronously(PracticeServer.plugin, 20);
        }
    }
    //ANTICHEAT

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getPlayer().getInventory().getItemInOffHand().getType() != Material.AIR) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerViolation(PlayerViolationEvent e) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                if (e.getHackType() != HackType.FLY && e.getHackType() != HackType.INTERACT && e.getHackType() != HackType.BADPACKETS) {
                    if (e.getViolations() > 100) {
                        if (!previo.contains(e.getPlayer())) {
                            StringUtil.sendCenteredMessage(p, ChatColor.GOLD + "[" + ChatColor.AQUA + "Atherial-Catcher" + ChatColor.GOLD + "]");
                            StringUtil.sendCenteredMessage(p, ChatColor.GOLD + "Hack Type: " + ChatColor.GRAY + e.getHackType().getName());
                            StringUtil.sendCenteredMessage(p, ChatColor.GOLD + "Player Suspected: " + ChatColor.GRAY + e.getPlayer().getName());
                            StringUtil.sendCenteredMessage(p, ChatColor.GOLD + "Violation Level: " + ChatColor.GRAY + e.getViolations());
                            StringUtil.sendCenteredMessage(p, ChatColor.GOLD + "Lag Info: " + ChatColor.GRAY + "(TPS: " + (int) TTA_Methods.getTPS(20) + ") (Ping: " + TTA_Methods.getPing(e.getPlayer()) + ")");
                            previo.add(e.getPlayer());
                            new BukkitRunnable() {
                                public void run() {
                                    previo.remove(e.getPlayer());
                                }

                            }.runTaskLaterAsynchronously(PracticeServer.plugin, 80);
                        }
                    }
                    if (e.getViolations() > 600 && TTA_Methods.getPing(e.getPlayer()) < 170) {
                        Alignments.tagged.remove(e.getPlayer());
                        e.getPlayer().kickPlayer(ChatColor.RED + "You have been kicked!\n" +
                                ChatColor.GRAY + "[" + ChatColor.AQUA + "Atherial-Catcher" + ChatColor.GRAY + "]\n" +
                                ChatColor.GOLD + "Hack Type: " + ChatColor.GRAY + e.getHackType().getName());
                        AAC.j.setViolationLevel(e.getPlayer(), e.getHackType(), 0);


                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerViolationCommand(PlayerViolationCommandEvent e) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp() && e.getHackType() != HackType.KILLAURA) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void NCPExempter(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            if (p.getInventory().getItemInMainHand().getType().name().contains("_SPADE") || p.getInventory().getItemInMainHand().getType().name().contains("_HOE")) {
                AAC.j.setViolationLevel(p, HackType.KILLAURA, 0);
                AAC.j.setViolationLevel(p, HackType.HEURISTICS, 0);
                NCPExemptionManager.exemptPermanently(p, CheckType.FIGHT_NOSWING);
                NCPExemptionManager.exemptPermanently(p, CheckType.FIGHT_REACH);
                NCPExemptionManager.exemptPermanently(p, CheckType.FIGHT_SPEED);
                NCPExemptionManager.exemptPermanently(p, CheckType.FIGHT_DIRECTION);
                NCPExemptionManager.exemptPermanently(p, CheckType.FIGHT_ANGLE);
            } else if (!p.getInventory().getItemInMainHand().getType().name().contains("_HOE") || !p.getInventory().getItemInMainHand().getType().name().contains("_SPADE") && NCPExemptionManager.isExempted(p, CheckType.FIGHT_SPEED) ||
                    NCPExemptionManager.isExempted(p, CheckType.FIGHT_REACH) || NCPExemptionManager.isExempted(p, CheckType.FIGHT_NOSWING)) {
                NCPExemptionManager.unexempt(p, CheckType.FIGHT_SPEED);
                NCPExemptionManager.unexempt(p, CheckType.FIGHT_REACH);
                NCPExemptionManager.unexempt(p, CheckType.FIGHT_NOSWING);
                NCPExemptionManager.unexempt(p, CheckType.FIGHT_DIRECTION);
                NCPExemptionManager.unexempt(p, CheckType.FIGHT_ANGLE);

            }
        }
    }

    //ANTICHEAT END
    @EventHandler
    public void onMOTD(ServerListPingEvent e) {
        String motd = ChatColor.AQUA.toString() + ChatColor.BOLD + "Atherial Runes";
        int i = 0;
        while (i < 30) {
            motd = String.valueOf(motd) + " ";
            ++i;
        }
        motd = String.valueOf(motd) + ChatColor.GRAY + "Patch " + PracticeServer.plugin.getDescription().getVersion();
        e.setMotd(motd);
        e.setMaxPlayers(100);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void a(ServerCommandEvent e) {
        if (e.getCommand().equalsIgnoreCase("save-all") || e.getCommand().equalsIgnoreCase("/save-all")) {
            e.setCommand("");
        }
    }

    @EventHandler
    public void onJoinBanned(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        if (Ban.banned.containsKey(p.getUniqueId())) {
            if (Ban.banned.get(p.getUniqueId()) == -1) {
                e.setKickMessage(ChatColor.RED + "Your account has been PERMANENTLY disabled." + "\n" + ChatColor.GRAY +
                        "For further information about this suspension, please contact a " + ChatColor.UNDERLINE + "Staff Member");
            } else {
                e.setKickMessage(ChatColor.RED + "Your account has been TEMPORARILY locked due to suspisious activity." + "\n" + ChatColor.GRAY +
                        "For further information about this suspension, please contact a " + ChatColor.UNDERLINE + "Staff Member");
            }
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            return;
        }
        if (Bukkit.getServer().getOnlinePlayers().size() >= 100) {
            if (ModerationMechanics.isSub(p) || p.isOp()) {
                e.allow();
            } else {
                e.setKickMessage(String.valueOf(ChatColor.RED.toString()) + "Atherial Runes is currently FULL." + "\n" +
                        ChatColor.GRAY.toString() + "You can subscribe at http://store.atherialrunes.net/ to get instant access.");
                e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                return;
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.setCollidable(false);


        HashMap<String, PermissionAttachment> attachments = new HashMap<String, PermissionAttachment>();

        PermissionAttachment attachment = p.addAttachment(PracticeServer.plugin);
        for (Permission permission : me.kayaba.guilds.enums.Permission.values()) {
            if (permission.name().contains("GUILDS_GUILD_")) {
                attachment.setPermission(permission.getPath(), true);
            }
        }

        // FIX SHIT
        p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(1024.0D);

        p.setLevel(100);
        p.setExp(1.0f);
        p.setHealthScale(20.0);
        p.setHealthScaled(true);
        p.getInventory().setHeldItemSlot(0);
        if (p.getInventory().getItem(0) != null && (p.getInventory().getItem(0).getType().name().contains("_AXE") || p.getInventory().getItem(0).getType().name().contains("_SWORD") || p.getInventory().getItem(0).getType().name().contains("_HOE") || p.getInventory().getItem(0).getType().name().contains("_SPADE"))) {
            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.5f);
        }
        int i = 0;
        while (i < 20) {
            p.sendMessage(" ");
            ++i;
        }
        StringUtil.sendCenteredMessage(p, ChatColor.WHITE.toString() + ChatColor.BOLD + "Atherial Runes Patch " + PracticeServer.plugin.getDescription().getVersion());
        StringUtil.sendCenteredMessage(p, ChatColor.GRAY + "http://store.atherialrunes.net/");
        p.sendMessage("");
        StringUtil.sendCenteredMessage(p, ChatColor.YELLOW + "You are on the " + ChatColor.BOLD + "US-1" + ChatColor.YELLOW + " shard.");
        StringUtil.sendCenteredMessage(p, ChatColor.GRAY.toString() + ChatColor.ITALIC + "To manage your gameplay settings, use " + ChatColor.YELLOW.toString() + ChatColor.UNDERLINE + "/toggles");
        if (ModerationMechanics.isSub(p)) {
            StringUtil.sendCenteredMessage(p, ChatColor.GRAY.toString() + ChatColor.ITALIC + "To toggle your subscriber trail, use " + ChatColor.YELLOW.toString() + ChatColor.UNDERLINE + "/toggletrail");
        }
        p.sendMessage("");
        e.setJoinMessage(null);
        if (!p.hasPlayedBefore()) {
            this.Kit(e.getPlayer());
            p.teleport(TeleportBooks.Cyrennica);
        }
        Listeners.hpCheck(p);
        if (p.isOp()) {
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl == p || pl.isOp()) continue;
                pl.hidePlayer(p);
            }
            if (!Vanish.vanished.contains(p.getName().toLowerCase())) {
                Vanish.vanished.add(p.getName().toLowerCase());
            }
            StringUtil.sendCenteredMessage(p, ChatColor.AQUA.toString() + ChatColor.BOLD + "GM INVISIBILITY (infinite)");
            StringUtil.sendCenteredMessage(p, ChatColor.GREEN + "You are now " + ChatColor.BOLD + "invisible.");
            p.setMaxHealth(9999.0);
            p.setHealth(9999.0);
        } else {
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl == p || !pl.isOp()) continue;
                p.hidePlayer(pl);
            }
        }
    }


    @EventHandler
    public void onLeave(PlayerKickEvent e) {
        e.setLeaveMessage(null);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        e.setQuitMessage(null);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Alignments.tagged.remove(p.getName());
        combat.remove(p.getName());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDeath(EntityDamageEvent e) {
        if (e.getEntity() instanceof LivingEntity) {
            LivingEntity s = (LivingEntity) e.getEntity();
            if (e.getDamage() >= s.getHealth()) {
                if (mobd.containsKey(s.getUniqueId())) {
                    mobd.remove(s.getUniqueId());
                }
                if (this.firedmg.containsKey(s.getUniqueId())) {
                    this.firedmg.remove(s.getUniqueId());
                }
                if (Mobs.sound.containsKey(s.getUniqueId())) {
                    Mobs.sound.remove(s.getUniqueId());
                }
                if (named.containsKey(s.getUniqueId())) {
                    named.remove(s.getUniqueId());
                }
            }
        }
    }

    @EventHandler
    public void onHealthBar(EntityDamageEvent e) {
        if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof Player) && e.getDamage() > 0.0) {
            LivingEntity s = (LivingEntity) e.getEntity();
            double max = s.getMaxHealth();
            double hp = s.getHealth() - e.getDamage();
            s.setCustomName(Mobs.generateOverheadBar(s, hp, max, Mobs.getMobTier(s), Mobs.isElite(s)));
            s.setCustomNameVisible(true);
            named.put(s.getUniqueId(), System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onPotDrink(PlayerInteractEvent e) {
        Player p;
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && (p = e.getPlayer()).getInventory().getItemInMainHand().getType() == Material.POTION && p.getInventory().getItemInMainHand() != null) {
            e.setCancelled(true);
            if (p.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                String l = ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getLore().get(0));
                l = l.split("HP")[0];
                int hp = 0;
                try {
                    hp = Integer.parseInt(l.split(" ")[4]);
                } catch (Exception ex) {
                    hp = 0;
                }
                if (hp > 0) {
                    p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0f, 1.0f);
                    p.getInventory().setItemInMainHand(null);
                    if (p.getHealth() + (double) hp > p.getMaxHealth()) {
                        p.sendMessage("               " + ChatColor.GREEN + ChatColor.BOLD + "+" + ChatColor.GREEN + hp + ChatColor.BOLD + " HP" + ChatColor.GRAY + " [" + (int) p.getMaxHealth() + "/" + (int) p.getMaxHealth() + "HP]");
                        p.setHealth(p.getMaxHealth());
                    } else {
                        p.sendMessage("               " + ChatColor.GREEN + ChatColor.BOLD + "+" + ChatColor.GREEN + hp + ChatColor.BOLD + " HP" + ChatColor.GRAY + " [" + (int) (p.getHealth() + (double) hp) + "/" + (int) p.getMaxHealth() + "HP]");
                        p.setHealth(p.getHealth() + (double) hp);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        if (e.toWeatherState()) {
            e.setCancelled(true);
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onBookOpen(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        PlayerInventory i = p.getInventory();
        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() == Material.WRITTEN_BOOK && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta bm = (BookMeta) book.getItemMeta();
            String s = ChatColor.DARK_GREEN.toString() + ChatColor.UNDERLINE + "Lawful";
            String desc = ChatColor.BLACK.toString() + ChatColor.ITALIC + "-30% Durability Arm/Wep on Death";
            if (Alignments.chaotic.containsKey(p.getName())) {
                s = ChatColor.DARK_RED.toString() + ChatColor.UNDERLINE + "Chaotic\n" + ChatColor.BLACK + ChatColor.BOLD + "Neutral" + ChatColor.BLACK + " in " + Alignments.chaotic.get(p.getName()) + "s";
                desc = ChatColor.BLACK.toString() + ChatColor.ITALIC + "Inventory LOST on Death";
            }
            if (Alignments.neutral.containsKey(p.getName())) {
                s = ChatColor.GOLD.toString() + ChatColor.UNDERLINE + "Neutral\n" + ChatColor.BLACK + ChatColor.BOLD + "Lawful" + ChatColor.BLACK + " in " + Alignments.neutral.get(p.getName()) + "s";
                desc = ChatColor.BLACK.toString() + ChatColor.ITALIC + "25%/50% Arm/Wep LOST on Death";
            }
            int dps = 0;
            int arm = 0;
            int amt = 5;
            int nrg = 100;
            int block = 0;
            int dodge = 0;
            int intel = 0;
            int str = 0;
            int vit = 0;
            int sword_dmg = 0;
            int axe_dmg = 0;
            int block_pcnt = 0;
            int health_pcnt = 0;
            int hps_pcnt = 0;
            int nrg_pcnt = 0;
            int crit_pcnt = 0;
            ItemStack[] arritemStack = i.getArmorContents();
            int n = arritemStack.length;
            int n2 = 0;
            while (n2 < n) {
                ItemStack is = arritemStack[n2];
                if (is != null && is.getType() != Material.AIR && is.hasItemMeta() && is.getItemMeta().hasLore()) {
                    int adddps = Damage.getDps(is);
                    dps += adddps;
                    int addarm = Damage.getArmor(is);
                    arm += addarm;
                    int added = Damage.getHps(is);
                    amt += added;
                    int addednrg = Damage.getEnergy(is);
                    nrg += addednrg;
                    int addeddodge = Damage.getPercent(is, "DODGE");
                    dodge += addeddodge;
                    int addedblock = Damage.getPercent(is, "BLOCK");
                    block += addedblock;
                    int addedint = Damage.getElem(is, "INT");
                    intel += addedint;
                    int addedstr = Damage.getElem(is, "STR");
                    str += addedstr;
                    int addedvit = Damage.getElem(is, "VIT");
                    vit += addedvit;
                }
                ++n2;
            }
            if (intel > 0) {
                nrg += Math.round(intel / 125);
                nrg_pcnt = (int) Math.round((double) intel * 0.009);
                crit_pcnt = (int) Math.round((double) intel * 0.011);
            }
            if (vit > 0) {
                sword_dmg = Math.round(vit / 50);
                health_pcnt = (int) Math.round((double) vit * 0.05);
                hps_pcnt = (int) Math.round((double) vit * 0.3);
                amt += hps_pcnt;
            }
            if (str > 0) {
                axe_dmg = Math.round(str / 50);
                block_pcnt = (int) Math.round((double) str * 0.015);
                block = (int) ((long) block + Math.round((double) str * 0.015));
            }
            bm.addPage(ChatColor.UNDERLINE.toString() + ChatColor.BOLD + "  Your Character  \n\n" + ChatColor.RESET + ChatColor.BOLD + "Alignment: " + s + "\n" + desc + "\n\n" + ChatColor.BLACK + "  " + (int) p.getHealth() + " / " + (int) p.getMaxHealth() + ChatColor.BOLD + " HP\n" + ChatColor.BLACK + "  " + arm + " - " + arm + "%" + ChatColor.BOLD + " Armor\n" + ChatColor.BLACK + "  " + dps + " - " + dps + "%" + ChatColor.BOLD + " DPS\n" + ChatColor.BLACK + "  " + amt + ChatColor.BOLD + " HP/s\n" + ChatColor.BLACK + "  " + nrg + "% " + ChatColor.BOLD + "Energy\n" + ChatColor.BLACK + "  " + dodge + "% " + ChatColor.BOLD + "Dodge\n" + ChatColor.BLACK + "  " + block + "% " + ChatColor.BOLD + "Block");
            bm.addPage(ChatColor.BLACK.toString() + ChatColor.BOLD + "+ " + str + " Strength\n" + "  " + ChatColor.BLACK + ChatColor.UNDERLINE + "'The Warrior'\n" + ChatColor.BLACK + "+" + block_pcnt + "% Block\n" + ChatColor.BLACK + "+" + axe_dmg + "% Axe DMG\n\n" + ChatColor.BLACK + ChatColor.BOLD + "+ " + vit + " Vitality\n" + "  " + ChatColor.BLACK + ChatColor.UNDERLINE + "'The Defender'\n" + ChatColor.BLACK + "+" + health_pcnt + "% Health\n" + ChatColor.BLACK + "+" + hps_pcnt + "   HP/s\n" + ChatColor.BLACK + "+" + sword_dmg + "% Sword DMG\n\n" + ChatColor.BLACK + ChatColor.BOLD + "+ " + intel + " Intellect\n" + "  " + ChatColor.BLACK + ChatColor.UNDERLINE + "'The Mage'\n" + ChatColor.BLACK + "+" + nrg_pcnt + "% Energy\n" + ChatColor.BLACK + "+" + crit_pcnt + "% Critical Hit\n\n");
            bm.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Character Journal");
            bm.setLore(Arrays.asList(ChatColor.GRAY + "A book that displays", ChatColor.GRAY + "your character's stats"));
            book.setItemMeta(bm);
            p.setItemInHand(book);
            p.updateInventory();
            p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0f, 1.25f);
            if (!this.update.containsKey(p.getName()) || System.currentTimeMillis() - this.update.get(p.getName()) > 2000) {
                p.closeInventory();
            }
            this.update.put(p.getName(), System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onCloseChest(InventoryCloseEvent e) {
        if (e.getInventory().getName().contains("Bank Chest") && e.getPlayer() instanceof Player) {
            Player p = (Player) e.getPlayer();
            p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1.0f, 1.0f);
        }
    }

    @EventHandler
    public void onArmourPutOn(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getInventory().getItemInMainHand() != null && (p.getInventory().getItemInMainHand().getType().name().contains("HELMET") || p.getInventory().getItemInMainHand().getType().name().contains("CHESTPLATE") || p.getInventory().getItemInMainHand().getType().name().contains("LEGGINGS") || p.getInventory().getItemInMainHand().getType().name().contains("BOOTS")) && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            e.setCancelled(true);
            p.updateInventory();
        }
    }

    @EventHandler
    public void onToggleSprint(PlayerToggleSprintEvent e) {
        if (Energy.getEnergy(e.getPlayer()) <= 0.0f) {
            e.setCancelled(true);
            e.getPlayer().setSprinting(false);
        }
    }

    @EventHandler
    public void onSprint(PlayerMoveEvent e) {
        if (Energy.getEnergy(e.getPlayer()) <= 0.0f) {
            e.getPlayer().setSprinting(false);
        }
    }

    @EventHandler
    public void onCombatTag(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            if (e.getDamage() <= 0.0) {
                return;
            }
            Player p = (Player) e.getDamager();
            combat.put(p.getName(), System.currentTimeMillis());
        }
    }//

    @EventHandler(priority = EventPriority.LOWEST)
    public void onNoAutoclick(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof LivingEntity && !(e.getDamager() instanceof Player)) {
            LivingEntity s = (LivingEntity) e.getDamager();
            if (!mobd.containsKey(s.getUniqueId()) || mobd.containsKey(s.getUniqueId()) && System.currentTimeMillis() - mobd.get(s.getUniqueId()) > 1000) {
                mobd.put(s.getUniqueId(), System.currentTimeMillis());
            } else if (!(e.getDamager() instanceof MagmaCube)) {
                e.setDamage(0.0);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onNoEnergyDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof LivingEntity && e.getDamager() instanceof Player && ((Player) e.getDamager()).getExp() <= 0.0f) {
            e.setDamage(0.0);
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onNoDamager(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof LivingEntity && e.getDamager() instanceof LivingEntity && Alignments.isSafeZone(e.getDamager().getLocation())) {
            e.setDamage(0.0);
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onNoDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof LivingEntity && Alignments.isSafeZone(e.getEntity().getLocation())) {
            e.setDamage(0.0);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onLoginShiny(PlayerJoinEvent e) {
        ItemStack is;
        Player p = e.getPlayer();
        ItemStack[] arritemStack = p.getInventory().getContents();
        int n = arritemStack.length;
        int n2 = 0;
        while (n2 < n) {
            is = arritemStack[n2];
            if (is != null && is.getType() != Material.AIR && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && Enchants.getPlus(is) > 3) {
                is.addUnsafeEnchantment(Enchants.glow, 1);
            }
            ++n2;
        }
        arritemStack = p.getInventory().getArmorContents();
        n = arritemStack.length;
        n2 = 0;
        while (n2 < n) {
            is = arritemStack[n2];
            if (is != null && is.getType() != Material.AIR && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && Enchants.getPlus(is) > 3) {
                is.addUnsafeEnchantment(Enchants.glow, 1);
            }
            ++n2;
        }
    }

    @EventHandler
    public void onOpenShinyShiny(InventoryOpenEvent e) {
        if (e.getInventory().getName().contains("Bank Chest")) {
            ItemStack[] arritemStack = e.getInventory().getContents();
            int n = arritemStack.length;
            int n2 = 0;
            while (n2 < n) {
                ItemStack is = arritemStack[n2];
                if (is != null && is.getType() != Material.AIR && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && Enchants.getPlus(is) > 3) {
                    is.addUnsafeEnchantment(Enchants.glow, 1);
                }
                ++n2;
            }
        }
    }

    @EventHandler
    public void onMapOpen(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && p.getInventory().getItemInMainHand().getType() == Material.EMPTY_MAP) {
            e.setCancelled(true);
            p.updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGemPickup(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        if (!e.isCancelled() && e.getItem().getItemStack().getType() == Material.EMERALD) {
            p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "                    +" + ChatColor.GREEN + e.getItem().getItemStack().getAmount() + ChatColor.GREEN + ChatColor.BOLD + "G");
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        }
    }

    public static boolean isItemTradeable(final ItemStack i) {
        if (i != null && i.hasItemMeta() && i.getItemMeta().hasLore()) {
            final List<String> lore = i.getItemMeta().getLore();
            for (final String s : lore) {
                if (ChatColor.stripColor(s).toLowerCase().equalsIgnoreCase("untradeable") || ChatColor.stripColor(s).toLowerCase().equalsIgnoreCase("permanent untradeable")) {
                    return false;
                }
            }
        }
        return true;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDamagePercent(EntityDamageEvent e) {
        if (e.getEntity() instanceof LivingEntity) {
            if (e.getDamage() <= 0.0) {
                return;
            }
            LivingEntity p = (LivingEntity) e.getEntity();
            if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || e.getCause().equals(EntityDamageEvent.DamageCause.LAVA) || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
                if (!this.firedmg.containsKey(p.getUniqueId()) || this.firedmg.containsKey(p.getUniqueId()) && System.currentTimeMillis() - this.firedmg.get(p.getUniqueId()) > 500) {
                    this.firedmg.put(p.getUniqueId(), System.currentTimeMillis());
                    double multiplier = 0.01;
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || e.getCause().equals(EntityDamageEvent.DamageCause.LAVA)) {
                        multiplier = 0.03;
                    }
                    if (p.getMaxHealth() * multiplier < 1.0) {
                        e.setDamage(1.0);
                    } else {
                        e.setDamage(p.getMaxHealth() * multiplier);
                    }
                } else {
                    e.setDamage(0.0);
                    e.setCancelled(true);
                }
            } else if (e.getCause().equals(EntityDamageEvent.DamageCause.POISON)) {
                if (p.getMaxHealth() * 0.01 >= p.getHealth()) {
                    e.setDamage(p.getHealth() - 1.0);
                } else if (p.getMaxHealth() * 0.01 < 1.0) {
                    e.setDamage(1.0);
                } else {
                    e.setDamage(p.getMaxHealth() * 0.01);
                }
            } else if (e.getCause().equals(EntityDamageEvent.DamageCause.DROWNING)) {
                if (p.getMaxHealth() * 0.04 < 1.0) {
                    e.setDamage(1.0);
                } else {
                    e.setDamage(p.getMaxHealth() * 0.04);
                }
            } else if (e.getCause().equals(EntityDamageEvent.DamageCause.WITHER)) {
                e.setCancelled(true);
                e.setDamage(0.0);
                if (p.hasPotionEffect(PotionEffectType.WITHER)) {
                    p.removePotionEffect(PotionEffectType.WITHER);
                }
            } else if (e.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)) {
                e.setDamage(0.0);
                e.setCancelled(true);
                Location loc = p.getLocation();
                while ((loc.getBlock().getType() != Material.AIR || loc.add(0.0, 1.0, 0.0).getBlock().getType() != Material.AIR) && loc.getY() < 255.0) {
                    loc.add(0.0, 1.0, 0.0);
                }
                p.teleport(loc);
            } else if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
                e.setDamage(0.0);
                e.setCancelled(true);
                if (p instanceof Player) {
                    Player pl = (Player) p;
                    if (Alignments.chaotic.containsKey(pl.getName())) {
                        p.teleport(TeleportBooks.generateRandomSpawnPoint(pl.getName()));
                    } else {
                        p.teleport(TeleportBooks.Cyrennica);
                    }
                }
            } else if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                if (e.getDamage()
                        * p.getMaxHealth() * 0.02 >= p.getHealth()) {
                    e.setDamage(p.getHealth() - 1.0);
                } else if (e.getDamage() * p.getMaxHealth() * 0.02 < 1.0) {
                    e.setDamage(1.0);
                } else {
                    e.setDamage(e.getDamage() * p.getMaxHealth() * 0.02);
                }
            } else if (e.getCause().equals(EntityDamageEvent.DamageCause.CONTACT)) {
                if (!this.firedmg.containsKey(p.getUniqueId()) || this.firedmg.containsKey(p.getUniqueId()) && System.currentTimeMillis() - this.firedmg.get(p.getUniqueId()) > 500) {
                    this.firedmg.put(p.getUniqueId(), System.currentTimeMillis());
                    e.setDamage(1.0);
                } else {
                    e.setDamage(0.0);
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onHungerLoss(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (Alignments.isSafeZone(p.getLocation())) {
                p.setFoodLevel(20);
                p.setSaturation(20.0f);
                e.setCancelled(true);
            } else if (e.getFoodLevel() < p.getFoodLevel() && ((new Random()).nextInt(5)) != 0) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);
        e.setDroppedExp(0);
        e.setDeathMessage(null);
        Alignments.tagged.remove(p.getName());
        combat.remove(p.getName());
    }

    public static void hpCheck(Player p) {
        if (p.isOp()) {
            return;
        }
        PlayerInventory i = p.getInventory();
        double a = 50.0;
        double vital = 0.0;
        ItemStack[] arritemStack = i.getArmorContents();
        int n = arritemStack.length;
        int n2 = 0;
        while (n2 < n) {
            ItemStack is = arritemStack[n2];
            if (is != null && is.getType() != Material.AIR && is.hasItemMeta() && is.getItemMeta().hasLore()) {
                double health = Damage.getHp(is);
                int vit = Damage.getElem(is, "VIT");
                a += health;
                vital += (double) vit;
            }
            ++n2;
        }
        if (vital > 0.0) {
            double mod = vital * 0.05;
            a += a * (mod / 100.0);
            p.setMaxHealth((double) ((int) a));
        } else {
            p.setMaxHealth(a);
        }
        p.setHealthScale(20.0);
        p.setHealthScaled(true);
    }

    boolean isArmor(ItemStack is) {
        if (is != null) {
            if (is.getType().name().contains("_HELMET")) {
                return true;
            }
            if (is.getType().name().contains("_CHESTPLATE")) {
                return true;
            }
            if (is.getType().name().contains("_LEGGINGS")) {
                return true;
            }
            if (is.getType().name().contains("_BOOTS")) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getSlotType() == InventoryType.SlotType.ARMOR && (e.isLeftClick() || e.isRightClick() || e.isShiftClick()) && (this.isArmor(e.getCurrentItem()) && this.isArmor(e.getCursor()) || this.isArmor(e.getCurrentItem()) && (e.getCursor() == null || e.getCursor().getType() == Material.AIR) || (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) && this.isArmor(e.getCursor()))) {
            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
        }
        if (e.getInventory().getHolder() == p) {
            if (e.isShiftClick() && e.getCurrentItem().getType().name().contains("_HELMET") && p.getInventory().getHelmet() == null) {
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
            }
            if (e.isShiftClick() && e.getCurrentItem().getType().name().contains("_CHESTPLATE") && p.getInventory().getChestplate() == null) {
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
            }
            if (e.isShiftClick() && e.getCurrentItem().getType().name().contains("_LEGGINGS") && p.getInventory().getLeggings() == null) {
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
            }
            if (e.isShiftClick() && e.getCurrentItem().getType().name().contains("_BOOTS") && p.getInventory().getBoots() == null) {
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
            }
        }
        new BukkitRunnable() {

            public void run() {
                Listeners.hpCheck(p);
            }
        }.runTaskLaterAsynchronously(PracticeServer.plugin, 1);
    }

    @EventHandler
    public void onWeaponSwitch(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        ItemStack newItem = p.getInventory().getItem(e.getNewSlot());
        if (newItem != null && (newItem.getType().name().contains("_SWORD") || newItem.getType().name().contains("_AXE") || newItem.getType().name().contains("_HOE") || newItem.getType().name().contains("_SPADE"))) {
            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.5f);
        }
    }

    @EventHandler
    public void onTag(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getDamage() <= 0.0) {
                return;
            }
            Player p = (Player) e.getEntity();
            if (e.getCause() != EntityDamageEvent.DamageCause.FALL) {
                Alignments.tagged.put(p.getName(), System.currentTimeMillis());
            }
        }
    }

    @EventHandler
    public void onHitTag(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof LivingEntity) {
            if (e.getDamage() <= 0.0) {
                return;
            }
            Player p = (Player) e.getDamager();
            Alignments.tagged.put(p.getName(), System.currentTimeMillis());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKickLog(PlayerKickEvent e) {
        Player p = e.getPlayer();
        if (!Alignments.isSafeZone(p.getLocation()) && Alignments.tagged.containsKey(p.getName()) && System.currentTimeMillis() - Alignments.tagged.get(p.getName()) < 10000) {
            p.setHealth(0.0);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuitLog(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (!Alignments.isSafeZone(p.getLocation()) && Alignments.tagged.containsKey(p.getName()) && System.currentTimeMillis() - Alignments.tagged.get(p.getName()) < 10000) {
            p.setHealth(0.0);
        }
    }

    @EventHandler
    public void onHealthRegen(EntityRegainHealthEvent e) {
        e.setCancelled(true);
    }

    public void Kit(Player p) {
        ItemStack S;
        ItemMeta smeta;
        ArrayList<String> slore;
        Random random = new Random();
        int wep = random.nextInt(2) + 1;
        if (wep == 1) {
            S = new ItemStack(Material.WOOD_SWORD);
            smeta = S.getItemMeta();
            smeta.setDisplayName(ChatColor.WHITE + "Training Sword");
            slore = new ArrayList<String>();
            slore.add(ChatColor.RED + "DMG: 3 - 4");
            slore.add(ChatColor.GRAY + "Untradeable");
            smeta.setLore(slore);
            S.setItemMeta(smeta);
            p.getInventory().addItem(S);
        }
        if (wep == 2) {
            S = new ItemStack(Material.WOOD_AXE);
            smeta = S.getItemMeta();
            smeta.setDisplayName(ChatColor.WHITE + "Training Hatchet");
            slore = new ArrayList<String>();
            slore.add(ChatColor.RED + "DMG: 2 - 5");
            slore.add(ChatColor.GRAY + "Untradeable");
            smeta.setLore(slore);
            S.setItemMeta(smeta);
            p.getInventory().addItem(S);
        }
        int t = 0;
        while (t < 3) {
            p.getInventory().addItem(this.createPotion());
            ++t;
        }
        ItemStack bread = new ItemStack(Material.BREAD);
        ItemMeta breadmeta = bread.getItemMeta();
        breadmeta.setLore(Arrays.asList(ChatColor.GRAY + "Untradeable"));
        bread.setItemMeta(breadmeta);
        int t2 = 0;
        while (t2 < 2) {
            p.getInventory().setItem(p.getInventory().firstEmpty(), bread);
            ++t2;
        }
        p.getInventory().addItem(Hearthstone.hearthstone());
        p.getInventory().addItem(Journal.journal());
        p.setMaxHealth(50.0);
        p.setHealth(50.0);
        p.setHealthScale(20.0);
        p.setHealthScaled(true);
    }

    private ItemStack createPotion() {
        Potion potion = new Potion(PotionType.REGEN);
        ItemStack itemStack = potion.toItemStack(1);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.setDisplayName(ChatColor.WHITE + "Minor Health Potion");
        potionMeta.setLore(Arrays.asList(ChatColor.GRAY + "A potion that restores " + ChatColor.WHITE + "15HP", ChatColor.GRAY + "Untradeable"));
        for (ItemFlag itemFlag : ItemFlag.values()) {
            potionMeta.addItemFlags(itemFlag);
        }
        itemStack.setItemMeta(potionMeta);
        return itemStack;
    }

}