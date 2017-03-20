package me.kayaba.guilds.command.admin.config;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import org.apache.commons.lang.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.*;

import java.util.*;

public class CommandAdminConfigGet extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        if (args.length == 0) {
            getCommand().getUsageMessage().send(sender);
            return;
        }

        String path = args[0];
        String value = "";
        Map<VarKey, String> vars = new HashMap<>();
        FileConfiguration config = plugin.getConfigManager().getConfig();

        if (!config.contains(path)) {
            Message.CHAT_INVALIDPARAM.send(sender);
            return;
        }

        if (config.isConfigurationSection(path)) {
            int depth = 1;
            String lastSection = null;

            vars.put(VarKey.DEPTH, "");
            vars.put(VarKey.KEY, path);
            Message.CHAT_ADMIN_CONFIG_GET_LIST_SECTION.clone().vars(vars).send(sender);

            for (String string : config.getConfigurationSection(path).getKeys(true)) {
                String[] prefixSplit = StringUtils.split(string, ".");
                String prefix = StringUtils.contains(string, ".") ? StringUtils.removeEnd(string, "." + prefixSplit[prefixSplit.length - 1]) : string;

                if (lastSection != null && !prefix.startsWith(lastSection)) {
                    depth--;
                    lastSection = null;
                }

                String space = "";
                for (int i = 0; i < depth; i++) {
                    space += " ";
                }
                vars.put(VarKey.DEPTH, space);

                if (config.isConfigurationSection(path + "." + string)) {
                    depth++;
                    lastSection = string;

                    vars.put(VarKey.KEY, prefixSplit[prefixSplit.length - 1]);
                    Message.CHAT_ADMIN_CONFIG_GET_LIST_SECTION.clone().vars(vars).send(sender);
                } else {
                    vars.put(VarKey.KEY, StringUtils.removeStart(string, prefix + "."));
                    Message.CHAT_ADMIN_CONFIG_GET_LIST_KEY.clone().vars(vars).send(sender);
                }
            }
        } else {
            if (config.isList(path)) {
                value = StringUtils.join(config.getStringList(path), " ");
            } else {
                value = config.getString(path);
            }
        }

        vars.put(VarKey.KEY, path);
        vars.put(VarKey.VALUE, value);

        if (!value.isEmpty()) {
            Message.CHAT_ADMIN_CONFIG_GET_SINGLE.clone().vars(vars).send(sender);
        }
    }

    @Override
    protected Collection<String> tabCompleteOptions(CommandSender sender, String[] args) {
        return PracticeServer.getInstance().getConfigManager().getConfig().getKeys(true);
    }
}
