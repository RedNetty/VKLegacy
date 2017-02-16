package io.vawke.practice.guild.factory.command;

import io.vawke.practice.Game;
import io.vawke.practice.factory.FactoryObject;
import io.vawke.practice.guild.Guild;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Giovanni on 12-2-2017.
 */
public class GuildTestCommand extends GuildCommand {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.getName().equalsIgnoreCase("vawkenetty")) {
                if (Game.getGame().directGuildRegistry().tryName(player, "testGuild")) {
                    Guild guild = new Guild("testGuild", "TGV", player);
                    guild.register();
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public void registerWatcher() {
        Game.getPracticeServer().getCommand("gtest").setExecutor(this);
    }
}

