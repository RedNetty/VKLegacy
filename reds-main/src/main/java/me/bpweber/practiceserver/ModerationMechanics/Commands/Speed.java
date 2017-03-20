package me.bpweber.practiceserver.ModerationMechanics.Commands;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class Speed implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        float speed = 0.0F;
        int argu = Integer.parseInt(args[0]);
        if (cmd.getName().equalsIgnoreCase("speed") && p.isOp()) {
            switch (argu) {
                case 1:
                    speed = 0.1F;
                    break;
                case 2:
                    speed = 0.2F;
                    break;
                case 3:
                    speed = 0.3F;
                    break;
                case 4:
                    speed = 0.4F;
                    break;
                case 5:
                    speed = 0.5F;
                    break;
                case 6:
                    speed = 0.6F;
                    break;
                case 7:
                    speed = 0.7F;
                    break;
                case 8:
                    speed = 0.8F;
                    break;
                case 9:
                    speed = 0.9F;
                    break;
                case 10:
                    speed = 1.0F;
                    break;
                default:
                    speed = 0.1F;
                    break;
            }
            if (speed <= 1.0F) {
                if (p.isFlying()) {
                    p.setFlySpeed(speed);
                } else {
                    p.setWalkSpeed(speed);
                }
            } else {
                p.sendMessage(ChatColor.RED + "Error, Max speed is 10.");
            }

        }
        return false;
    }
}