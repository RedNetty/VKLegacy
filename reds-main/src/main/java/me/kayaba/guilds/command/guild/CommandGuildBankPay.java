package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandGuildBankPay extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if (!nPlayer.hasGuild()) {
            Message.CHAT_GUILD_NOTINGUILD.send(sender);
            return;
        }

        Guild guild = nPlayer.getGuild();

        if (!nPlayer.hasPermission(GuildPermission.BANK_PAY)) {
            Message.CHAT_GUILD_NOGUILDPERM.send(sender);
            return;
        }

        if (args.length == 0 || !NumberUtils.isNumeric(args[0])) {
            Message.CHAT_GUILD_BANK_ENTERAMOUNT.send(sender);
            return;
        }

        Double money = Double.parseDouble(args[0]);

        money = NumberUtils.roundOffTo2DecPlaces(money);

        if (money < 0) {
            Message.CHAT_BASIC_NEGATIVENUMBER.send(sender);
            return;
        }

        if (!nPlayer.hasMoney(money)) {
            Message.CHAT_GUILD_BANK_PAY_NOTENOUGH.send(sender);
            return;
        }

        nPlayer.takeMoney(money);
        guild.addMoney(money);
        Message.CHAT_GUILD_BANK_PAY_PAID.clone().setVar(VarKey.AMOUNT, money).send(sender);
        TabUtils.refresh(nPlayer.getGuild());
    }
}
