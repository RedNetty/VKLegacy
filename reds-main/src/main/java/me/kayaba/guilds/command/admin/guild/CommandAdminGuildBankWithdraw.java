package me.kayaba.guilds.command.admin.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

import java.util.*;

public class CommandAdminGuildBankWithdraw extends AbstractCommandExecutor.Reversed<Guild> {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        Guild guild = getParameter();

        if (args.length != 1) {
            getCommand().getUsageMessage().send(sender);
            return;
        }

        String moneyString = args[0];

        if (!NumberUtils.isNumeric(moneyString)) {
            Message.CHAT_ENTERINTEGER.send(sender);
            return;
        }

        double money = Double.parseDouble(moneyString);

        if (money < 0) {
            Message.CHAT_BASIC_NEGATIVENUMBER.send(sender);
            return;
        }

        if (guild.getMoney() < money) {
            Message.CHAT_GUILD_BANK_WITHDRAW_NOTENOUGH.send(sender);
            return;
        }

        money = NumberUtils.roundOffTo2DecPlaces(money);

        guild.takeMoney(money);
        TabUtils.refresh(guild);

        Map<VarKey, String> vars = new HashMap<>();
        vars.put(VarKey.MONEY, moneyString);
        vars.put(VarKey.GUILD_NAME, guild.getName());
        Message.CHAT_ADMIN_GUILD_BANK_WITHDREW.clone().vars(vars).send(sender);
    }
}
