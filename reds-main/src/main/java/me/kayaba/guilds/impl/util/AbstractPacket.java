package me.kayaba.guilds.impl.util;

import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.util.reflect.*;
import org.bukkit.entity.*;

public class AbstractPacket implements Packet {
    protected Object packet;

    @Override
    public void send(Player player) {
        PacketSender.sendPacket(player, packet);
    }

    @Override
    public Object getPacket() {
        return packet;
    }
}
