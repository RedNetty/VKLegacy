package me.kayaba.guilds.util.reflect;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.util.reflect.*;
import me.kayaba.guilds.impl.util.reflect.*;
import me.kayaba.guilds.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.lang.reflect.*;
import java.util.*;

public final class Reflections {
    private static Method entityGetHandleMethod;
    private static Method worldGetHandleMethod;
    private static Field modifiersField;

    static {
        try {
            if (PracticeServer.getInstance() != null) {
                Class<?> craftWorldClass = getBukkitClass("CraftWorld");
                Class<?> craftEntityClass = getBukkitClass("entity.CraftEntity");
                worldGetHandleMethod = getMethod(craftWorldClass, "getHandle");
                entityGetHandleMethod = getMethod(craftEntityClass, "getHandle");
                modifiersField = getPrivateField(Field.class, "modifiers");
            }
        } catch (NoSuchFieldException | ClassNotFoundException | NoSuchMethodException e) {
            LoggerUtils.exception(e);
        }
    }


    public static Class<?> getClass(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }


    public static Class<?> getCraftClass(String name) throws ClassNotFoundException {
        return getClass("net.minecraft.server." + getVersion() + name);
    }


    public static Class<?> getBukkitClass(String name) throws ClassNotFoundException {
        return getClass("org.bukkit.craftbukkit." + getVersion() + name);
    }


    public static Object getHandle(Entity entity) throws InvocationTargetException, IllegalAccessException {
        return entityGetHandleMethod.invoke(entity);
    }


    public static Object getHandle(World world) throws InvocationTargetException, IllegalAccessException {
        return worldGetHandleMethod.invoke(world);
    }


    public static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        return clazz.getDeclaredField(fieldName);
    }


    public static <T> FieldAccessor<T> getField(Class<?> target, Class<T> fieldType, int index) throws NoSuchFieldException {
        return getField(target, null, fieldType, index);
    }


    public static <T> FieldAccessor<T> getField(Class<?> target, String name, Class<T> fieldType) throws NoSuchFieldException {
        return getField(target, name, fieldType, 0);
    }


    private static <T> FieldAccessor<T> getField(Class<?> target, String name, Class<T> fieldType, int index) throws NoSuchFieldException {
        for (final Field field : target.getDeclaredFields()) {
            if ((name == null || field.getName().equals(name)) && fieldType.isAssignableFrom(field.getType()) && index-- <= 0) {
                field.setAccessible(true);
                return new FieldAccessorImpl<>(field);
            }
        }

        if (target.getSuperclass() != null) {
            return getField(target.getSuperclass(), name, fieldType, index);
        }

        throw new NoSuchFieldException("Cannot find field with type " + fieldType);
    }


    public static Field getPrivateField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    public static <T> Set<FieldAccessor<T>> getFields(Class<?> clazz, Class<T> type) {
        Set<FieldAccessor<T>> collection = new HashSet<>();

        for (Field field : clazz.getFields()) {
            if (!field.getType().equals(type)) {
                continue;
            }

            collection.add(new FieldAccessorImpl<T>(field));
        }

        return collection;
    }


    public static Method getMethod(Class<?> clazz, String method, Class<?>... args) throws NoSuchMethodException {
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(method) && classListEqual(args, m.getParameterTypes())) {
                return m;
            }
        }

        throw new NoSuchMethodException("Could not access the method");
    }


    public static Method getMethod(Class<?> clazz, String method) throws NoSuchMethodException {
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(method)) {
                return m;
            }
        }

        throw new NoSuchMethodException("Could not access the method");
    }


    public static void setNotFinal(Field field) throws IllegalAccessException {
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    }


    public static <T> MethodInvoker<T> getMethod(final Class<?> clazz, final Class<T> type, final String methodName, final Class<?>... args) throws NoSuchMethodException {
        return new MethodInvoker<T>() {
            private final Method method;

            {
                if (args.length == 0) {
                    method = getMethod(clazz, methodName);
                } else {
                    method = getMethod(clazz, methodName, args);
                }

                if (!method.getReturnType().equals(type)) {
                    throw new IllegalArgumentException("Invalid return type. " + type.getName() + " assumed, got " + method.getReturnType().getName());
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            public T invoke(Object target, Object... arguments) {
                try {
                    return (T) method.invoke(target, arguments);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Cannot access reflection.", e);
                }
            }
        };
    }


    public static boolean classListEqual(Class<?>[] l1, Class<?>[] l2) {
        if (l1.length != l2.length) {
            return false;
        }

        for (int i = 0; i < l1.length; i++) {
            if (l1[i] != l2[i]) {
                return false;
            }
        }

        return true;
    }


    public static Enum getEnumConstant(Class<?> clazz, String name) {
        if (!clazz.isEnum()) {
            throw new IllegalArgumentException("Class" + clazz.getName() + " is not an enum");
        }

        for (Object enumConstant : clazz.getEnumConstants()) {
            if (((Enum) enumConstant).name().equalsIgnoreCase(name)) {
                return (Enum) enumConstant;
            }
        }

        throw new IllegalArgumentException("Could not find enum constant");
    }


    public static String getVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        return name.substring(name.lastIndexOf('.') + 1) + ".";
    }

    public interface ConstructorInvoker<T> {

        T invoke(Object... arguments);
    }
}
