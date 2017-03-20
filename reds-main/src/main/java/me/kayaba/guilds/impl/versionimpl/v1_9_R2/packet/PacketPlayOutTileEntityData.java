package me.kayaba.guilds.impl.versionimpl.v1_9_R2.packet;

import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.impl.versionimpl.v1_9_R2.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.reflect.*;
import org.bukkit.*;

import java.lang.reflect.*;

public class PacketPlayOutTileEntityData extends AbstractPacket {
    enum Action {
        SPAWNPOTENTIALS(1),
        COMMANDBLOCKTEXT(2),
        BEACON(3),
        MOBHEAD(4),
        FLOWER(5),
        BANNER(6),
        STRUCTURE(7),
        END_GATEWAY(8),
        SIGN_TEXT(9);

        private final int id;


        Action(int id) {
            this.id = id;
        }


        public int getId() {
            return id;
        }
    }

    protected static Class<?> packetPlayOutTileEntityData;
    protected static Class<?> nBTTagCompoundClass;
    protected static Class<?> blockPositionClass;
    protected static Class<?> iChatBaseComponentClass;
    protected static Class<?> chatComponentTextClass;
    protected static Class<?> craftChatMessageClass;
    protected static Class<?> chatSerializerClass;
    protected static Method nBTTagCompoundSetStringMethod;
    protected static Method nBTTagCompoundSetIntMethod;
    protected static Method craftChatMessageFromStringMethod;
    protected static Method chatSerializerAMethod;

    static {
        try {
            packetPlayOutTileEntityData = Reflections.getCraftClass("PacketPlayOutTileEntityData");
            nBTTagCompoundClass = Reflections.getCraftClass("NBTTagCompound");
            blockPositionClass = Reflections.getCraftClass("BlockPosition");
            iChatBaseComponentClass = Reflections.getCraftClass("IChatBaseComponent");
            chatComponentTextClass = Reflections.getCraftClass("ChatComponentText");
            chatSerializerClass = Reflections.getCraftClass("IChatBaseComponent$ChatSerializer");
            craftChatMessageClass = Reflections.getBukkitClass("util.CraftChatMessage");

            nBTTagCompoundSetStringMethod = Reflections.getMethod(nBTTagCompoundClass, "setString");
            nBTTagCompoundSetIntMethod = Reflections.getMethod(nBTTagCompoundClass, "setInt");
            craftChatMessageFromStringMethod = Reflections.getMethod(craftChatMessageClass, "fromString", String.class);
            chatSerializerAMethod = Reflections.getMethod(chatSerializerClass, "a", iChatBaseComponentClass);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            LoggerUtils.exception(e);
        }
    }


    public PacketPlayOutTileEntityData(Location location, Action action, Object data) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        packet = packetPlayOutTileEntityData.getConstructor(
                blockPositionClass,
                int.class,
                nBTTagCompoundClass
        ).newInstance(
                new BlockPositionWrapperImpl(location).getBlockPosition(),
                action.getId(),
                data
        );
    }


    public static PacketPlayOutTileEntityData getSignChange(Location location, String[] lines) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        Object tag = nBTTagCompoundClass.newInstance();

        nBTTagCompoundSetStringMethod.invoke(tag, "id", "0");
        nBTTagCompoundSetIntMethod.invoke(tag, "x", location.getBlockX());
        nBTTagCompoundSetIntMethod.invoke(tag, "y", location.getBlockY());
        nBTTagCompoundSetIntMethod.invoke(tag, "z", location.getBlockZ());


        Object sanitizedLines = Array.newInstance(iChatBaseComponentClass, 4);
        for (int i = 0; i < 4; ++i) {
            if (i < lines.length && lines[i] != null) {
                Object val = Array.get(craftChatMessageFromStringMethod.invoke(null, lines[i]), 0);
                Array.set(sanitizedLines, i, val);
            } else {
                Object val = chatComponentTextClass.getConstructor(String.class).newInstance("");
                Array.set(sanitizedLines, i, val);
            }
        }


        for (int i = 0; i < 4; ++i) {
            Object s = chatSerializerAMethod.invoke(null, Array.get(sanitizedLines, i));
            nBTTagCompoundSetStringMethod.invoke(tag, "Text" + (i + 1), s);
        }

        return new PacketPlayOutTileEntityData(location, Action.SIGN_TEXT, tag);
    }
}
