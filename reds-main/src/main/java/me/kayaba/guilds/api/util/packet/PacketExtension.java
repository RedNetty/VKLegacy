package me.kayaba.guilds.api.util.packet;

import me.kayaba.guilds.api.event.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public interface PacketExtension {

    void registerPlayer(final Player player);


    void unregisterChannel();


    void sendPacket(Player player, Object... packets);

    interface PacketHandler {

        String getPacketName();


        EventPriority getPriority();


        void handle(PacketReceiveEvent event);
    }
}
