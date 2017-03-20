package me.kayaba.guilds.impl.versionimpl.v1_8_R1.packet;

import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.reflect.*;

import java.lang.reflect.*;

@SuppressWarnings("ConstantConditions")
public class PacketPlayOutTitle extends AbstractPacket {
    protected static Class<?> packetTitleClass;
    protected static Class<?> titleActionsClass;
    protected static Class<?> chatSerializerClass;
    protected static Class<?> chatBaseComponentClass;

    public enum EnumTitleAction {
        TITLE(0),
        SUBTITLE(1),
        TIMES(2),
        CLEAR(3),
        RESET(4);

        private final Object[] actions = titleActionsClass.getEnumConstants();
        private final Object action;


        EnumTitleAction(int id) {
            action = actions[id];
        }


        public Object getCraftAction() {
            return action;
        }
    }

    static {
        try {
            packetTitleClass = Reflections.getCraftClass("PacketPlayOutTitle");
            titleActionsClass = Reflections.getCraftClass("EnumTitleAction");
            chatSerializerClass = Reflections.getCraftClass("ChatSerializer");
            chatBaseComponentClass = Reflections.getCraftClass("IChatBaseComponent");
        } catch (Exception e) {
            LoggerUtils.exception(e);
        }
    }


    public PacketPlayOutTitle(EnumTitleAction action, String json) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        this(action, json, -1, -1, -1);
    }


    public PacketPlayOutTitle(EnumTitleAction action, String json, int fadeIn, int stay, int fadeOut) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Object serialized = null;


        if (json != null) {
            serialized = Reflections.getMethod(chatSerializerClass, "a", String.class).invoke(null, json);
        }

        packet = packetTitleClass.getConstructor(titleActionsClass, chatBaseComponentClass, Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(action.getCraftAction(), serialized, fadeIn, stay, fadeOut);
    }
}
