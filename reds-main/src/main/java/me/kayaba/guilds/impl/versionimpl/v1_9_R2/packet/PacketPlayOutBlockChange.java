package me.kayaba.guilds.impl.versionimpl.v1_9_R2.packet;

import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.impl.versionimpl.v1_9_R2.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.reflect.*;
import org.bukkit.*;

import java.lang.reflect.*;

public class PacketPlayOutBlockChange extends AbstractPacket {
    protected static Class<?> packetBlockChangeClass;
    protected static Class<?> worldClass;
    protected static Class<?> craftMagicNumbersClass;
    protected static Class<?> blockClass;
    protected static Field blockPositionField;
    protected static Field blockDataField;
    protected static Method getBlockMethod;
    protected static Method fromLegacyDataMethod;
    protected static Method getTypeMethod;

    static {
        try {
            packetBlockChangeClass = Reflections.getCraftClass("PacketPlayOutBlockChange");
            blockClass = Reflections.getCraftClass("Block");
            worldClass = Reflections.getCraftClass("World");
            craftMagicNumbersClass = Reflections.getBukkitClass("util.CraftMagicNumbers");

            blockPositionField = Reflections.getPrivateField(packetBlockChangeClass, "a");
            blockDataField = Reflections.getPrivateField(packetBlockChangeClass, "block");

            getBlockMethod = Reflections.getMethod(craftMagicNumbersClass, "getBlock", Material.class);
            fromLegacyDataMethod = Reflections.getMethod(blockClass, "fromLegacyData");
            getTypeMethod = Reflections.getMethod(worldClass, "getType");
        } catch (Exception e) {
            LoggerUtils.exception(e);
        }
    }


    public PacketPlayOutBlockChange(Location location, Material material, int data) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object blockPosition = new BlockPositionWrapperImpl(location).getBlockPosition();
        packet = packetBlockChangeClass.newInstance();

        Object blockData;
        if (material == null) {
            Object world = Reflections.getHandle(location.getWorld());
            blockData = getTypeMethod.invoke(world, blockPosition);
        } else {
            blockData = getData(material, data);
        }

        blockPositionField.set(packet, blockPosition);
        blockDataField.set(packet, blockData);
    }


    protected Object getData(Material material, int data) throws InvocationTargetException, IllegalAccessException {
        Object block = getBlockMethod.invoke(craftMagicNumbersClass, material);
        return fromLegacyDataMethod.invoke(block, data);
    }
}
