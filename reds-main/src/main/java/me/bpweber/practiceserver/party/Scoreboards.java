/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.scoreboard.DisplaySlot
 *  org.bukkit.scoreboard.Objective
 *  org.bukkit.scoreboard.Score
 *  org.bukkit.scoreboard.Scoreboard
 *  org.bukkit.scoreboard.Team
 */
package me.bpweber.practiceserver.party;

import me.bpweber.practiceserver.pvp.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.scoreboard.*;

import java.util.*;

public class Scoreboards {
    public static HashMap<Player, Scoreboard> boards = new HashMap<Player, Scoreboard>();

    public static Scoreboard getBoard(Player p) {
        if (!boards.containsKey(p)) {
            Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
            Team red = sb.registerNewTeam("red");
            red.setPrefix(ChatColor.RED.toString());
            Team yellow = sb.registerNewTeam("yellow");
            yellow.setPrefix(ChatColor.YELLOW.toString());
            Team white = sb.registerNewTeam("white");
            white.setPrefix(ChatColor.WHITE.toString());
            Team gm = sb.registerNewTeam("gm");
            gm.setPrefix(String.valueOf(ChatColor.AQUA.toString()) + ChatColor.BOLD + "GM " + ChatColor.AQUA);
            Team dev = sb.registerNewTeam("dev");
            dev.setPrefix(String.valueOf(ChatColor.GOLD.toString()) + ChatColor.BOLD + "DEV " + ChatColor.GOLD);
            Objective o = sb.registerNewObjective("showHealth", "health");
            o.setDisplaySlot(DisplaySlot.BELOW_NAME);
            o.setDisplayName(ChatColor.RED + "\u2764");
            boards.put(p, sb);
            return sb;
        }
        return boards.get(p);
    }

    @SuppressWarnings("deprecation")
    public static void updateAllColors() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Scoreboard sb = Scoreboards.getBoard(p);
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.getName().equalsIgnoreCase("RedsEmporium")) {
                    sb.getTeam("dev").addPlayer(pl);
                    continue;
                }
                if (pl.isOp()) {
                    sb.getTeam("gm").addPlayer(pl);
                    continue;
                }
                if (Alignments.neutral.containsKey(pl.getName())) {
                    sb.getTeam("yellow").addPlayer(pl);
                    continue;
                }
                if (Alignments.chaotic.containsKey(pl.getName())) {
                    sb.getTeam("red").addPlayer(pl);
                    continue;
                }
                sb.getTeam("white").addPlayer(pl);
            }
            p.setScoreboard(sb);
            boards.put(p, sb);
        }
    }

    @SuppressWarnings("deprecation")
    public static void updatePlayerHealth() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Scoreboard sb = Scoreboards.getBoard(p);
            for (Player pl : Bukkit.getOnlinePlayers()) {
                Objective o = sb.getObjective(DisplaySlot.BELOW_NAME);
                o.getScore(pl).setScore((int) pl.getHealth());
            }
            p.setScoreboard(sb);
            boards.put(p, sb);
        }
    }
}

