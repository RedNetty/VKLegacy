package me.kayaba.guilds.listener;

import me.bpweber.practiceserver.damage.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;

public class DeathListener extends AbstractListener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player attacker = Damage.lastphit.get(victim);
        if(attacker == null) return; // Fixes some errors.
        GPlayer nPlayer = PlayerManager.getPlayer(victim);

        if (attacker == null || attacker.equals(victim)) {
            return;
        }

        GPlayer nPlayerAttacker = PlayerManager.getPlayer(attacker);

        nPlayerAttacker.addKill();
        nPlayer.addDeath();

        if(nPlayer.hasGuild() && nPlayerAttacker.hasGuild())
        {
            Guild guildAttacker = nPlayerAttacker.getGuild();
            Guild guildVictim = nPlayer.getGuild();
            if(!guildAttacker.isWarWith(guildVictim))
            {
                return; // DO NOT RUN THE CODE IF THEY AREN'T AN ENEMY GUILD!
            }
            if(guildVictim.getLives() < 0)
            {
                guildVictim.setLives(0); // Small fix in case
            }
            if(guildVictim.getLives() > 0)
            {
                guildVictim.setLives(guildVictim.getLives() - 1);
            }
            long createdTime = NumberUtils.systemSeconds() - guildVictim.getTimeCreated();
            long timeProtection = Config.GUILD_CREATEPROTECTION.getSeconds() - createdTime;
            if(timeProtection <= 0)
            {
                if (nPlayer.hasGuild()) {
                    if(guildVictim.getLives() == 0)
                    {
                        guildVictim.takePoints(Config.GUILD_DEATHPOINTS.getInt());
                        guildAttacker.addPoints(Config.GUILD_KILLPOINTS.getInt());
                        for(Player p : guildAttacker.getOnlinePlayers())
                        {
                            p.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + ">>> " + ChatColor.AQUA + "Received " + Config.GUILD_KILLPOINTS.getInt() + " for killing an enemy guild's member." + " <<<");
                            p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
                            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        }
                        for(Player p : guildVictim.getOnlinePlayers())
                        {
                            p.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + ">>> " + ChatColor.AQUA + "Lost " + Config.GUILD_DEATHPOINTS.getInt() + " for being killed by an enemy guild's member." + " <<<");
                            p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
                            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        }
                    }
                }
            }
        }

        TabUtils.refresh(attacker);
        TabUtils.refresh(victim);
        TagUtils.refresh(attacker);
        TagUtils.refresh(victim);
    }
}
