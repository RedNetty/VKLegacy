package me.kayaba.guilds.api.event;

import me.kayaba.guilds.api.basic.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class PlayerEnterRegionEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final GRegion region;


    public PlayerEnterRegionEvent(Player who, GRegion region) {
        super(who);
        this.region = region;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }


    public GRegion getRegion() {
        return region;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }
}
