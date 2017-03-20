package me.kayaba.guilds.impl.versionimpl.v1_9_R1.packet;

import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.impl.versionimpl.v1_9_R1.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.reflect.*;
import org.bukkit.*;

import java.lang.reflect.*;

public class PacketPlayOutUpdateSign extends AbstractPacket {
    protected static Class<?> packetOutUpdateSignClass;
    protected static Class<?> chatComponentTextClass;
    protected static Field worldField;
    protected static Field blockPositionField;
    protected static Field linesField;
    protected static Constructor<?> chatComponentTextConstructor;

    static {
        try {
            packetOutUpdateSignClass = Reflections.getCraftClass("PacketPlayOutUpdateSign");
            chatComponentTextClass = Reflections.getCraftClass("ChatComponentText");

            worldField = Reflections.getPrivateField(packetOutUpdateSignClass, "a");
            blockPositionField = Reflections.getPrivateField(packetOutUpdateSignClass, "b");
            linesField = Reflections.getPrivateField(packetOutUpdateSignClass, "c");

            chatComponentTextConstructor = chatComponentTextClass.getConstructor(String.class);
        } catch (NoSuchMethodException | ClassNotFoundException | NoSuchFieldException e) {
            LoggerUtils.exception(e);
        }
    }


    public PacketPlayOutUpdateSign(Location location, String[] lines) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        packet = packetOutUpdateSignClass.newInstance();
        Object blockPosition = new BlockPositionWrapperImpl(location).getBlockPosition();
        worldField.set(packet, Reflections.getHandle(location.getWorld()));
        blockPositionField.set(packet, blockPosition);

        int n = 4;
        Object array = Array.newInstance(chatComponentTextClass, n);
        for (int i = 0; i < n; i++) {
            Object val = chatComponentTextConstructor.newInstance(lines[i]);

            Array.set(array, i, val);
        }

        linesField.set(packet, array);
    }
}
