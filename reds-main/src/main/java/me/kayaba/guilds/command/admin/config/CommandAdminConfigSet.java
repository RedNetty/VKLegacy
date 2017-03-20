package me.kayaba.guilds.command.admin.config;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

import java.util.*;

public class CommandAdminConfigSet extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        ConfigWrapper config = Config.fromPath(args[0]);

        if (config == null) {
            Message.CHAT_INVALIDPARAM.send(sender);
            return;
        }

        String valueString = StringUtils.join(StringUtils.parseArgs(args, 1), " ");
        Object value = valueString;

        if (valueString.equalsIgnoreCase("true")) {
            value = true;
        } else if (valueString.equalsIgnoreCase("false")) {
            value = false;
        } else if (NumberUtils.isNumeric(valueString)) {
            value = Integer.parseInt(valueString);
        } else if (valueString.startsWith("{") && valueString.endsWith("}")) {
            valueString = valueString.substring(1);
            valueString = valueString.substring(0, valueString.length() - 1);

            String[] split = {valueString};
            if (org.apache.commons.lang.StringUtils.contains(valueString, ";")) {
                split = org.apache.commons.lang.StringUtils.split(valueString, ";");
            }

            value = new ArrayList<>(Arrays.asList(split));
        } else if (valueString.startsWith("\"") && valueString.endsWith("\"")) {
            valueString = valueString.substring(1);
            valueString = valueString.substring(0, valueString.length() - 1);
            value = String.valueOf(valueString);
        }

        plugin.getConfigManager().set(config, value);
        TagUtils.refresh();
        TabUtils.refresh();

        Map<VarKey, String> vars = new HashMap<>();
        vars.put(VarKey.KEY, config.getName());
        vars.put(VarKey.VALUE, valueString);
        Message.CHAT_ADMIN_CONFIG_SET.clone().vars(vars).send(sender);
    }

    @Override
    protected Collection<String> tabCompleteOptions(CommandSender sender, String[] args) {
        return PracticeServer.getInstance().getConfigManager().getConfig().getKeys(true);
    }
}
