package me.bpweber.practiceserver.ModerationMechanics;

import me.bpweber.practiceserver.ModerationMechanics.Commands.Ban;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Mute;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Setrank;
import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.player.GamePlayer.GameConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ModerationMechanics implements Listener {
    public Setrank setrank;

    public void onEnable() {
        this.loadranks();
        this.loadBans();
        Bukkit.getServer().getPluginManager().registerEvents(this, PracticeServer.plugin);
        PracticeServer.log.info("[ModerationMechanics] has been enabled.");
        new BukkitRunnable() {

            public void run() {
                for (UUID s2 : Mute.muted.keySet()) {
                    if (s2 == null)
                        continue;
                    if (Mute.muted.get(s2) < 0) {
                        Player p;
                        Mute.muted.remove(s2);
                        if (Bukkit.getPlayer(s2) == null || !(p = Bukkit.getPlayer(s2)).isOnline())
                            continue;
                        p.sendMessage(ChatColor.GREEN + "Your " + ChatColor.BOLD + "GLOBAL MUTE" + ChatColor.GREEN
                                + " has expired.");
                        continue;
                    }
                    Mute.muted.put(s2, Mute.muted.get(s2) - 1);
                }
                for (UUID s2 : Ban.banned.keySet()) {
                    if (s2 == null)
                        continue;
                    if (Ban.banned.get(s2) < 0) {
                        Ban.banned.put(s2, -1);
                        continue;
                    }
                    if (Ban.banned.get(s2) == 0) {
                        Ban.banned.remove(s2);
                        continue;
                    }
                    Ban.banned.put(s2, Ban.banned.get(s2) - 1);
                }
            }
        }.runTaskTimerAsynchronously(PracticeServer.plugin, 20, 20);
    }

    public void onDisable() {
        PracticeServer.log.info("[ModerationMechanics] has been disabled.");
        for (UUID s2 : Ban.banned.keySet()) {
            GameConfig.get().set(s2.toString() + ".Main.Banned", Ban.banned.get(s2));
            GameConfig.save();
        }
        for (UUID s2 : Mute.muted.keySet()) {
            GameConfig.get().set(s2.toString() + ".Main.Muted", Mute.muted.get(s2));
            GameConfig.save();
        }
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.saveranks();
    }

    public static boolean isSub(Player p) {
        String rank;
        return Setrank.ranks.containsKey(p.getUniqueId()) && ((rank = Setrank.ranks.get(p.getUniqueId())).equalsIgnoreCase("sub")
                || rank.equalsIgnoreCase("sub+") || rank.equalsIgnoreCase("sub++") || p.isOp());
    }

    public static boolean isStaff(Player p) {

        return Setrank.ranks.containsKey(p.getUniqueId()) && (Setrank.ranks.get(p.getUniqueId())).equalsIgnoreCase("PMOD")
                || p.isOp();
    }

    void loadranks() {
        for (String ke : GameConfig.get().getKeys(false)) {
            UUID key = UUID.fromString(ke);
            String p = GameConfig.get().getString(ke + ".Main.Rank");
            Setrank.ranks.put(key, p);
            System.out.print(key);
        }
    }

    void loadBans() {
        for (String ke : GameConfig.get().getKeys(false)) {
            UUID key = UUID.fromString(ke);
            int p = GameConfig.get().getInt(ke + ".Main.Banned");
            Ban.banned.put(key, p);
        }
    }




    void saveranks() {
        for (UUID s : Setrank.ranks.keySet()) {
            GameConfig.get().set(s.toString() + ".Main.Rank", Setrank.ranks.get(s));
            GameConfig.save();
        }
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}