package me.kayaba.guilds.api.util;

import org.bukkit.entity.*;

public interface Packet {

    void send(Player player);


    Object getPacket();
}
