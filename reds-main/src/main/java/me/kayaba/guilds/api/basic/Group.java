package me.kayaba.guilds.api.basic;

public interface Group {
    interface Key<T> {

        Class<T> getType();
    }


    String getName();


    <T> T get(Key<T> key);
}
