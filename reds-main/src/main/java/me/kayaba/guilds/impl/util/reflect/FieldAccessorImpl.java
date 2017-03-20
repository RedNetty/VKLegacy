package me.kayaba.guilds.impl.util.reflect;

import me.kayaba.guilds.api.util.reflect.*;
import me.kayaba.guilds.util.reflect.*;

import java.lang.reflect.*;

public class FieldAccessorImpl<T> implements FieldAccessor<T> {
    private final Field field;


    public FieldAccessorImpl(Field field) {
        this.field = field;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(Object target) {
        try {
            return (T) field.get(target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot access reflection.", e);
        }
    }

    @Override
    public T get() {
        return get(null);
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public void set(T value) {
        set(null, value);
    }

    @Override
    public void set(Object target, Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot access reflection.", e);
        }
    }

    @Override
    public boolean hasField(Object target) {
        return field.getDeclaringClass().isAssignableFrom(target.getClass());
    }

    @Override
    public void setNotFinal() {
        try {
            Reflections.setNotFinal(field);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot access reflection.", e);
        }
    }
}
