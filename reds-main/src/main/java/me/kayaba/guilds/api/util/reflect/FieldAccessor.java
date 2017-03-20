package me.kayaba.guilds.api.util.reflect;

public interface FieldAccessor<T> {

    T get(Object target);


    T get();


    String getName();


    void set(Object target, T value);


    void set(T value);


    boolean hasField(Object target);


    void setNotFinal();
}
