package me.kayaba.guilds.listener;

import me.kayaba.guilds.api.event.*;
import me.kayaba.guilds.api.util.packet.*;
import me.kayaba.guilds.impl.util.*;
import org.bukkit.event.*;

import java.util.*;

public class PacketListener extends AbstractListener {
    private static final Map<String, PacketExtension.PacketHandler> packetHandlers = new HashMap<>();


    public static void register(PacketExtension.PacketHandler packetHandler) {
        PacketExtension.PacketHandler existingHandler = getHandler(packetHandler.getPacketName());
        if (existingHandler != null && existingHandler.getPriority().getSlot() >= packetHandler.getPriority().getSlot()) {
            return;
        }

        packetHandlers.put(packetHandler.getPacketName(), packetHandler);
    }


    public static void unregister(PacketExtension.PacketHandler packetHandler) {
        if (packetHandlers.containsKey(packetHandler.getPacketName())) {
            packetHandlers.remove(packetHandler.getPacketName());
        }
    }


    public static PacketExtension.PacketHandler getHandler(String packetName) {
        return packetHandlers.get(packetName);
    }

    @EventHandler
    public void onPacketReceive(PacketReceiveEvent event) {
        PacketExtension.PacketHandler packetHandler = getHandler(event.getPacketName());

        if (packetHandler == null) {
            return;
        }

        packetHandler.handle(event);
    }


    public Map<String, PacketExtension.PacketHandler> getPacketHandlers() {
        return packetHandlers;
    }
}
