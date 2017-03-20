package me.kayaba.guilds.impl.util.bossbar;

import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.entity.*;

public class BossBarUtils {
    private static IBossBarUtils bossBarUtils;

    static {
        if (bossBarUtils == null) {
            AbstractBossBarUtils abstractBossBarUtils = new AbstractBossBarUtils() {
                @Override
                public void setMessage(Player player, String message) {

                }

                @Override
                public void setMessage(Player player, String message, float percent) {

                }

                @Override
                public void setMessage(Player player, String message, int seconds) {

                }

                @Override
                public boolean hasBar(Player player) {
                    return false;
                }

                @Override
                public void removeBar(Player player) {

                }

                @Override
                public void setHealth(Player player, float percent) {

                }

                @Override
                public float getHealth(Player player) {
                    return 0;
                }

                @Override
                public String getMessage(Player player) {
                    return null;
                }
            };

            if (Config.BOSSBAR_ENABLED.getBoolean()) {
                switch (ConfigManager.getServerVersion()) {
                    case MINECRAFT_1_7_R3:
                    case MINECRAFT_1_7_R4:
                    case MINECRAFT_1_8_R1:
                    case MINECRAFT_1_8_R2:
                    case MINECRAFT_1_8_R3:
                        break;
                    case MINECRAFT_1_9_R1:
                    case MINECRAFT_1_9_R2:
                    case MINECRAFT_1_10_R1:
                    case MINECRAFT_1_10_R2:
                    case MINECRAFT_1_11_R1:
                        bossBarUtils = new BossBarUtilsBukkitImpl();
                        break;
                    default:
                        bossBarUtils = abstractBossBarUtils;
                        break;
                }
            } else {
                bossBarUtils = abstractBossBarUtils;
            }
        }
    }

    public static void setMessage(String message) {
        bossBarUtils.setMessage(message);
    }

    public static void setMessage(Player player, String message) {
        bossBarUtils.setMessage(player, message);
    }

    public static void setMessage(String message, float percent) {
        bossBarUtils.setMessage(message, percent);
    }

    public static void setMessage(Player player, String message, float percent) {
        bossBarUtils.setMessage(player, message, percent);
    }

    public static void setMessage(String message, int seconds) {
        bossBarUtils.setMessage(message, seconds);
    }

    public static void setMessage(Player player, String message, int seconds) {
        bossBarUtils.setMessage(player, message, seconds);
    }

    public static boolean hasBar(Player player) {
        return bossBarUtils.hasBar(player);
    }

    public static void removeBar(Player player) {
        if (hasBar(player)) {
            bossBarUtils.removeBar(player);
        }
    }

    public static void setHealth(Player player, float percent) {
        bossBarUtils.setHealth(player, percent);
    }

    public static float getHealth(Player player) {
        return bossBarUtils.getHealth(player);
    }

    public static String getMessage(Player player) {
        return bossBarUtils.getMessage(player);
    }
}
