package me.bpweber.practiceserver.world.Tips;

import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.player.Toggles;
import me.bpweber.practiceserver.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jaxon on 5/1/2017.
 */
public class TipsMain {

    public void onEnable() {
        TipsConfig.setup();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PracticeServer.plugin, new Runnable() {
            @Override
            public void run() {
                Random r = new Random();
                List<String> configList = (List<String>)TipsConfig.get().getStringList("Tips List") ;
                String msg = configList.get(r.nextInt(configList.size()));
                for(Player p : Bukkit.getOnlinePlayers()) {
                    ArrayList<String> toggles = Toggles.getToggles(p.getName());
                    if(toggles.contains("tips")) {
                        p.sendMessage(ChatColor.YELLOW + ">>> " + ChatColor.BOLD + "TIP" + ChatColor.YELLOW + ": " + ChatColor.GRAY + msg);
                    }

                }


            }
        },0L, 2500L);

    }
    public void onDisable() {

    }

}
