package me.kayaba.guilds.impl.basic;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.manager.*;
import org.apache.commons.lang.*;
import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.inventory.*;

import java.util.*;

public class ConfigWrapperImpl extends AbstractVarKeyApplicable<ConfigWrapper> implements ConfigWrapper {
    private static final ConfigManager cM = PracticeServer.getInstance() == null ? null : PracticeServer.getInstance().getConfigManager();
    private String path;
    private boolean fixColors;


    public ConfigWrapperImpl(String path, boolean fixColors) {
        this.path = path;
        this.fixColors = fixColors;
    }


    public ConfigWrapperImpl(ConfigWrapper configWrapper) {
        this(configWrapper.getPath(), configWrapper.isFixColors());
    }

    @Override
    public String getName() {
        return StringUtils.replace(path, ".", "_").toUpperCase();
    }

    @Override
    public String getPath() {
        if (path == null) {
            throw new IllegalArgumentException("Path has not been set!");
        }

        return path;
    }

    @Override
    public boolean isFixColors() {
        return fixColors;
    }

    @Override
    public boolean isEmpty() {
        return cM.getConfig().get(getPath()).equals("none");
    }

    @Override
    public String getString() {
        return cM.get(this, String.class);
    }

    @Override
    public List<String> getStringList() {
        List<String> r = cM.isInCache(this) && cM.getEnumConfig(this) instanceof List ? (List<String>) cM.getEnumConfig(this) : cM.getStringList(path, vars, fixColors);
        cM.putInCache(this, r);
        return r;
    }

    @Override
    public List<ItemStack> getItemStackList() {
        List<ItemStack> r = cM.isInCache(this) && cM.getEnumConfig(this) instanceof List ? (List<ItemStack>) cM.getEnumConfig(this) : cM.getItemStackList(path, vars);
        cM.putInCache(this, r);
        return r;
    }

    @Override
    public List<Material> getMaterialList() {
        List<Material> r = cM.isInCache(this) && cM.getEnumConfig(this) instanceof List ? (List<Material>) cM.getEnumConfig(this) : cM.getMaterialList(path, vars);
        cM.putInCache(this, r);
        return r;
    }

    @Override
    public long getLong() {
        return cM.get(this, Long.class);
    }

    @Override
    public double getDouble() {
        return cM.get(this, Double.class);
    }

    @Override
    public int getInt() {
        return cM.get(this, Integer.class);
    }

    @Override
    public boolean getBoolean() {
        return cM.get(this, Boolean.class);
    }

    @Override
    public int getSeconds() {
        int r = cM.isInCache(this) && cM.getEnumConfig(this) instanceof Integer ? (int) cM.getEnumConfig(this) : cM.getSeconds(path);
        cM.putInCache(this, r);
        return r;
    }

    @Override
    public ItemStack getItemStack() {
        return cM.get(this, ItemStack.class);
    }

    @Override
    public Material getMaterial() {
        return cM.get(this, Material.class);
    }

    @Override
    public byte getMaterialData() {
        byte r = cM.isInCache(this) && cM.getEnumConfig(this) instanceof Byte ? (byte) cM.getEnumConfig(this) : cM.getMaterialData(path);
        cM.putInCache(this, r);
        return r;
    }

    @Override
    public double getPercent() {
        return getDouble() / 100;
    }

    @Override
    public ConfigurationSection getConfigurationSection() {
        return cM.getConfig().getConfigurationSection(path);
    }

    @Override
    public void set(Object obj) {
        cM.set(this, obj);
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void setFixColors(boolean b) {
        this.fixColors = b;
    }

    @Override
    public <E extends Enum> E toEnum(Class<E> clazz) {
        for (E enumConstant : clazz.getEnumConstants()) {
            if (enumConstant.name().equalsIgnoreCase(getString())) {
                return enumConstant;
            }
        }

        return null;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public ConfigWrapper clone() {
        return new ConfigWrapperImpl(this);
    }
}
