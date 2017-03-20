package me.kayaba.guilds.impl.storage.managers.file.yaml;

import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.impl.storage.managers.file.*;
import org.bukkit.configuration.file.*;

import java.io.*;

public abstract class AbstractYAMLResourceManager<T extends Resource> extends AbstractFileResourceManager<T> {

    protected AbstractYAMLResourceManager(Storage storage, Class clazz, String directoryPath) {
        super(storage, clazz, directoryPath);
    }


    protected FileConfiguration getData(T t) {
        return loadConfiguration(getFile(t));
    }


    protected FileConfiguration loadConfiguration(File file) {
        return file.exists() ? YamlConfiguration.loadConfiguration(file) : null;
    }
}
