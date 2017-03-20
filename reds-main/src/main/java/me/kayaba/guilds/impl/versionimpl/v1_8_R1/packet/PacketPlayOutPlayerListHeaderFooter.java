package me.kayaba.guilds.impl.versionimpl.v1_8_R1.packet;

import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.reflect.*;

import java.lang.reflect.*;

public class PacketPlayOutPlayerListHeaderFooter extends AbstractPacket {
    protected static Class<?> PacketPlayOutPlayerListHeaderFooterClass;
    protected static Class<?> craftChatMessageClass;
    protected static Method craftChatMessageFromStringMethod;
    protected static Field PacketPlayOutPlayerListHeaderFooterBField;
    protected static Field PacketPlayOutPlayerListHeaderFooterAField;

    static {
        try {
            craftChatMessageClass = Reflections.getBukkitClass("util.CraftChatMessage");
            craftChatMessageFromStringMethod = Reflections.getMethod(craftChatMessageClass, "fromString", String.class);
            PacketPlayOutPlayerListHeaderFooterClass = Reflections.getCraftClass("PacketPlayOutPlayerListHeaderFooter");
            PacketPlayOutPlayerListHeaderFooterAField = Reflections.getPrivateField(PacketPlayOutPlayerListHeaderFooterClass, "a");
            PacketPlayOutPlayerListHeaderFooterBField = Reflections.getPrivateField(PacketPlayOutPlayerListHeaderFooterClass, "b");
        } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException e) {
            LoggerUtils.exception(e);
        }
    }


    public PacketPlayOutPlayerListHeaderFooter(String header, String footer) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object[] iChatBaseComponentHeader = (Object[]) craftChatMessageFromStringMethod.invoke(null, StringUtils.fixColors(header));
        Object[] iChatBaseComponentFooter = (Object[]) craftChatMessageFromStringMethod.invoke(null, StringUtils.fixColors(footer));
        packet = PacketPlayOutPlayerListHeaderFooterClass.newInstance();
        PacketPlayOutPlayerListHeaderFooterAField.set(packet, iChatBaseComponentHeader[0]);
        PacketPlayOutPlayerListHeaderFooterBField.set(packet, iChatBaseComponentFooter[0]);
    }
}
