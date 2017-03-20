package me.kayaba.guilds.command.admin.player;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;


public class CommandAdminPlayerSetPoints extends AbstractCommandExecutor.Reversed<GPlayer> {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        GPlayer nPlayer = getParameter();

        if (args.length != 1) {
            getCommand().getUsageMessage().send(sender);
            return;
        }

        String points = args[0];
        int pointsInteger = 0;

        boolean subtract = points.startsWith("-=");
        if (points.startsWith("+=") || subtract) {
            pointsInteger = nPlayer.getPoints();
            points = points.substring(2);

            if (subtract) {
                points = "-" + points;
            }
        }

        if (!NumberUtils.isNumeric(points)) {
            Message.CHAT_ENTERINTEGER.send(sender);
            return;
        }

        pointsInteger += Integer.parseInt(points);

        if (pointsInteger < 0) {
            Message.CHAT_BASIC_NEGATIVENUMBER.send(sender);
            return;
        }

        nPlayer.setPoints(pointsInteger);
        TabUtils.refresh(nPlayer);
        Message.CHAT_ADMIN_PLAYER_SET_POINTS
                .clone()
                .setVar(VarKey.PLAYER_NAME, nPlayer.getName())
                .setVar(VarKey.PLAYER_POINTS, nPlayer.getPoints())
                .send(sender);
    }
}
