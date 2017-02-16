package io.vawke.practice.guild.factory.event;

import io.vawke.practice.Game;
import io.vawke.practice.factory.FactoryObject;
import io.vawke.practice.guild.Guild;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Giovanni on 12-2-2017.
 */
public class GuildListener implements Listener, FactoryObject {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Game.getGame().directGuildRegistry().hasGuild(event.getPlayer().getUniqueId())) {
            Guild guild = Game.getGame().directGuildRegistry().ownerGuild(event.getPlayer());
            if (guild != null) {
                guild.checkOwnerJoin(event.getPlayer());
                guild.sendAlert(ChatColor.DARK_AQUA + event.getPlayer().getName() + " has connected to US-1.");
            } else {
                // Maybe he doesn't own a guild but is in one?
                Guild guild1 = Game.getGame().directGuildRegistry().playerGuild(event.getPlayer());
                if (guild1 != null) {
                    guild1.sendAlert(ChatColor.DARK_AQUA + event.getPlayer().getName() + " has connected to US-1.");
                }
            }
        }
    }

    @Override
    public void registerWatcher() {
        Game.getPracticeServer().getServer().getPluginManager().registerEvents(this, Game.getPracticeServer());
    }
}
