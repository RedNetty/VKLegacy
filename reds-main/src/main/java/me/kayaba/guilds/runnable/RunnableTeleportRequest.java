package me.kayaba.guilds.runnable;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public class RunnableTeleportRequest implements Runnable {
    private final Player player;
    private final Location location;
    private final Location startLocation;
    private final MessageWrapper message;


    public RunnableTeleportRequest(Player player, Location location, MessageWrapper message) {
        this.player = player;
        this.location = location;
        startLocation = this.player.getLocation();
        this.message = message;
    }

    @Override
    public void run() {
        if (player.getLocation().distance(startLocation) < 1) {
            player.teleport(location);
            message.send(player);
        } else {
            Message.CHAT_DELAYEDTPMOVED.send(player);
        }
    }
}
