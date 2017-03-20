package me.kayaba.guilds.impl.storage.managers.file;

import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.impl.storage.*;
import me.kayaba.guilds.impl.storage.managers.*;
import me.kayaba.guilds.util.*;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.*;

public abstract class AbstractFileResourceManager<T extends Resource> extends AbstractResourceManager<T> {
    private final File directory;


    protected AbstractFileResourceManager(Storage storage, Class<T> clazz, String directoryPath) {
        super(storage, clazz);
        directory = new File(getStorage().getDirectory(), directoryPath);
    }

    @Override
    protected AbstractFileStorage getStorage() {
        if (!(super.getStorage() instanceof AbstractFileStorage)) {
            throw new IllegalArgumentException("Invalid storage type");
        }

        return (AbstractFileStorage) super.getStorage();
    }

    @Override
    public void add(T t) {
        try {
            createFileIfNotExists(getFile(t));
            t.setAdded();
        } catch (IOException e) {
            LoggerUtils.exception(e);
        }
    }


    protected File getDirectory() {
        return directory;
    }


    public abstract File getFile(T t);


    protected List<File> getFiles() {
        File[] files = getDirectory().listFiles();
        final List<File> list = new ArrayList<>();

        if (files != null) {
            list.addAll(Arrays.asList(files));
        }

        return list;
    }


    private void createFileIfNotExists(File file) throws IOException {
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("File creating failed (" + file.getPath() + ")");
            }
        }
    }


    protected final String trimExtension(File file) {
        return StringUtils.substring(file.getName(), 0, StringUtils.lastIndexOf(file.getName(), '.'));
    }
}
