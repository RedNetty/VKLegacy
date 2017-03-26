package me.bpweber.practiceserver.player.GamePlayer;

import me.bpweber.practiceserver.PracticeServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by jaxon on 3/23/2017.
 */
public class GamePlayer implements Listener {


    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, PracticeServer.plugin);
        GameConfig.setup();
        GameConfig.get().options().copyDefaults(true);
        GameConfig.save();

    }


    public boolean playerExists(Player p) {
        return GameConfig.get().contains(p.getUniqueId().toString());
    }

    @EventHandler
    public void onJoinCheck(PlayerJoinEvent e) {
        if(!GameConfig.get().contains(e.getPlayer().getUniqueId() + ".Main.ZCash")) {
            GameConfig.get().set(e.getPlayer().getUniqueId() + ".Main.ZCash", 0);
        }
        if (playerExists(e.getPlayer())) {
            return;
        } else {
            PracticeServer.plugin.getServer().getScheduler().scheduleSyncDelayedTask(PracticeServer.plugin, new Runnable() {
                @Override
                public void run() {
                    Player p = e.getPlayer();
                    GameConfig.get().set(p.getUniqueId() + ".Info.Username", p.getName());
                    GameConfig.get().set(p.getUniqueId() + ".Info.IP Address", p.getAddress().toString());
                    GameConfig.get().set(p.getUniqueId() + ".Main.Rank", "Default");
                    GameConfig.get().set(p.getUniqueId() + ".Main.Banned", 0);
                    GameConfig.get().set(p.getUniqueId() + ".Main.Muted", 0);
                    GameConfig.get().set(p.getUniqueId() + ".Economy.Money Balance", 0);
                    GameConfig.get().set(p.getUniqueId() + ".Economy.Elite Shards", 0);
                    GameConfig.get().set(p.getUniqueId() + ".Stats.Monster Kills", 0);
                    GameConfig.get().set(p.getUniqueId() + ".Stats.Player Kills", 0);
                    GameConfig.save();
                }
            }, 50);

        }
    }

}
