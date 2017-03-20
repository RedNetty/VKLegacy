package me.kayaba.guilds.impl.versionimpl.v1_8_R3.packet;

import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.reflect.*;
import org.bukkit.entity.*;

import java.lang.reflect.*;
import java.util.*;

public class PacketPlayOutPlayerInfo extends AbstractPacket {
    public enum EnumPlayerInfoAction {
        ADD_PLAYER,
        UPDATE_GAME_MODE,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER
    }

    protected static Class<?> enumPlayerInfoActionClass;
    protected static Class<?> packetPlayerOutPlayerInfoClass;
    protected static Constructor<?> packetPlayerOutPlayerInfoConstructor;

    protected final List<Object> list = new ArrayList<>();

    static {
        try {
            enumPlayerInfoActionClass = Reflections.getCraftClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
            packetPlayerOutPlayerInfoClass = Reflections.getCraftClass("PacketPlayOutPlayerInfo");
            packetPlayerOutPlayerInfoConstructor = packetPlayerOutPlayerInfoClass.getConstructor(
                    enumPlayerInfoActionClass,
                    Iterable.class
            );
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            LoggerUtils.exception(e);
        }
    }


    public PacketPlayOutPlayerInfo(EnumPlayerInfoAction action, Iterable<Player> players) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object craftEnum = Reflections.getEnumConstant(enumPlayerInfoActionClass, action.name());
        List<Object> list = new ArrayList<>();

        if (players != null) {
            for (Player player : players) {
                list.add(Reflections.getHandle(player));
            }
        }

        packet = packetPlayerOutPlayerInfoConstructor.newInstance(craftEnum, list);
    }


    public PacketPlayOutPlayerInfo(EnumPlayerInfoAction action, Player... players) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        this(action, Arrays.asList(players));
    }


    public PacketPlayOutPlayerInfo(EnumPlayerInfoAction action) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        this(action, new ArrayList<Player>());
    }
}
