package me.kayaba.guilds.impl.versionimpl.v1_8_R1;

import me.kayaba.guilds.api.event.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.impl.util.signgui.*;
import me.kayaba.guilds.impl.versionimpl.v1_8_R1.packet.PacketPlayInUpdateSign;
import me.kayaba.guilds.impl.versionimpl.v1_8_R1.packet.PacketPlayOutBlockChange;
import me.kayaba.guilds.impl.versionimpl.v1_8_R1.packet.*;
import me.kayaba.guilds.impl.versionimpl.v1_9_R2.packet.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.reflect.*;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.lang.reflect.*;
import java.util.*;

@SuppressWarnings("ConstantConditions")
public class SignGUIImpl extends AbstractSignGui {

    public SignGUIImpl() {
        registerUpdateHandling();
    }

    @Override
    public void open(Player player, String[] defaultText, SignGUIListener response) {
        try {
            final List<Packet> packets = new ArrayList<>();
            Location location = player.getLocation().clone();
            location.setY(0);

            if (defaultText != null) {
                packets.add(new PacketPlayOutBlockChange(location, Material.SIGN_POST, 0));
                packets.add(new PacketPlayOutUpdateSign(location, defaultText));
            }

            packets.add(new PacketPlayOutOpenSignEditor(location));

            if (defaultText != null) {
                packets.add(new PacketPlayOutBlockChange(location, null, 0));
            }

            signLocations.put(player.getUniqueId(), location);
            listeners.put(player.getUniqueId(), response);
            PacketSender.sendPacket(player, packets.toArray(new Packet[packets.size()]));
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            LoggerUtils.exception(e);
        }
    }


    protected void registerUpdateHandling() {
        new AbstractPacketHandler("PacketPlayInUpdateSign") {
            @Override
            public void handle(PacketReceiveEvent event) {
                try {
                    final PacketPlayInUpdateSign packetPlayInUpdateSign = new PacketPlayInUpdateSign(event.getPacket());
                    final Player player = event.getPlayer();
                    Location v = getSignLocations().remove(player.getUniqueId());

                    if (v == null
                            || packetPlayInUpdateSign.getBlockPositionWrapper().getX() != v.getBlockX()
                            || packetPlayInUpdateSign.getBlockPositionWrapper().getY() != v.getBlockY()
                            || packetPlayInUpdateSign.getBlockPositionWrapper().getZ() != v.getBlockZ()) {
                        return;
                    }

                    final SignGUIListener response = getListeners().remove(player.getUniqueId());

                    if (response != null) {
                        event.setCancelled(true);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            public void run() {
                                response.onSignDone(player, packetPlayInUpdateSign.getLines());
                            }
                        });
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LoggerUtils.exception(e);
                }
            }
        };
    }
}
