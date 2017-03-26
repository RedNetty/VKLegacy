package me.bpweber.practiceserver.player.GamePlayer.Commands;

import me.bpweber.practiceserver.money.Economy.Economy;
import me.bpweber.practiceserver.player.GamePlayer.GameConfig;
import me.bpweber.practiceserver.player.Stats.StatsMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class wipeAll implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("wipeall") && sender.getName().equalsIgnoreCase("RedsEmporium")) {
            Player p = (Player) sender;
            for (String s : GameConfig.get().getKeys(false)) {
                GameConfig.get().set(s + ".Economy.Money Balance", 0);
                GameConfig.get().set(s + ".Stats.Monster Kills", 0);
                GameConfig.get().set(s + ".Stats.Player Kills", 0);
            }
            for (UUID s : Economy.currentBalance.keySet()) {
                Economy.currentBalance.remove(s);
            }
            for (UUID s : StatsMain.currentPlayerKills.keySet()) {
                StatsMain.currentPlayerKills.remove(s);
            }
            for (UUID s : StatsMain.currentMonsterKills.keySet()) {
                StatsMain.currentMonsterKills.remove(s);
            }
            for (UUID s : StatsMain.currentMonsterKills.keySet()) {
                StatsMain.currentMonsterKills.remove(s);
            }

        }


        return false;
    }


}
