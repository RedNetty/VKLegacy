package me.kayaba.guilds.util;

import com.sk89q.worldguard.protection.regions.*;
import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.configuration.*;
import org.bukkit.entity.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

public final class RegionUtils {
    private RegionUtils() {

    }


    public static List<Block> getBorderBlocks(GRegion region) {
        return getBorderBlocks(region.getCorner(0), region.getCorner(1));
    }


    public static List<Block> getBorderBlocks(Location l1, Location l2) {
        final List<Block> blocks = new ArrayList<>();


        World world = l1.getWorld() == null ? Bukkit.getWorlds().get(0) : l1.getWorld();

        int x;
        int z;

        int xs;
        int zs;

        int x1 = l1.getBlockX();
        int x2 = l2.getBlockX();
        int z1 = l1.getBlockZ();
        int z2 = l2.getBlockZ();

        int t;

        int difX = Math.abs(x1 - x2) + 1;
        int difZ = Math.abs(z1 - z2) + 1;

        if (l1.getBlockX() < l2.getBlockX()) {
            xs = l1.getBlockX();
        } else {
            xs = l2.getBlockX();
        }

        if (l1.getBlockZ() < l2.getBlockZ()) {
            zs = l1.getBlockZ();
        } else {
            zs = l2.getBlockZ();
        }

        for (t = 0; t < difX; t++) {
            x = xs + t;

            Block highestBlock1 = world.getHighestBlockAt(x, z1);
            Block highestBlock2 = world.getHighestBlockAt(x, z2);
            int highest1 = highestBlock1.getY() - (highestBlock1.getType() == Material.SNOW ? 0 : 1);
            int highest2 = highestBlock2.getY() - (highestBlock2.getType() == Material.SNOW ? 0 : 1);

            blocks.add(world.getBlockAt(x, highest1, z1));
            blocks.add(world.getBlockAt(x, highest2, z2));
        }


        for (t = 0; t < difZ; t++) {
            z = zs + t;

            Block highestBlock1 = world.getHighestBlockAt(x1, z);
            Block highestBlock2 = world.getHighestBlockAt(x2, z);
            int highest1 = highestBlock1.getY() - (highestBlock1.getType() == Material.SNOW ? 0 : 1);
            int highest2 = highestBlock2.getY() - (highestBlock2.getType() == Material.SNOW ? 0 : 1);

            blocks.add(world.getBlockAt(x1, highest1, z));
            blocks.add(world.getBlockAt(x2, highest2, z));
        }

        return blocks;
    }


    @SuppressWarnings("deprecation")
    public static void resetBlock(Player player, Block block) {
        if (player == null || block == null) {
            return;
        }

        Material material = block.getWorld().getBlockAt(block.getLocation()).getType();
        byte data = block.getWorld().getBlockAt(block.getLocation()).getData();

        player.sendBlockChange(block.getLocation(), material, data);
    }


    public static Location getCenterLocation(Location l1, Location l2) {
        int width = Math.abs(l1.getBlockX() - l2.getBlockX());
        int height = Math.abs(l1.getBlockZ() - l2.getBlockZ());

        int newX = l1.getBlockX() + (l1.getBlockX() < l2.getBlockX() ? width : -width) / 2;
        int newZ = l1.getBlockZ() + (l1.getBlockZ() < l2.getBlockZ() ? height : -height) / 2;

        return new Location(l1.getWorld(), newX, 0, newZ);
    }


    public static int checkRegionSize(Location l1, Location l2) {
        int x1 = l1.getBlockX();
        int x2 = l2.getBlockX();
        int z1 = l1.getBlockZ();
        int z2 = l2.getBlockZ();

        int difX = Math.abs(x1 - x2) + 1;
        int difZ = Math.abs(z1 - z2) + 1;

        return difX * difZ;
    }


    public static Location sectionToLocation(ConfigurationSection section) {
        World world;
        try {
            world = PracticeServer.getInstance().getServer().getWorld(UUID.fromString(section.getString("world")));
        } catch (IllegalArgumentException e) {
            world = PracticeServer.getInstance().getServer().getWorld(section.getString("world"));
        }

        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        float yaw = (float) section.getDouble("yaw");
        float pitch = (float) section.getDouble("pitch");

        if (world == null) {
            return null;
        }

        return new Location(world, x, y, z, yaw, pitch);
    }


    public static Location deserializeLocation2D(String string) {
        String[] split = string.split(";");
        return new Location(null, Integer.parseInt(split[0]), 0, Integer.parseInt(split[1]));
    }


    public static Area toArea(ProtectedRegion region) {
        int x = region.getMinimumPoint().getBlockX();
        int z = region.getMinimumPoint().getBlockZ();
        int width = region.getMaximumPoint().getBlockX() - x;
        int height = region.getMaximumPoint().getBlockZ() - z;
        return new Area(new Rectangle(x, z, width, height));
    }
}
