package me.kayaba.guilds.impl.storage;

import me.kayaba.guilds.exception.*;
import me.kayaba.guilds.util.*;

import java.io.*;

public abstract class AbstractFileStorage extends AbstractStorage {
    private final File dataDirectory;
    private final File playersDirectory;
    private final File guildsDirectory;
    private final File regionsDirectory;
    private final File ranksDirectory;


    public AbstractFileStorage(File dataDirectory) throws StorageConnectionFailedException {
        this.dataDirectory = dataDirectory;
        playersDirectory = new File(dataDirectory, "player/");
        guildsDirectory = new File(dataDirectory, "guild/");
        regionsDirectory = new File(dataDirectory, "region/");
        ranksDirectory = new File(dataDirectory, "rank/");

        if (!setUp()) {
            throw new StorageConnectionFailedException("Failed creating directories");
        }
    }

    @Override
    public boolean setUp() {
        if (!dataDirectory.exists()) {
            if (dataDirectory.mkdir()) {
                LoggerUtils.info("Data directory created");
            }
        }

        if (dataDirectory.exists()) {
            if (!playersDirectory.exists()) {
                if (playersDirectory.mkdir()) {
                    LoggerUtils.info("Players directory created");
                }
            }

            if (!guildsDirectory.exists()) {
                if (guildsDirectory.mkdir()) {
                    LoggerUtils.info("Guilds directory created");
                }
            }

            if (!regionsDirectory.exists()) {
                if (regionsDirectory.mkdir()) {
                    LoggerUtils.info("Regions directory created");
                }
            }

            if (!ranksDirectory.exists()) {
                if (ranksDirectory.mkdir()) {
                    LoggerUtils.info("Ranks directory created");
                }
            }
        } else {
            LoggerUtils.error("Could not setup directories!");
            LoggerUtils.error("Switching to secondary data storage type!");
            return false;
        }

        return true;
    }


    public final File getDirectory() {
        return dataDirectory;
    }
}
