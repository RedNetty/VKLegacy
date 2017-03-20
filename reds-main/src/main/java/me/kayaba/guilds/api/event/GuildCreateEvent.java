package me.kayaba.guilds.api.event;


import me.kayaba.guilds.api.basic.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class GuildCreateEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Guild guild;
    private final Player creator;
    private boolean cancelled;


    public GuildCreateEvent(Guild guild, Player creator) {
        this.guild = guild;
        this.creator = creator;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }


    public Player getCreator() {
        return creator;
    }


    public Guild getGuild() {
        return guild;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }
}
