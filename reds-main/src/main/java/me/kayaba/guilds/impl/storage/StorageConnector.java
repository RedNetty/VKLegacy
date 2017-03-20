package me.kayaba.guilds.impl.storage;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.exception.*;
import me.kayaba.guilds.util.*;

import java.io.*;

public class StorageConnector {
    private static final PracticeServer plugin = PracticeServer.getInstance();
    private final DataStorageType dataStorageType;
    private int storageConnectionAttempt = 1;
    private Storage storage;


    public StorageConnector(DataStorageType dataStorageType) throws StorageConnectionFailedException {
        this.dataStorageType = dataStorageType;
        handle();
    }


    public void handle() throws StorageConnectionFailedException {
        try {
            connect();
        } catch (StorageConnectionFailedException e) {
            storageConnectionAttempt++;

            if (e.getMessage() != null && e.getMessage().contains("credentials")) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }

            LoggerUtils.exception(e);

            if (storageConnectionAttempt > 3) {
                throw e;
            }

            handle();
        }
    }


    public void connect() throws StorageConnectionFailedException {
        LoggerUtils.info("Connecting to " + dataStorageType.name() + " storage (attempt: " + storageConnectionAttempt + ")");

        switch (dataStorageType) {
            case MYSQL:
                if (Config.MYSQL_HOST.getString().isEmpty()) {
                    throw new StorageConnectionFailedException("MySQL credentials not specified in the config. Switching to secondary storage.");
                }

                storage = new MySQLStorageImpl(
                        Config.MYSQL_HOST.getString(),
                        Config.MYSQL_PORT.getString(),
                        Config.MYSQL_DATABASE.getString(),
                        Config.MYSQL_USERNAME.getString(),
                        Config.MYSQL_PASSWORD.getString()
                );
                break;
            case FUNNYGUILDS_MYSQL:
                LoggerUtils.info("Please change the table prefix to a valid one with");
                LoggerUtils.info(" /ga config set mysql.prefix 'prefix'");
                LoggerUtils.info("It's empty by default (use '')");

                if (Config.MYSQL_HOST.getString().isEmpty()) {
                    throw new StorageConnectionFailedException("Cannot connect to the storage.");
                }

                storage = new me.kayaba.guilds.impl.storage.funnyguilds.MySQLStorageImpl(
                        Config.MYSQL_HOST.getString(),
                        Config.MYSQL_PORT.getString(),
                        Config.MYSQL_DATABASE.getString(),
                        Config.MYSQL_USERNAME.getString(),
                        Config.MYSQL_PASSWORD.getString()
                );
                break;
            case SQLITE:
                storage = new SQLiteStorageImpl(new File(plugin.getDataFolder(), "sqlite.db"));
                break;
            case FLAT:
                storage = new YamlStorageImpl(new File(plugin.getDataFolder(), "data/"));
                break;
            case FUNNYGUILDS_FLAT:
                storage = new me.kayaba.guilds.impl.storage.funnyguilds.YamlStorageImpl(new File(plugin.getDataFolder(), "../FunnyGuilds/data/"));
                break;
        }

        storage.registerManagers();
        LoggerUtils.info("Successfully connected to the storage");
    }


    public Storage getStorage() {
        return storage;
    }
}
