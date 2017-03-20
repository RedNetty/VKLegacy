package me.bpweber.practiceserver.pvp;

import com.google.common.collect.*;
import com.google.common.util.concurrent.*;
import me.bpweber.practiceserver.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.*;

import java.util.*;
import java.util.concurrent.*;

public class ForceField implements Listener {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("PvP ForceField Thread").build());
    private final Map<UUID, Set<Location>> previousUpdates = new HashMap<>();
    public ArrayList<Player> tag = new ArrayList<Player>();
    private static final List<BlockFace> ALL_DIRECTIONS = ImmutableList.of(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);

    public void onEnable() {
        PracticeServer.log.info("Forcefield ");
        Bukkit.getServer().getPluginManager().registerEvents(this, PracticeServer.plugin);
    }

    @EventHandler
    public void shutdown(PluginDisableEvent event) {
        // Shutdown executor service and clean up threads
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException ignore) {
        }

        // Go through all previous updates and revert spoofed blocks
        for (UUID uuid : previousUpdates.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;

            for (Location location : previousUpdates.get(uuid)) {
                Block block = location.getBlock();
                player.sendBlockChange(location, block.getType(), block.getData());
            }
        }
    }

    @EventHandler
    public void onTag(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            if (tag.contains(p)) {
                tag.remove(p);
                tag.add(p);
                Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(PracticeServer.plugin, new Runnable() {
                    @Override
                    public void run() {
                        tag.remove(p);
                    }
                }, 180);
            } else {
                tag.add(p);
                Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(PracticeServer.plugin, new Runnable() {
                    @Override
                    public void run() {
                        tag.remove(p);
                    }
                }, 180);
            }

        }
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void updateViewedBlocks(PlayerMoveEvent event) {
        final Player player = event.getPlayer();

        if (player == null) return;

        // check if we have to send blocks or remove them
        if (!tag.contains(player) && !Alignments.chaotic.containsKey(player.getName()) &&
                !previousUpdates.containsKey(player.getUniqueId()))
            return;

        // Do nothing if player hasn't moved over a whole block
        Location t = event.getTo();
        Location f = event.getFrom();
        if (t.getBlockX() == f.getBlockX() && t.getBlockY() == f.getBlockY() &&
                t.getBlockZ() == f.getBlockZ()) {
            return;
        }

        // Asynchronously send block changes around player
        executorService.submit(() -> {
            // Stop processing if player has logged off
            UUID uuid = player.getUniqueId();
            if (!player.isOnline()) {
                previousUpdates.remove(uuid);
                return;
            }

            // Update the players force field perspective and find all blocks to stop spoofing
            Set<Location> changedBlocks = getChangedBlocks(player);
            Material forceFieldMaterial = Material.STAINED_GLASS;
            byte forceFieldMaterialDamage = 14; // 14 = red for stained glass

            Set<Location> removeBlocks;
            if (previousUpdates.containsKey(uuid)) {
                removeBlocks = previousUpdates.remove(uuid);
            } else {
                removeBlocks = new HashSet<>();
            }

            for (Location location : changedBlocks) {
                player.sendBlockChange(location, forceFieldMaterial, forceFieldMaterialDamage);
                removeBlocks.remove(location);
            }

            // Remove no longer used spoofed blocks
            for (Location location : removeBlocks) {
                Block block = location.getBlock();
                player.sendBlockChange(location, block.getType(), block.getData());
            }

            previousUpdates.put(uuid, changedBlocks);
        });
    }

    private Set<Location> getChangedBlocks(Player player) {
        Set<Location> locations = new HashSet<>();


        if (player == null) return locations;

        // Do nothing if player is not tagged or chaotic
        if (!tag.contains(player) && !Alignments.chaotic.containsKey(player.getName())) return locations;

        // Find the radius around the player
        int r = 10;
        Location l = player.getLocation();
        Location loc1 = l.clone().add(r, 0, r);
        Location loc2 = l.clone().subtract(r, 0, r);
        int topBlockX = loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX();
        int bottomBlockX = loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX();
        int topBlockZ = loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ();
        int bottomBlockZ = loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ();

        // Iterate through all blocks surrounding the player
        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                // Location corresponding to current loop
                Location location = new Location(l.getWorld(), (double) x, l.getY(), (double) z);

                // PvP is enabled here, no need to do anything else
                if (!Alignments.isSafeZone(location)) continue;

                // Check if PvP is enabled in a location surrounding this
                if (!isPvpSurrounding(location)) continue;

                // Add circular locations
                for (int i = -r; i < r; i++) {
                    Location loc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
                    loc.setY(loc.getY() + i);

                    if (l.distanceSquared(loc) > 80) continue;

                    // Do nothing if the block at the location is not air
                    if (!loc.getBlock().getType().equals(Material.AIR)) continue;

                    // Add this location to locations
                    locations.add(new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
                }
            }
        }

        return locations;
    }

    private boolean isPvpSurrounding(Location loc) {
        for (BlockFace direction : ALL_DIRECTIONS) {
            if (!Alignments.isSafeZone(loc.getBlock().getRelative(direction).getLocation())) {
                return true;
            }
        }

        return false;
    }
}