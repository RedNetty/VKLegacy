package me.kayaba.guilds.impl.versionimpl.v1_9_R2.packet;

import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.api.util.reflect.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.impl.versionimpl.v1_9_R2.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.reflect.*;

import java.lang.reflect.*;

public class PacketPlayInUpdateSign extends AbstractPacket {
    protected static Class<?> packetInUpdateSignClass;
    protected static Field blockPositionField;
    protected static FieldAccessor<String[]> linesField;

    private String[] lines;
    private BlockPositionWrapper blockPositionWrapper;

    static {
        try {
            packetInUpdateSignClass = Reflections.getCraftClass("PacketPlayInUpdateSign");
            blockPositionField = Reflections.getPrivateField(packetInUpdateSignClass, "a");
            linesField = Reflections.getField(packetInUpdateSignClass, String[].class, 0);
        } catch (Exception e) {
            LoggerUtils.exception(e);
        }
    }


    public PacketPlayInUpdateSign(Object packet) throws IllegalAccessException {
        blockPositionWrapper = new BlockPositionWrapperImpl(blockPositionField.get(packet));
        lines = linesField.get(packet);
    }


    public BlockPositionWrapper getBlockPositionWrapper() {
        return blockPositionWrapper;
    }


    public String[] getLines() {
        return lines;
    }
}
