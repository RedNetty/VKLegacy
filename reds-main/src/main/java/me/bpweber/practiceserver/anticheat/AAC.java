/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.ChatSerializer
 *  net.minecraft.server.v1_7_R4.EntityPlayer
 *  net.minecraft.server.v1_7_R4.IChatBaseComponent
 *  net.minecraft.server.v1_7_R4.Packet
 *  net.minecraft.server.v1_7_R4.PacketPlayOutChat
 *  net.minecraft.server.v1_7_R4.PlayerConnection
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.event.player.PlayerChatTabCompleteEvent
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package me.bpweber.practiceserver.anticheat;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import me.bpweber.practiceserver.PracticeServer;
import me.konsolas.aac.api.HackType;
import me.konsolas.aac.api.PlayerViolationCommandEvent;
import me.konsolas.aac.api.PlayerViolationEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.rmi.activation.UnknownObjectException;
import java.util.UUID;


public class AAC implements Listener {

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, PracticeServer.plugin);
    }

    public void onDisable() {

    }
    public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = PracticeServer.plugin.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            try {
                throw new UnknownObjectException("getWorldGuard() of GameAPI.class is RETURNING NULL!");
            } catch (UnknownObjectException e) {
                e.printStackTrace();
            }
        }
        return (WorldGuardPlugin) plugin;
    }
    public static boolean isInSafeRegion(Location location) {
//        if (!location.getWorld().equals(Bukkit.getWorlds().get(0))) {
//            return false;
//        }
        ApplicableRegionSet region = getWorldGuard().getRegionManager(location.getWorld())
                .getApplicableRegions(location);
        return region.getFlag(DefaultFlag.PVP) != null && !region.allows(DefaultFlag.PVP)
                && region.getFlag(DefaultFlag.MOB_DAMAGE) != null && !region.allows(DefaultFlag.MOB_DAMAGE);
    }

    public static boolean isNonPvPRegion(Location location) {
        ApplicableRegionSet region = getWorldGuard().getRegionManager(location.getWorld())
                .getApplicableRegions(location);
        return region.getFlag(DefaultFlag.PVP) != null && !region.allows(DefaultFlag.PVP);
    }

    public static boolean isNonMobDamageRegion(Location location) {
        ApplicableRegionSet region = getWorldGuard().getRegionManager(location.getWorld())
                .getApplicableRegions(location);
        return region.getFlag(DefaultFlag.MOB_DAMAGE) != null && !region.allows(DefaultFlag.MOB_DAMAGE);
    }

    /**
     * Will check the players region
     *
     * @param uuid
     * @param region
     * @return
     * @since 1.0
     */
    public static boolean isPlayerInRegion(UUID uuid, String region) {
        return getWorldGuard().getRegionManager(Bukkit.getPlayer(uuid).getWorld())
                .getApplicableRegions(Bukkit.getPlayer(uuid).getLocation()).getRegions().contains(region);
    }

    public static boolean isInWorld(Player player, World world) {
        return world != null && player.getLocation().getWorld().equals(world);
    }

    @EventHandler
    public void onPlayerViolation(PlayerViolationEvent e) {
        HackType type = e.getHackType();
        if(type == HackType.FLY && !isInWorld(e.getPlayer(), Bukkit.getWorlds().get(0)))
        {
            e.setCancelled(true); // They are in a realm.. They can fly
            return;
        }
        if(type == HackType.FLY && isInWorld(e.getPlayer(), Bukkit.getWorlds().get(0)))
        {
            if(isPlayerInRegion(e.getPlayer().getUniqueId(), "tutorial_island"))
            {
                e.setCancelled(true);
                return; // Don't matter.. They in tutorial
            }
            if(isInSafeRegion(e.getPlayer().getLocation()))
            {
                e.setCancelled(true);
                return; // Fine.. Fly Hack in safe zones lolz
            }
            e.setCancelled(false);
            return;
        }
        if(type == HackType.SPEED) {
            return; // We made our own speed checker.
        }
        if(type == HackType.CLIMB)
        {
            e.setCancelled(true);
            return; // Fuck climb hacks
        }
        if(type == HackType.NOFALL)
        {
            e.setCancelled(true);
            return; // EVERYONE has NOFALL in SC.. Lolz
        }
        if(type == HackType.FASTPLACE && !isInWorld(e.getPlayer(), Bukkit.getWorlds().get(0)))
        {
            e.setCancelled(true);
            return; // This is for realms
        }
        if(type == HackType.FASTBREAK && !isInWorld(e.getPlayer(), Bukkit.getWorlds().get(0)))
        {
            e.setCancelled(true);
            return; // This is for realms
        }
        if(type == HackType.BADPACKETS)
        {
            e.setCancelled(true);
            return; // Nope
        }
        if(type == HackType.REGEN)
        {
            e.setCancelled(true);
            return;
        }
        if(type == HackType.NOSWING)
        {
            e.setCancelled(true);
            return;
        }
        if(type == HackType.HITBOX)
        {
            e.setCancelled(true);
            return;
        }
        if( type == HackType.INTERACT)
        {
            e.setCancelled(true);
            return;
        }
        if( type == HackType.PHASE)
        {
            e.setCancelled(true);
            return;
        }
        if (type == HackType.SPAM)
        {
            e.setCancelled(true);
            return;
        }
        if (type == HackType.KNOCKBACK)
        {
            e.setCancelled(true);
            return;
        }
        for(Player p : Bukkit.getOnlinePlayers())
        {
            if(p.hasPermission("anticheat.getautism"))
            {
                p.sendMessage(ChatColor.RED.toString() + "ANTI-CHEAT: " + e.getPlayer().getName() + " is hacking on " + Bukkit.getServerName());
            }
        }
        e.getPlayer().kickPlayer("Hacking is not allowed.");
        return;
    }
    @EventHandler
    public void onPlayerViolationCommand(PlayerViolationCommandEvent e) {
        for(Player p : Bukkit.getOnlinePlayers())
        {
            if(p.hasPermission("anticheat.getautism"))
            {
                p.sendMessage(ChatColor.RED.toString() + "ANTI-CHEAT: " + e.getPlayer().getName() + " is hacking on " + Bukkit.getServerName());
            }
        }
        e.setCancelled(true); // Don't let AAC execute any commands.. We will handle that in our onPlayerViolation event
        e.getPlayer().kickPlayer("Hacking is not allowed.");
    }
}

