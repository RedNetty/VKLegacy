package io.vawke.practice.guild.factory;

import io.vawke.practice.factory.Factory;
import io.vawke.practice.guild.factory.event.GuildChatListener;
import io.vawke.practice.guild.factory.event.GuildListener;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

/**
 * Created by Giovanni on 12-2-2017.
 */
public class GuildEventFactory extends Factory<Listener> {

    public GuildEventFactory() {
        this.register(new GuildChatListener());
        this.register(new GuildListener());
    }
}
