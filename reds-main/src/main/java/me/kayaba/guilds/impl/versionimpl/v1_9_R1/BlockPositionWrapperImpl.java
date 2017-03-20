package me.kayaba.guilds.impl.versionimpl.v1_9_R1;

import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.reflect.*;
import org.bukkit.*;

import java.lang.reflect.*;

public class BlockPositionWrapperImpl extends AbstractBlockPositionWrapper {
    protected static Class<?> blockPositionClass;
    protected static Class<?> baseBlockPositionClass;
    protected static Field xField;
    protected static Field yField;
    protected static Field zField;

    static {
        try {
            blockPositionClass = Reflections.getCraftClass("BlockPosition");
            baseBlockPositionClass = Reflections.getCraftClass("BaseBlockPosition");
            xField = Reflections.getPrivateField(baseBlockPositionClass, "a");
            yField = Reflections.getPrivateField(baseBlockPositionClass, "c");
            zField = Reflections.getPrivateField(baseBlockPositionClass, "d");
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            LoggerUtils.exception(e);
        }
    }

    public BlockPositionWrapperImpl(Location location) {
        super(location);
    }

    public BlockPositionWrapperImpl(int x, int y, int z) {
        super(x, y, z);
    }

    public BlockPositionWrapperImpl(Object blockPosition) throws IllegalAccessException {
        super(blockPosition);

        setX(xField.getInt(blockPosition));
        setY(yField.getInt(blockPosition));
        setZ(zField.getInt(blockPosition));
    }

    @Override
    public Object getBlockPosition() {
        try {
            return blockPositionClass.getConstructor(
                    int.class,
                    int.class,
                    int.class
            ).newInstance(
                    getX(),
                    getY(),
                    getZ()
            );
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException | InstantiationException e) {
            LoggerUtils.exception(e);
            return null;
        }
    }
}
