package me.kayaba.guilds.impl.versionimpl.v1_8_R3.packet;

import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.impl.versionimpl.v1_9_R1.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.reflect.*;

import java.lang.reflect.*;

public class PacketPlayInUpdateSign extends AbstractPacket {
    protected static Class<?> iChatBaseComponentClass;
    protected static Class<?> packetInUpdateSignClass;
    protected static Field blockPositionField;
    protected static Field linesField;
    protected static Method getTextMethod;

    private final String[] lines = new String[4];
    private BlockPositionWrapper blockPositionWrapper;

    static {
        try {
            packetInUpdateSignClass = Reflections.getCraftClass("PacketPlayInUpdateSign");
            iChatBaseComponentClass = Reflections.getCraftClass("IChatBaseComponent");

            blockPositionField = Reflections.getPrivateField(packetInUpdateSignClass, "a");
            linesField = Reflections.getPrivateField(packetInUpdateSignClass, "b");

            getTextMethod = Reflections.getMethod(iChatBaseComponentClass, "getText");
        } catch (Exception e) {
            LoggerUtils.exception(e);
        }
    }


    public PacketPlayInUpdateSign(Object packet) throws IllegalAccessException, InvocationTargetException {
        blockPositionWrapper = new BlockPositionWrapperImpl(blockPositionField.get(packet));
        Object[] components = (Object[]) linesField.get(packet);

        int index = 0;
        for (Object component : components) {
            Object line = getTextMethod.invoke(component);
            lines[index] = (String) line;
            index++;
        }
    }


    public BlockPositionWrapper getBlockPositionWrapper() {
        return blockPositionWrapper;
    }


    public String[] getLines() {
        return lines;
    }
}
