package io.vawke.practice.guild.factory.command;

import io.vawke.practice.Game;
import io.vawke.practice.factory.FactoryObject;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Giovanni on 12-2-2017.
 */
public class GuildMotdCommand extends GuildCommand {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (Game.getGame().directGuildRegistry().hasGuild(player.getUniqueId())) {
                boolean guildOwner = Game.getGame().directGuildRegistry().ownerGuild(player) != null;
                if (strings.length > 0) {
                    if (guildOwner) {
                        StringBuilder stringBuilder = new StringBuilder(strings[0]);
                        for (int arg = 1; arg < strings.length; arg++) stringBuilder.append(" ").append(strings[arg]);

                        Game.getGame().directGuildRegistry().ownerGuild(player).setMotd(stringBuilder.toString());
                        player.sendMessage(ChatColor.GRAY + "You have updated the guild " + ChatColor.BOLD.toString() + ChatColor.DARK_AQUA + "MOTD" + ChatColor.GRAY + " to:");
                        Game.getGame().directGuildRegistry().ownerGuild(player).displayMotd(player);
                    } else {
                        player.sendMessage(ChatColor.RED + "You must be the " + ChatColor.BOLD + "GUILD OWNER" + ChatColor.RED + " to use " + ChatColor.BOLD + "/gmotd <motd>");
                        return false;
                    }
                } else {
                    Game.getGame().directGuildRegistry().playerGuild(player).displayMotd(player);
                }
            } else {
                player.sendMessage(ChatColor.RED + "You must be in a " + ChatColor.BOLD + "GUILD" + ChatColor.RED + " to view " + ChatColor.BOLD + "/gmotd");
                return false;
            }
        }
        return false;
    }

    @Override
    public void registerWatcher() {
        Game.getPracticeServer().getCommand("gmotd").setExecutor(this);
    }
}
