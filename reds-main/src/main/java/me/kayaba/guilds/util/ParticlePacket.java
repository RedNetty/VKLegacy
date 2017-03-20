package me.kayaba.guilds.util;

import me.kayaba.guilds.api.util.reflect.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.reflect.*;
import org.apache.commons.lang.*;
import org.bukkit.*;
import org.bukkit.util.*;

import java.lang.reflect.*;


public class ParticlePacket extends AbstractPacket {
    protected static Class<?> packetClass;
    protected static Class<?> enumParticleClass;
    protected static Constructor<?> packetConstructor;
    protected static Field aField;
    protected static FieldAccessor<Float> bField;
    protected static FieldAccessor<Float> cField;
    protected static FieldAccessor<Float> dField;
    protected static FieldAccessor<Float> eField;
    protected static FieldAccessor<Float> fField;
    protected static FieldAccessor<Float> gField;
    protected static FieldAccessor<Float> hField;
    protected static FieldAccessor<Integer> iField;
    protected static FieldAccessor<Boolean> jField;
    protected static FieldAccessor<int[]> kField;

    static {
        try {
            packetClass = Reflections.getCraftClass(ConfigManager.getServerVersion().isOlderThan(ConfigManager.ServerVersion.MINECRAFT_1_7_R3)
                    ? "Packet63WorldParticles"
                    : "PacketPlayOutWorldParticles");

            if (ConfigManager.getServerVersion().isNewerThan(ConfigManager.ServerVersion.MINECRAFT_1_7_R4)) {
                enumParticleClass = Reflections.getCraftClass("EnumParticle");
                jField = Reflections.getField(packetClass, "j", boolean.class);
                kField = Reflections.getField(packetClass, "k", int[].class);
            }
            packetConstructor = packetClass.getConstructor();

            aField = Reflections.getPrivateField(packetClass, "a");
            bField = Reflections.getField(packetClass, "b", float.class);
            cField = Reflections.getField(packetClass, "c", float.class);
            dField = Reflections.getField(packetClass, "d", float.class);
            eField = Reflections.getField(packetClass, "e", float.class);
            fField = Reflections.getField(packetClass, "f", float.class);
            gField = Reflections.getField(packetClass, "g", float.class);
            hField = Reflections.getField(packetClass, "h", float.class);
            iField = Reflections.getField(packetClass, "i", int.class);
        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e) {
            LoggerUtils.exception(e);
        }
    }


    public ParticlePacket(Location center, ParticleEffect effect, float offsetX, float offsetY, float offsetZ, float speed, int amount, boolean longDistance, ParticleEffect.ParticleData data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Validate.isTrue(speed >= 0F, "Speed can't be lower than 0");
        Validate.isTrue(amount >= 0, "Amount can't be lower than 0");


        packet = packetConstructor.newInstance();

        if (ConfigManager.getServerVersion().isOlderThan(ConfigManager.ServerVersion.MINECRAFT_1_8_R1)) {
            String exception = effect.getName();
            if (data != null) {
                exception += data.toString();
            }

            aField.set(packet, exception);
        } else {
            aField.set(packet, enumParticleClass.getEnumConstants()[effect.getId()]);
            jField.set(packet, longDistance);

            if (data != null) {
                int[] exception1 = data.getPacketData();
                kField.set(packet, effect == ParticleEffect.ITEM_CRACK
                        ? exception1
                        : new int[]{exception1[0] | exception1[1] << 12});
            }
        }

        bField.set(packet, (float) center.getX());
        cField.set(packet, (float) center.getY());
        dField.set(packet, (float) center.getZ());
        eField.set(packet, offsetX);
        fField.set(packet, offsetY);
        gField.set(packet, offsetZ);
        hField.set(packet, speed);
        iField.set(packet, amount);
    }


    public ParticlePacket(Location center, ParticleEffect effect, Vector velocity, float speed, int amount, boolean longDistance, ParticleEffect.ParticleData data) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        this(center, effect, (float) velocity.getX(), (float) velocity.getY(), (float) velocity.getZ(), speed, amount, longDistance, data);
    }
}
