package io.vawke.practice.guild.factory.command;

import io.vawke.practice.Game;
import io.vawke.practice.factory.FactoryObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Giovanni on 12-2-2017.
 */
public class GuildInfoCommand extends GuildCommand {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (Game.getGame().directGuildRegistry().hasGuild(player.getUniqueId())) {
                Game.getGame().directGuildRegistry().playerGuild(player).showGuildInfo(player);
                return true;
            }
        }
        return false;
    }

    @Override
    public void registerWatcher() {
        Game.getPracticeServer().getCommand("ginfo").setExecutor(this);
    }
}
