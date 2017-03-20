package me.kayaba.guilds.api.util.reflect;

public interface MethodInvoker<T> {

    T invoke(Object target, Object... arguments);
}
