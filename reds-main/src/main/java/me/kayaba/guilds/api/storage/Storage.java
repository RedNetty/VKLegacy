package me.kayaba.guilds.api.storage;

import java.util.*;

public interface Storage {

    boolean setUp();


    <T extends Resource> ResourceManager<T> getResourceManager(Class<T> clazz);


    Map<Class<? extends Resource>, ResourceManager<? extends Resource>> getResourceManagers();


    <T extends Resource> void registerResourceManager(Class<T> clazz, ResourceManager<T> resourceManager);


    void registerManagers();


    void save();
}
