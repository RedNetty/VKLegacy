package me.kayaba.guilds.util.reflect;

import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;

import me.bpweber.practiceserver.*;

public class PacketSender {

    public static void sendPacket(List<Player> players, Object... packets) {
        for (Player player : players) {
            sendPacket(player, packets);
        }
    }


    @Deprecated
    public static void sendPacket(Player[] players, Object... packets) {
        sendPacket(Arrays.asList(players), packets);
    }


    public static void sendPacket(Player player, Object... packets) {
        final List<Object> packetList = new ArrayList<>();

        for (Object packet : packets) {
            if (packet instanceof Packet) {
                packetList.add(((Packet) packet).getPacket());
            } else {
                packetList.add(packet);
            }
        }


        PracticeServer.getInstance().getPacketExtension().sendPacket(player, packetList.toArray());
    }


    public void sendPacket(Location center, double range, Object... packets) {
        sendPacket(ParticleUtils.getPlayersInRadius(center, range), packets);
    }
}
