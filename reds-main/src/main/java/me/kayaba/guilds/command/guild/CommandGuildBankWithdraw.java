package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandGuildBankWithdraw extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        if (args.length != 1) {
            Message.CHAT_GUILD_BANK_ENTERAMOUNT.send(sender);
            return;
        }

        String moneyString = args[0];
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if (!nPlayer.hasGuild()) {
            Message.CHAT_GUILD_NOTINGUILD.send(sender);
            return;
        }

        Guild guild = nPlayer.getGuild();

        if (!nPlayer.hasPermission(GuildPermission.BANK_WITHDRAW)) {
            Message.CHAT_GUILD_NOGUILDPERM.send(sender);
            return;
        }

        if (!NumberUtils.isNumeric(moneyString)) {
            Message.CHAT_ENTERINTEGER.send(sender);
            return;
        }

        double money = Double.parseDouble(moneyString);
        money = NumberUtils.roundOffTo2DecPlaces(money);

        if (money < 0) {
            Message.CHAT_BASIC_NEGATIVENUMBER.send(sender);
            return;
        }

        if (guild.getMoney() < money) {
            Message.CHAT_GUILD_BANK_WITHDRAW_NOTENOUGH.send(sender);
            return;
        }

        guild.takeMoney(money);
        nPlayer.addMoney(money);
        Message.CHAT_GUILD_BANK_WITHDRAW_SUCCESS.clone().setVar(VarKey.AMOUNT, money).send(sender);
        TabUtils.refresh(nPlayer.getGuild());
    }
}
