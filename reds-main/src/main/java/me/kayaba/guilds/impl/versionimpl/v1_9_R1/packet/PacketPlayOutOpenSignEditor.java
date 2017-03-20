package me.kayaba.guilds.impl.versionimpl.v1_9_R1.packet;

import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.impl.versionimpl.v1_9_R1.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.reflect.*;
import org.bukkit.*;

import java.lang.reflect.*;

public class PacketPlayOutOpenSignEditor extends AbstractPacket {
    protected static Class<?> packetOpenSignEditorClass;
    protected static Class<?> blockPositionClass;

    static {
        try {
            packetOpenSignEditorClass = Reflections.getCraftClass("PacketPlayOutOpenSignEditor");
            blockPositionClass = Reflections.getCraftClass("BlockPosition");
        } catch (Exception e) {
            LoggerUtils.exception(e);
        }
    }


    public PacketPlayOutOpenSignEditor(Location location) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        BlockPositionWrapper blockPosition = new BlockPositionWrapperImpl(location);
        packet = packetOpenSignEditorClass.getConstructor(blockPositionClass).newInstance(blockPosition.getBlockPosition());
    }
}
