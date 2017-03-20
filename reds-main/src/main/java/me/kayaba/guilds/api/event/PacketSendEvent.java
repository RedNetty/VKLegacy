package me.kayaba.guilds.api.event;

import me.kayaba.guilds.api.util.packet.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class PacketSendEvent extends Event implements Cancellable, PacketEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Object packet;
    private boolean cancelled;


    public PacketSendEvent(Object packet, Player player) {
        super(true);
        this.packet = packet;
        this.player = player;
        this.cancelled = false;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Object getPacket() {
        return packet;
    }

    @Override
    public String getPacketName() {
        return this.packet.getClass().getSimpleName();
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
