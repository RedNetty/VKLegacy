package me.kayaba.guilds.api.util.packet;

import org.bukkit.entity.*;

public interface PacketEvent {

    Object getPacket();


    Player getPlayer();


    String getPacketName();
}
