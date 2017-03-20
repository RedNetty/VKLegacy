package me.kayaba.guilds.api.storage;

public interface Migrant {

    Storage getFromStorage();


    Storage getToStorage();


    void migrate();


    void save();
}
