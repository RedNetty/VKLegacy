package me.kayaba.guilds.listener;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;

public class PvpListener extends AbstractListener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player attacker = null;
            Player player = (Player) event.getEntity();

            if (event.getDamager() instanceof Player) {
                attacker = (Player) event.getDamager();
            } else if (event.getDamager().getType() == EntityType.ARROW) {
                Arrow arrow = (Arrow) event.getDamager();

                if (arrow.getShooter() instanceof Player) {
                    attacker = (Player) arrow.getShooter();
                }
            }

            if (attacker != null) {
                GPlayer nPlayer = PlayerManager.getPlayer(player);
                GPlayer nPlayerAttacker = PlayerManager.getPlayer(attacker);


                if (!nPlayerAttacker.getName().equals(nPlayer.getName())) {
                    if (nPlayerAttacker.hasGuild() && nPlayer.hasGuild()) {
                        if (plugin.getPlayerManager().isGuildMate(player, attacker)) {
                            if (!nPlayer.getGuild().getFriendlyPvp()) {
                                Message.CHAT_PVP_TEAM.send(attacker);
                                event.setCancelled(true);
                                event.setDamage(0.0);
                                ((LivingEntity) event.getEntity()).setNoDamageTicks(0);


                                if (event.getDamager().getType() == EntityType.ARROW) {
                                    event.getDamager().remove();
                                }
                            }
                        } else if (plugin.getPlayerManager().isAlly(player, attacker)) {
                            if (!(nPlayer.getGuild().getFriendlyPvp() && nPlayerAttacker.getGuild().getFriendlyPvp())) {
                                Message.CHAT_PVP_ALLY.send(attacker);
                                event.setCancelled(true);
                                event.setDamage(0.0);
                                ((LivingEntity) event.getEntity()).setNoDamageTicks(0);


                                if (event.getDamager().getType() == EntityType.ARROW) {
                                    event.getDamager().remove();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
