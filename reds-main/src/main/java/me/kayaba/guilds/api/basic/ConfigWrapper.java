package me.kayaba.guilds.api.basic;

import me.kayaba.guilds.api.util.*;
import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.inventory.*;

import java.util.*;

public interface ConfigWrapper extends VarKeyApplicable<ConfigWrapper>, Cloneable {

    String getName();


    String getPath();


    boolean isFixColors();


    boolean isEmpty();


    String getString();


    List<String> getStringList();


    List<ItemStack> getItemStackList();


    List<Material> getMaterialList();


    long getLong();


    double getDouble();


    int getInt();


    boolean getBoolean();


    int getSeconds();


    ItemStack getItemStack();


    Material getMaterial();


    byte getMaterialData();


    double getPercent();


    ConfigurationSection getConfigurationSection();


    void set(Object obj);


    void setPath(String path);


    void setFixColors(boolean b);


    <E extends Enum> E toEnum(Class<E> clazz);


    ConfigWrapper clone();
}
