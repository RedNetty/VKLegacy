package me.kayaba.guilds.command.admin;

import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.storage.*;
import org.bukkit.command.*;

import java.util.*;

public class CommandAdminMigrate extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        if (args.length != 2) {
            getCommand().getUsageMessage().send(sender);
            return;
        }

        DataStorageType dataStorageType;
        try {
            dataStorageType = DataStorageType.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            Message.CHAT_ADMIN_MIGRATE_INVALIDTYPE.send(sender);
            return;
        }

        if (dataStorageType == plugin.getConfigManager().getDataStorageType()) {
            Message.CHAT_ADMIN_MIGRATE_SAMETYPE.send(sender);
            return;
        }

        if (!args[0].equalsIgnoreCase("from") && !args[0].equalsIgnoreCase("to")) {
            Message.CHAT_INVALIDPARAM.send(sender);
            return;
        }

        Storage storage = new StorageConnector(dataStorageType).getStorage();

        Storage toStorage, fromStorage;
        if (args[0].equalsIgnoreCase("from")) {
            fromStorage = storage;
            toStorage = plugin.getStorage();
        } else {
            fromStorage = plugin.getStorage();
            toStorage = storage;
        }

        toStorage.registerManagers();
        Migrant migrant = new MigrantImpl(fromStorage, toStorage);
        migrant.migrate();
        migrant.save();

        Message.CHAT_ADMIN_MIGRATE_SUCCESS.send(sender);
    }

    @Override
    protected Collection<String> tabCompleteOptions(CommandSender sender, String[] args) {
        Set<String> set = new HashSet<>();

        if (args.length == 1) {
            set.add("from");
            set.add("to");
        } else if (args.length == 2) {
            for (DataStorageType dataStorageType : DataStorageType.values()) {
                set.add(dataStorageType.name());
            }
        }

        return set;
    }
}
