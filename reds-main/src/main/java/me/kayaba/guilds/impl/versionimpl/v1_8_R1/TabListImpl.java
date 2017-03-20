package me.kayaba.guilds.impl.versionimpl.v1_8_R1;

import com.google.common.collect.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.api.util.reflect.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.impl.versionimpl.v1_8_R1.packet.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.reflect.*;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.lang.reflect.*;
import java.util.*;

public class TabListImpl extends AbstractTabList {
    protected static Class<?> enumGamemodeClass;
    protected static Class<?> packetPlayerOutPlayerInfoClass;
    protected static Class<?> playerInfoDataClass;
    protected static Class<?> iChatBaseComponentClass;
    protected static Class<?> craftChatMessageClass;
    protected static Class<?> gameProfileClass;
    protected static Class<?> entityPlayerClass;
    protected static Class<?> craftOfflinePlayerClass;
    protected static Class<?> minecraftSessionServiceClass;
    protected static Class<?> minecraftServerClass;
    protected static Class<?> propertyMapClass;
    protected static Constructor<?> playerInfoDataConstructor;
    protected static Constructor<?> gameProfileConstructor;
    protected static Method craftChatMessageFromStringMethod;
    protected static Method minecraftServerGetMinecraftServerMethod;
    protected static Method craftOfflinePlayerGetProfileMethod;
    protected static Method entityPlayerGetProfileMethod;
    protected static Method minecraftSessionServiceFillProfilePropertiesMethod;
    protected static Method gameProfileGetPropertiesMethod;
    protected static MethodInvoker<Collection> propertyMapGetMethod;
    protected static Method propertyMapPutAllMethod;
    protected static Field packetPlayerOutPlayerInfoBField;
    protected static Field minecraftServerMinecraftSessionServiceField;

    protected final Object[] profiles = new Object[80];
    protected boolean first = true;
    protected static final String uuid = "00000000-0000-%s-0000-000000000000";
    protected static final String token = "!@#$^*";

    static {
        try {
            enumGamemodeClass = Reflections.getCraftClass("EnumGamemode");
            packetPlayerOutPlayerInfoClass = Reflections.getCraftClass("PacketPlayOutPlayerInfo");
            playerInfoDataClass = Reflections.getCraftClass("PlayerInfoData");
            iChatBaseComponentClass = Reflections.getCraftClass("IChatBaseComponent");
            craftChatMessageClass = Reflections.getBukkitClass("util.CraftChatMessage");
            gameProfileClass = Reflections.getClass("com.mojang.authlib.GameProfile");
            entityPlayerClass = Reflections.getCraftClass("EntityPlayer");
            craftOfflinePlayerClass = Reflections.getBukkitClass("CraftOfflinePlayer");
            minecraftSessionServiceClass = Reflections.getClass("com.mojang.authlib.minecraft.MinecraftSessionService");
            minecraftServerClass = Reflections.getCraftClass("MinecraftServer");
            propertyMapClass = Reflections.getClass("com.mojang.authlib.properties.PropertyMap");
            craftChatMessageFromStringMethod = Reflections.getMethod(craftChatMessageClass, "fromString", String.class);
            minecraftServerGetMinecraftServerMethod = Reflections.getMethod(minecraftServerClass, "getServer");
            craftOfflinePlayerGetProfileMethod = Reflections.getMethod(craftOfflinePlayerClass, "getProfile");
            entityPlayerGetProfileMethod = Reflections.getMethod(entityPlayerClass, "getProfile");
            minecraftSessionServiceFillProfilePropertiesMethod = Reflections.getMethod(minecraftSessionServiceClass, "fillProfileProperties");
            gameProfileGetPropertiesMethod = Reflections.getMethod(gameProfileClass, "getProperties");
            propertyMapGetMethod = Reflections.getMethod(propertyMapClass, Collection.class, "get");
            propertyMapPutAllMethod = Reflections.getMethod(propertyMapClass, "putAll", Multimap.class);
            packetPlayerOutPlayerInfoBField = Reflections.getPrivateField(packetPlayerOutPlayerInfoClass, "b");
            minecraftServerMinecraftSessionServiceField = Reflections.getPrivateField(minecraftServerClass, "W");

            playerInfoDataConstructor = playerInfoDataClass.getConstructor(
                    packetPlayerOutPlayerInfoClass,
                    gameProfileClass,
                    int.class,
                    enumGamemodeClass,
                    iChatBaseComponentClass
            );

            gameProfileConstructor = gameProfileClass.getConstructor(UUID.class, String.class);
        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e) {
            LoggerUtils.exception(e);
        }
    }

    public TabListImpl(GPlayer nPlayer) {
        super(nPlayer);
    }

    @Override
    public void send() {
        try {
            if (!getPlayer().isOnline()) {
                return;
            }

            TabUtils.fillVars(this);
            final List<Packet> packets = new ArrayList<>();

            Packet addPlayerPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
            Packet updateNamePacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME);
            List<Object> addPlayerList = new ArrayList<>();
            List<Object> updateNameList = new ArrayList<>();


            Object targetProfile = null;

            if (Bukkit.getOnlineMode()) {
                String name = Config.TABLIST_TEXTURE.getString();
                Player online = Bukkit.getPlayerExact(name);

                if (online != null) {
                    targetProfile = entityPlayerGetProfileMethod.invoke(Reflections.getHandle(online));
                } else {
                    targetProfile = craftOfflinePlayerGetProfileMethod.invoke(Bukkit.getOfflinePlayer(name));
                }

                if (Iterables.getFirst(propertyMapGetMethod.invoke(gameProfileGetPropertiesMethod.invoke(targetProfile), "textures"), null) == null) {
                    Object server = minecraftServerGetMinecraftServerMethod.invoke(null);
                    Object service = minecraftServerMinecraftSessionServiceField.get(server);
                    targetProfile = minecraftSessionServiceFillProfilePropertiesMethod.invoke(service, targetProfile, true);
                }
            }

            for (int i = 0; i < 80; i++) {
                String line;
                if (i < lines.size()) {
                    line = lines.get(i);
                } else {
                    line = "";
                }

                if (profiles[i] == null) {
                    profiles[i] = gameProfileConstructor.newInstance(
                            UUID.fromString(String.format(uuid, digit(i))),
                            token + digit(i)
                    );
                }

                line = StringUtils.replaceVarKeyMap(line, getVars());
                line = StringUtils.fixColors(line);

                Object gameProfile = profiles[i];
                Object gameMode = enumGamemodeClass.getEnumConstants()[0];
                Object lineCompound = Array.get(craftChatMessageFromStringMethod.invoke(null, line), 0);

                if (targetProfile != null) {
                    propertyMapPutAllMethod.invoke(gameProfileGetPropertiesMethod.invoke(gameProfile), gameProfileGetPropertiesMethod.invoke(targetProfile));
                }

                Object playerInfoData = playerInfoDataConstructor.newInstance(
                        null,
                        gameProfile,
                        0,
                        gameMode,
                        lineCompound
                );

                if (first) {
                    addPlayerList.add(playerInfoData);
                }

                updateNameList.add(playerInfoData);
            }

            if (first) {
                first = false;
            }

            packets.add(addPlayerPacket);
            packets.add(updateNamePacket);

            if (!Config.TABLIST_HEADER.isEmpty() || !Config.TABLIST_FOOTER.isEmpty()) {
                packets.add(new PacketPlayOutPlayerListHeaderFooter(Config.TABLIST_HEADER.getString(), Config.TABLIST_FOOTER.getString()));
            }

            packetPlayerOutPlayerInfoBField.set(addPlayerPacket.getPacket(), addPlayerList);
            packetPlayerOutPlayerInfoBField.set(updateNamePacket.getPacket(), updateNameList);
            PacketSender.sendPacket(getPlayer().getPlayer(), packets.toArray());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            LoggerUtils.exception(e);
        }
    }


    protected final String digit(int i) {
        return i > 9 ? "" + i : "0" + i;
    }
}
