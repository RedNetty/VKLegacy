package io.vawke.practice.guild.factory.event;

import io.vawke.practice.Game;
import io.vawke.practice.factory.FactoryObject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Giovanni on 12-2-2017.
 */
public class GuildChatListener implements Listener, FactoryObject {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

    }

    @Override
    public void registerWatcher() {
        Game.getPracticeServer().getServer().getPluginManager().registerEvents(this, Game.getPracticeServer());
    }
}
