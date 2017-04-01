package me.bpweber.practiceserver.player.GamePlayer.Commands;

import me.bpweber.practiceserver.money.Economy.*;
import me.bpweber.practiceserver.player.Stats.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class wipeAll implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("wipeall")) {
            if(sender.getName().equalsIgnoreCase("RedsEmporium") || sender.getName().equalsIgnoreCase("Kayaba"))
            {
                Player p = (Player) sender;
                for (UUID s : Economy.currentBalance.keySet()) {
                    Economy.currentBalance.put(s, 0);
                }
                for (UUID s : StatsMain.currentPlayerKills.keySet()) {
                    StatsMain.currentPlayerKills.put(s, 0);
                }
                for (UUID s : StatsMain.currentMonsterKills.keySet()) {
                    StatsMain.currentMonsterKills.put(s, 0);
                }
                Bukkit.broadcastMessage("Done!");
            }
        }


        return false;
    }


}
