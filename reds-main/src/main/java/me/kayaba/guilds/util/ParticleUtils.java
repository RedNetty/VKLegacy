package me.kayaba.guilds.util;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.lang.reflect.*;
import java.util.*;

public class ParticleUtils {

    public static void createSuperKayaba(Entity entity) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        createSuperKayaba(entity.getLocation());
    }


    public static List<Vector> getCircleVectors(int radius, int precision) {
        final List<Vector> list = new ArrayList<>();

        for (int i = 0; i < precision; i++) {
            double p1 = (i * Math.PI) / (precision / 2);
            double p2 = (((i == 0) ? precision : i - 1) * Math.PI) / (precision / 2);

            double x1 = Math.cos(p1) * radius;
            double x2 = Math.cos(p2) * radius;
            double z1 = Math.sin(p1) * radius;
            double z2 = Math.sin(p2) * radius;

            Vector vec = new Vector(x2 - x1, 0, z2 - z1);
            list.add(vec);
        }

        return list;
    }


    public static void createSuperKayaba(Location location) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        float speed = 1F;
        double range = 15D;

        location = location.clone();
        location.add(0, 0.5, 0);

        for (Vector vector : getCircleVectors(15, 100)) {
            ParticleEffect.SNOW_SHOVEL.send(location, vector, speed, 0, null, range);
        }
    }


    public static List<Player> getPlayersInRadius(Location center, double range) {
        if (range < 1.0D) {
            throw new IllegalArgumentException("The range is lower than 1");
        }

        double squared = range * range;
        List<Player> list = new ArrayList<>();

        for (Player player : CompatibilityUtils.getOnlinePlayers()) {
            if (player.getWorld().equals(center.getWorld()) && player.getLocation().distanceSquared(center) <= squared) {
                list.add(player);
            }
        }

        return list;
    }
}
