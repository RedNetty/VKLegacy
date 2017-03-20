package me.kayaba.guilds.command;

import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.Command;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

import java.util.*;

public class CommandGuilds extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        if (args.length == 0) {
            Map<String, String[]> langInfo = new HashMap<>();

            langInfo.put("en-en", new String[]{
                    Message.CHAT_PREFIX.get() + "Guild Information"
            });

            String[] info = langInfo.get(langInfo.containsKey(Config.LANG_NAME.getString().toLowerCase()) ? Config.LANG_NAME.getString().toLowerCase() : "en-en");

            for (String i : info) {
                sender.sendMessage(StringUtils.fixColors(i));
            }

            return;
        }

        switch (args[0].toLowerCase()) {
            //case "tool":
            //	Command.TOOL_GET.execute(sender, args);
            //	break;
            case "admin":
                Command.ADMIN_ACCESS.execute(sender, StringUtils.parseArgs(args, 1));
                break;
            /*
            case "group":
				Group group = GroupManager.getGroup(sender);

				if(args.length > 1) {
					group = GroupManager.getGroup(args[1]);
					if(group == null) {
						sender.sendMessage("Invalid group");
						return;
					}
				}

				sender.sendMessage("name = " + group.getName());
				sender.sendMessage("guildCreateMoney = " + group.get(GroupImpl.Key.CREATE_MONEY));
				sender.sendMessage("guildHomeMoney = " + group.get(GroupImpl.Key.HOME_MONEY));
				sender.sendMessage("guildJoinMoney = " + group.get(GroupImpl.Key.JOIN_MONEY));
				sender.sendMessage("guildCreateItems = " + group.get(GroupImpl.Key.CREATE_ITEMS).toString());
				sender.sendMessage("guildCreateSchematic = " + (group.get(GroupImpl.Key.CREATE_SCHEMATIC) == null ? "no schematic" : group.get(GroupImpl.Key.CREATE_SCHEMATIC).getName()));
				sender.sendMessage("guildHomeItems = " + group.get(GroupImpl.Key.HOME_ITEMS).toString());
				sender.sendMessage("guildJoinItems = " + group.get(GroupImpl.Key.JOIN_ITEMS).toString());
				sender.sendMessage("guildBuyLifeItems = " + group.get(GroupImpl.Key.BUY_LIFE_ITEMS).toString());
				sender.sendMessage("guildBuySlotItems = " + group.get(GroupImpl.Key.BUY_SLOT_ITEMS).toString());
				sender.sendMessage("guildBuyBannerItems = " + group.get(GroupImpl.Key.BUY_BANNER_ITEMS).toString());
				sender.sendMessage("guildBuyLifeMoney = " + group.get(GroupImpl.Key.BUY_LIFE_MONEY));
				sender.sendMessage("guildBuySlotMoney = " + group.get(GroupImpl.Key.BUY_SLOT_MONEY));
				sender.sendMessage("guildBuyBannerMoney = " + group.get(GroupImpl.Key.BUY_BANNER_MONEY));
				sender.sendMessage("guildEffectMoney = " + group.get(GroupImpl.Key.EFFECT_MONEY));
				sender.sendMessage("guildTeleportDelay = " + group.get(GroupImpl.Key.HOME_DELAY) + "s");
				sender.sendMessage("regionCreateMoney = " + group.get(GroupImpl.Key.REGION_CREATE_MONEY));
				sender.sendMessage("regionPricePerBlock = " + group.get(GroupImpl.Key.REGION_PRICEPERBLOCK));
				sender.sendMessage("regionAutoSize = " + group.get(GroupImpl.Key.REGION_AUTOSIZE));
				break; */
            case "g":
            case "guild":
                Command.GUILD_ACCESS.execute(sender, StringUtils.parseArgs(args, 1));
                break;
            case "tr":
                TabUtils.refresh();
                break;
            case "confirm":
                Command.CONFIRM.execute(sender, args);
                break;
            default:
                Message.CHAT_UNKNOWNCMD.send(sender);
                break;
        }
    }
}
