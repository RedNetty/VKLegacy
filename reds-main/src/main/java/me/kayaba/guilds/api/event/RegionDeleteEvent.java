package me.kayaba.guilds.api.event;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import org.apache.commons.lang.*;
import org.bukkit.event.*;

public class RegionDeleteEvent extends Event implements Cancellable {
    public enum Cause {
        ADMIN,
        DELETE,
        ABANDON,
        RAID;


        public static Cause fromGuildAbandonCause(AbandonCause abandonCause) {
            switch (abandonCause) {
                case ADMIN:
                case ADMIN_ALL:
                    return RegionDeleteEvent.Cause.ADMIN;
                case INACTIVE:
                case INVALID:
                case UNLOADED:
                    return RegionDeleteEvent.Cause.ABANDON;
                case RAID:
                    return RegionDeleteEvent.Cause.RAID;
                case PLAYER:
                    return RegionDeleteEvent.Cause.DELETE;
            }

            return null;
        }
    }

    private static final HandlerList handlers = new HandlerList();
    private final Cause cause;
    private final GRegion region;
    private boolean cancelled;


    public RegionDeleteEvent(GRegion region, Cause cause) {
        Validate.notNull(cause, "Region delete cause cannot be null");

        this.region = region;
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


    public GRegion getRegion() {
        return region;
    }


    public Cause getCause() {
        return cause;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }
}
