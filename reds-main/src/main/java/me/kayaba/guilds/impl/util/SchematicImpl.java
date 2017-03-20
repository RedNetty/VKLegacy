package me.kayaba.guilds.impl.util;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.reflect.*;
import org.bukkit.*;
import org.bukkit.block.*;

import java.io.*;
import java.lang.reflect.*;

@SuppressWarnings("ConstantConditions")
public class SchematicImpl implements Schematic {
    protected static Class<?> nBTCompressedStreamToolsClass;
    protected static Class<?> nBTTagCompoundClass;
    protected static Method aMethod;
    protected static Method getShortMethod;
    protected static Method getByteArrayMethod;
    private short width;
    private short height;
    private short length;
    private byte[] blocks;
    private byte[] data;
    private final String name;

    static {
        try {
            nBTCompressedStreamToolsClass = Reflections.getCraftClass("NBTCompressedStreamTools");
            nBTTagCompoundClass = Reflections.getCraftClass("NBTTagCompound");
            aMethod = Reflections.getMethod(nBTCompressedStreamToolsClass, "a", InputStream.class);
            getShortMethod = Reflections.getMethod(nBTTagCompoundClass, "getShort");
            getByteArrayMethod = Reflections.getMethod(nBTTagCompoundClass, "getByteArray");
        } catch (Exception e) {
            LoggerUtils.exception(e);
        }
    }


    public SchematicImpl(String fileName) throws FileNotFoundException {
        this(new File(PracticeServer.getInstance().getDataFolder() + "/schematic/", fileName));
    }


    public SchematicImpl(File file) throws FileNotFoundException {
        name = file.getName();

        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        try {
            FileInputStream fis = new FileInputStream(file);
            Object nbtData = aMethod.invoke(null, fis);

            width = (short) getShortMethod.invoke(nbtData, "Width");
            height = (short) getShortMethod.invoke(nbtData, "Height");
            length = (short) getShortMethod.invoke(nbtData, "Length");
            blocks = (byte[]) getByteArrayMethod.invoke(nbtData, "Blocks");
            data = (byte[]) getByteArrayMethod.invoke(nbtData, "Data");

            fis.close();
        } catch (InvocationTargetException | IllegalAccessException | IOException e) {
            LoggerUtils.exception(e);
        }
    }

    @Override
    public void paste(Location location) {
        Location root = location.clone().subtract(width / 2, 1, length / 2);

        for (int y = height - 1; y >= 0; y--) {
            for (int z = 0; z < length; z++) {
                for (int x = 0; x < width; x++) {
                    Location blockLocation = root.clone().add(x, y, z);
                    int index = x + (y * length + z) * width;

                    Block block = blockLocation.getBlock();

                    Meta.protect(block);
                    Meta.setMetadata(block, "state", block.getState());

                    block.setTypeId(blocks[index] < 0 ? Material.SPONGE.getId() : blocks[index]);
                    block.setData(data[index]);
                }
            }
        }
    }

    @Override
    public short getWidth() {
        return width;
    }

    @Override
    public short getHeight() {
        return height;
    }

    @Override
    public short getLength() {
        return length;
    }

    @Override
    public String getName() {
        return name;
    }
}
