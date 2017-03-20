package me.kayaba.guilds.api.event;


import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import org.bukkit.event.*;

public class GuildAbandonEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Guild guild;
    private boolean cancelled;
    private AbandonCause cause;


    public GuildAbandonEvent(Guild guild, AbandonCause cause) {
        this.guild = guild;
        this.cause = cause;
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


    public Guild getGuild() {
        return guild;
    }


    public void setCause(AbandonCause cause) {
        this.cause = cause;
    }


    public AbandonCause getCause() {
        return cause;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }
}
