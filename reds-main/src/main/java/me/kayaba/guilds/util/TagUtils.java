package me.kayaba.guilds.util;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.preparedtag.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.entity.*;
import org.bukkit.scoreboard.*;

public final class TagUtils {
    private TagUtils() {
    }


    @SuppressWarnings("deprecation")
    public static void refresh(Player p) {
        if (!Config.TAGAPI_ENABLED.getBoolean()) {
            return;
        }

        Scoreboard board = p.getScoreboard();
        for (Player player : CompatibilityUtils.getOnlinePlayers()) {
            GPlayer nPlayerLoop = PlayerManager.getPlayer(player);

            String tName = "ng_" + player.getName();
            if (tName.length() > 16) {
                tName = tName.substring(0, 16);
            }

            Team team = board.getTeam(tName);

            if (team == null) {
                team = board.registerNewTeam(tName);
                team.addPlayer(player);
            }


            Objective pointsObjective = board.getObjective("points");
            if (Config.POINTSBELOWNAME.getBoolean()) {
                if (pointsObjective == null) {
                    pointsObjective = board.registerNewObjective("points", "dummy");
                    pointsObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);
                    pointsObjective.setDisplayName(Message.MISC_POINTSBELOWNAME.get());
                }

                Score score = pointsObjective.getScore(player);
                score.setScore(nPlayerLoop.getPoints());
            } else if (pointsObjective != null) {
                pointsObjective.unregister();
            }


            PreparedTag tag = new PreparedTagScoreboardImpl(PlayerManager.getPlayer(player));
            team.setPrefix(tag.get());
        }
    }


    public static void refresh() {
        for (Player player : CompatibilityUtils.getOnlinePlayers()) {
            refresh(player);
        }
    }


    public static void refresh(Guild guild) {
        if (guild != null) {
            for (Player player : guild.getOnlinePlayers()) {
                refresh(player);
            }
        }
    }
}
