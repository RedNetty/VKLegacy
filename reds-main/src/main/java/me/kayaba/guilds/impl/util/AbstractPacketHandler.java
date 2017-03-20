package me.kayaba.guilds.impl.util;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.util.packet.*;
import me.kayaba.guilds.listener.*;
import org.bukkit.event.*;

public abstract class AbstractPacketHandler implements PacketExtension.PacketHandler {
    protected static final PracticeServer plugin = PracticeServer.getInstance();
    private final String packetName;
    protected EventPriority priority = EventPriority.NORMAL;


    public AbstractPacketHandler(String packetName) {
        this.packetName = packetName;
        PacketListener.register(this);
    }

    @Override
    public final EventPriority getPriority() {
        return priority;
    }

    @Override
    public String getPacketName() {
        return packetName;
    }


    protected final void setPriority(EventPriority priority) {
        this.priority = priority;
    }
}
