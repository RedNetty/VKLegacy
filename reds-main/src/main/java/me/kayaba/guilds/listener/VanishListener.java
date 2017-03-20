package me.kayaba.guilds.listener;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.kitteh.vanish.event.*;

public class VanishListener extends AbstractListener {

    @EventHandler
    public void onVanishStatusChange(VanishStatusChangeEvent event) {
        Player player = event.getPlayer();

        if (event.isVanishing()) {
            GPlayer nPlayer = PlayerManager.getPlayer(player);

            if (nPlayer.getAtRegion() != null) {
                plugin.getRegionManager().playerExitedRegion(player);
            }
        } else {
            GRegion region = RegionManager.get(player);
            if (region != null) {
                plugin.getRegionManager().playerEnteredRegion(player, region);
            }
        }
    }
}
