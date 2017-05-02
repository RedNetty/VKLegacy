package me.bpweber.practiceserver.world.Tips;

import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by jaxon on 5/1/2017.
 */
public class TipsMain {

    public void onEnable() {
        TipsConfig.setup();
        if(!TipsConfig.get().contains("Tip List")) {
            TipsConfig.get().addDefault("Tips List", "");
            List<String> configList = (List<String>)TipsConfig.get().getList("Tips List");
            configList.add("Default Tip");
            TipsConfig.get().set("Tips List", configList);
            TipsConfig.save();
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PracticeServer.plugin, new Runnable() {
            @Override
            public void run() {
                String msg;
                List<String> configList = (List<String>)TipsConfig.get().getList("Tips List");
                int msgn = Util.random.nextInt(configList.size());
                for(Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(ChatColor.YELLOW + ">>> TIP: " + ChatColor.GRAY +  configList.get(msgn));

                }


            }
        },0L, 3600L);

    }
    public void onDisable() {

    }

}
