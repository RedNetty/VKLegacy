package me.kayaba.guilds.api.event;

import me.kayaba.guilds.api.util.packet.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class PacketReceiveEvent extends Event implements Cancellable, PacketEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Object packet;
    private boolean cancelled;


    public PacketReceiveEvent(Object packet, Player player) {
        this.packet = packet;
        this.player = player;
    }

    @Override
    public Object getPacket() {
        return packet;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getPacketName() {
        return packet.getClass().getSimpleName();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }
}
