package me.kayaba.guilds.api.event;

import me.kayaba.guilds.enums.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class PlayerInteractEntityEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    protected final Entity clickedEntity;
    private final EntityUseAction action;
    private boolean cancelled = false;


    public PlayerInteractEntityEvent(Player player, Entity clickedEntity, EntityUseAction action) {
        super(player);
        this.clickedEntity = clickedEntity;
        this.action = action;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }


    public Entity getEntity() {
        return this.clickedEntity;
    }


    public EntityUseAction getAction() {
        return action;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }
}
