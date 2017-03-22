package me.kayaba.guilds.impl.versionimpl.v1_8_R3;

import io.netty.channel.*;
import me.kayaba.guilds.api.event.PacketReceiveEvent;
import me.kayaba.guilds.api.event.PacketSendEvent;
import me.kayaba.guilds.api.util.packet.PacketExtension;
import me.kayaba.guilds.api.util.reflect.FieldAccessor;
import me.kayaba.guilds.manager.ListenerManager;
import me.kayaba.guilds.util.LoggerUtils;
import me.kayaba.guilds.util.reflect.Reflections;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressWarnings("ConstantConditions")
public class PacketExtensionImpl implements PacketExtension {
    protected static FieldAccessor<Channel> clientChannelField;
    protected static Field networkManagerField;
    protected static Field playerConnectionField;
    protected static Method handleMethod;
    protected static Method sendPacketMethod;
    protected static Class<?> packetClass;
    protected static Class<?> craftPlayerClass;
    protected static Class<?> entityPlayerClass;
    protected static Class<?> playerConnectionClass;
    protected static Class<?> craftEntityClass;
    protected static Class<?> networkManagerClass;

    static {
        try {
            networkManagerClass = Reflections.getCraftClass("NetworkManager");
            playerConnectionClass = Reflections.getCraftClass("PlayerConnection");
            craftEntityClass = Reflections.getBukkitClass("entity.CraftEntity");
            packetClass = Reflections.getCraftClass("Packet");
            craftPlayerClass = Reflections.getBukkitClass("entity.CraftPlayer");
            entityPlayerClass = Reflections.getCraftClass("EntityPlayer");
            handleMethod = Reflections.getMethod(craftEntityClass, "getHandle");
            sendPacketMethod = Reflections.getMethod(playerConnectionClass, "sendPacket", packetClass);
            playerConnectionField = Reflections.getField(entityPlayerClass, "playerConnection");
            clientChannelField = Reflections.getField(networkManagerClass, Channel.class, 0);
            networkManagerField = Reflections.getField(playerConnectionClass, "networkManager");
        } catch (Exception e) {
            LoggerUtils.exception(e);
        }
    }


    private static Channel getChannel(Player player) {
        try {
            Object eP = handleMethod.invoke(player);
            return clientChannelField.get(networkManagerField.get(playerConnectionField.get(eP)));
        } catch (Exception e) {
            LoggerUtils.exception(e);
            return null;
        }
    }

    @Override
    public void registerPlayer(final Player player) {
        Channel c = getChannel(player);
        ChannelHandler handler = new ChannelDuplexHandler() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                PacketSendEvent event = new PacketSendEvent(msg, player);
                ListenerManager.getLoggedPluginManager().callEvent(event);

                if (event.isCancelled() || event.getPacket() == null) {
                    return;
                }

                super.write(ctx, msg, promise);
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                try {
                    if (msg == null) {
                        return;
                    }

                    PacketReceiveEvent event = new PacketReceiveEvent(msg, player);
                    ListenerManager.getLoggedPluginManager().callEvent(event);

                    if (event.isCancelled() || event.getPacket() == null) {
                        return;
                    }
                    super.channelRead(ctx, event.getPacket());
                } catch (Exception e) {
                    super.channelRead(ctx, msg);
                }
            }
        };

        ChannelPipeline cp = c.pipeline();
        if (cp.names().contains("packet_handler")) {
            if (cp.names().contains("Guilds")) {
                cp.replace("Guilds", "Guilds", handler);
            } else {
                cp.addBefore("packet_handler", "KayabaGuilds", handler);
            }
        }
    }

    @Override
    public void unregisterChannel() {

    }

    @Override
    public void sendPacket(Player player, Object... packets) {
        try {
            Object handle = Reflections.getHandle(player);
            Object playerConnection = playerConnectionField.get(handle);

            for (Object packet : packets) {
                if (packet == null) {
                    continue;
                }

                if (!packetClass.isInstance(packet)) {
                    throw new IllegalArgumentException("Argument Type missmatch. Expected: " + packetClass.getName() + " got " + packet.getClass());
                }

                sendPacketMethod.invoke(playerConnection, packet);
            }
        } catch (Exception e) {
            LoggerUtils.exception(e);
        }
    }
}
