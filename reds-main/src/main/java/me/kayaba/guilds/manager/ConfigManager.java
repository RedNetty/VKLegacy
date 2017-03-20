package me.kayaba.guilds.manager;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.StringUtils;
import me.kayaba.guilds.util.reflect.*;
import org.apache.commons.lang.*;
import org.bukkit.*;
import org.bukkit.boss.*;
import org.bukkit.configuration.file.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ConfigManager {
    private static final PracticeServer plugin = PracticeServer.getInstance();
    private FileConfiguration config;

    private DataStorageType primaryDataStorageType;
    private DataStorageType secondaryDataStorageType;
    private DataStorageType dataStorageType;

    private final List<PotionEffectType> guildEffects = new ArrayList<>();

    private final Map<ConfigWrapper, Object> cache = new HashMap<>();
    private static final ServerVersion serverVersion = ServerVersion.detect();


    public static ServerVersion getServerVersion() {
        return serverVersion;
    }

    public enum ServerVersion {
        MINECRAFT_1_7_R3,
        MINECRAFT_1_7_R4,
        MINECRAFT_1_8_R1,
        MINECRAFT_1_8_R2,
        MINECRAFT_1_8_R3,
        MINECRAFT_1_9_R1,
        MINECRAFT_1_9_R2,
        MINECRAFT_1_10_R1,
        MINECRAFT_1_10_R2,
        MINECRAFT_1_11_R1,;


        public static ServerVersion detect() {
            String craftBukkitVersion = Reflections.getVersion();
            craftBukkitVersion = craftBukkitVersion.substring(1, craftBukkitVersion.length() - 1);

            for (ServerVersion version : values()) {
                String string = version.name();
                string = org.apache.commons.lang.StringUtils.replace(string, "MINECRAFT_", "");

                if (craftBukkitVersion.startsWith(string)) {
                    LoggerUtils.info("This server is using version: " + craftBukkitVersion);
                    return version;
                }
            }

            ServerVersion closestVersion = getClosestVersion(craftBukkitVersion);
            LoggerUtils.error("Version " + craftBukkitVersion + " is not supported by PracticeServer.");
            LoggerUtils.error("Expect bugs and report them to the development team. (/ng)");
            LoggerUtils.error("KayabaGuilds is now using implementation for version: " + closestVersion.name());


            return closestVersion;
        }


        public boolean isOlderThan(ServerVersion version) {
            return getIndex() < version.getIndex();
        }


        public boolean isNewerThan(ServerVersion version) {
            return getIndex() > version.getIndex();
        }


        public static ServerVersion getClosestVersion(String versionString) {
            versionString = org.apache.commons.lang.StringUtils.replace(versionString, "_", "");
            versionString = org.apache.commons.lang.StringUtils.replace(versionString, "R", "");
            int versionInt = Integer.parseInt(versionString);
            final List<Integer> intVersions = new ArrayList<>();
            intVersions.add(versionInt);
            Map<Integer, ServerVersion> integerServerVersionMap = new HashMap<>();

            for (ServerVersion serverVersion : ServerVersion.values()) {
                String versionString1 = org.apache.commons.lang.StringUtils.replace(serverVersion.name().substring(10), "_", "");
                versionString1 = org.apache.commons.lang.StringUtils.replace(versionString1, "R", "");
                int versionNumber = Integer.parseInt(versionString1);
                intVersions.add(versionNumber);
                integerServerVersionMap.put(versionNumber, serverVersion);
            }

            Collections.sort(intVersions, new Comparator<Integer>() {
                public int compare(Integer o1, Integer o2) {
                    return o2 - o1;
                }
            });

            int index;
            for (index = 0; index < intVersions.size(); index++) {
                if (versionInt == intVersions.get(index)) {
                    break;
                }
            }

            int targetIndex = index + 1;
            if (intVersions.size() <= targetIndex) {
                targetIndex = intVersions.size() - 2;
            }

            return integerServerVersionMap.get(intVersions.get(targetIndex));
        }


        private int getIndex() {
            int index = 1;

            for (ServerVersion version : values()) {
                if (version == this) {
                    return index;
                }

                index++;
            }

            return index;
        }


        public String getString() {
            return "v" + name().substring(10);
        }
    }

    public static final Map<String, String> essentialsLocale = new HashMap<String, String>() {{
        put("en", "en-en");
        put("pl", "pl-pl");
        put("de", "de-de");
        put("zh", "zh-cn");
    }};


    public void reload() {
        cache.clear();

        if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
            LoggerUtils.info("Creating default config...");
            plugin.saveDefaultConfig();
        }

        File schematicDirectory = new File(PracticeServer.getInstance().getDataFolder(), "/schematic/");
        if (!schematicDirectory.exists() && schematicDirectory.mkdirs()) {
            LoggerUtils.info("Created schematic/ directory");
        }

        plugin.reloadConfig();
        config = plugin.getConfig();

        LoggerUtils.info("This server is using Bukkit: " + Bukkit.getBukkitVersion());

        if (Config.USETITLES.getBoolean() && getServerVersion().isOlderThan(ServerVersion.MINECRAFT_1_8_R1)) {
            Config.USETITLES.set(false);
            LoggerUtils.error("You can't use Titles with Bukkit other than 1.8");
        }

        String primaryDataStorageTypeString = Config.DATASTORAGE_PRIMARY.getString().toUpperCase();
        String secondaryDataStorageTypeString = Config.DATASTORAGE_SECONDARY.getString().toUpperCase();

        boolean primaryValid = false;
        boolean secondaryValid = false;

        if (primaryDataStorageTypeString.equals(secondaryDataStorageTypeString)) {
            LoggerUtils.error("Primary and secondary data storage types cannot be the same!");
            LoggerUtils.error("Resetting to defaults. (MySQL/Flat)");
            primaryDataStorageTypeString = DataStorageType.MYSQL.name();
            secondaryDataStorageTypeString = DataStorageType.FLAT.name();
        }

        for (DataStorageType dst : DataStorageType.values()) {
            if (dst.name().equals(primaryDataStorageTypeString)) {
                primaryValid = true;
            }

            if (dst.name().equals(secondaryDataStorageTypeString)) {
                secondaryValid = true;
            }
        }

        if (!primaryValid || !secondaryValid) {
            LoggerUtils.error("Not valid Data Storage Types.");
            LoggerUtils.error("Resetting to defaults. (MySQL/Flat)");
            primaryDataStorageTypeString = DataStorageType.MYSQL.name();
            secondaryDataStorageTypeString = DataStorageType.FLAT.name();
        }

        if (primaryDataStorageTypeString.equalsIgnoreCase("sqlite") && !Config.DEBUG.getBoolean()) {
            primaryDataStorageTypeString = DataStorageType.MYSQL.name();
            LoggerUtils.error("Please enable debug mode to use SQLite storage.");
        }

        primaryDataStorageType = DataStorageType.valueOf(primaryDataStorageTypeString);
        secondaryDataStorageType = DataStorageType.valueOf(secondaryDataStorageTypeString);
        setToPrimaryDataStorageType();
        LoggerUtils.info("Data storage: Primary: " + primaryDataStorageType.name() + ", Secondary: " + secondaryDataStorageType.name());


        guildEffects.clear();
        List<String> guildEffectsString = Config.GUILD_EFFECT_LIST.getStringList();
        for (String effect : guildEffectsString) {
            PotionEffectType effectType = PotionEffectType.getByName(effect);
            if (effectType != null) {
                guildEffects.add(effectType);
            }
        }


        if (Config.LIVEREGENERATION_TASKINTERVAL.getSeconds() < 60) {
            LoggerUtils.error("Live regeneration task interval can't be shorter than 60 seconds.");
            Config.LIVEREGENERATION_TASKINTERVAL.set("60s");
        }

        if (Config.CLEANUP_INTERVAL.getSeconds() < 60) {
            LoggerUtils.error("Cleanup interval can't be shorter than 60 seconds.");
            Config.CLEANUP_INTERVAL.set("60s");
        }

        if (Config.SAVEINTERVAL.getSeconds() < 60) {
            LoggerUtils.error("Save interval can't be shorter than 60 seconds.");
            Config.SAVEINTERVAL.set("60s");
        }


        if (getServerVersion().isNewerThan(ServerVersion.MINECRAFT_1_8_R3)) {
            if (Config.BOSSBAR_RAIDBAR_STYLE.toEnum(BarStyle.class) == null) {
                LoggerUtils.error("Invalid BarStyle enum. Resetting to default.");
                Config.BOSSBAR_RAIDBAR_STYLE.set(BarStyle.SOLID.name());
            }


            if (Config.BOSSBAR_RAIDBAR_COLOR.toEnum(BarColor.class) == null) {
                LoggerUtils.error("Invalid BarColor enum. Resetting to default.");
                Config.BOSSBAR_RAIDBAR_COLOR.set(BarColor.PURPLE.name());
            }
        }
    }


    public DataStorageType getDataStorageType() {
        return dataStorageType;
    }


    public List<PotionEffectType> getGuildEffects() {
        return guildEffects;
    }


    public FileConfiguration getConfig() {
        return config;
    }


    public boolean isSecondaryDataStorageType() {
        return dataStorageType == secondaryDataStorageType;
    }


    public void setToSecondaryDataStorageType() {
        dataStorageType = secondaryDataStorageType;
    }


    public void setToPrimaryDataStorageType() {
        dataStorageType = primaryDataStorageType;
    }


    public Object getEnumConfig(ConfigWrapper c) {
        return cache.get(c);
    }


    public boolean isInCache(ConfigWrapper c) {
        return cache.containsKey(c);
    }


    public void putInCache(ConfigWrapper c, Object o) {
        cache.put(c, o);
    }


    public void removeFromCache(ConfigWrapper c) {
        if (cache.containsKey(c)) {
            cache.remove(c);
        }
    }


    @SuppressWarnings("unchecked")
    public <T> T get(ConfigWrapper configWrapper, Class<T> clazz) {
        Validate.notNull(configWrapper);
        Validate.notNull(clazz);
        Object value;

        if (isInCache(configWrapper)
                && clazz.isInstance(getEnumConfig(configWrapper))
                && !configWrapper.isChanged()) {
            return (T) getEnumConfig(configWrapper);
        }

        if (clazz == String.class) {
            value = getString(configWrapper.getPath(), configWrapper.getVars(), configWrapper.isFixColors());
        } else if (clazz == Long.class) {
            value = getLong(configWrapper.getPath());
        } else if (clazz == Double.class) {
            value = getDouble(configWrapper.getPath());
        } else if (clazz == Integer.class) {
            value = getInt(configWrapper.getPath());
        } else if (clazz == Boolean.class) {
            value = getBoolean(configWrapper.getPath());
        } else if (clazz == Material.class) {
            value = getMaterial(configWrapper.getPath());
        } else if (clazz == ItemStack.class) {
            value = getItemStack(configWrapper.getPath(), configWrapper.getVars());
        } else {
            throw new RuntimeException("Return type " + clazz.getName() + " is not allowed.");
        }

        if (value != null) {
            putInCache(configWrapper, value);
        }

        return (T) value;
    }


    public String getString(String path, Map<VarKey, String> vars, boolean fixColors) {
        String string = config.getString(path);

        if (string == null) {
            return "";
        }

        string = MessageManager.replaceVarKeyMap(string, vars, false);

        if (fixColors) {
            string = StringUtils.fixColors(string);
        }

        return string;
    }


    public List<String> getStringList(String path, Map<VarKey, String> vars, boolean fixColors) {
        List<String> list = config.getStringList(path);

        if (list == null) {
            return new ArrayList<>();
        }

        list = MessageManager.replaceVarKeyMap(list, vars, false);

        if (fixColors) {
            list = StringUtils.fixColors(list);
        }

        return list;
    }


    public long getLong(String path) {
        return config.getLong(path);
    }


    public int getInt(String path) {
        return config.getInt(path);
    }


    public double getDouble(String path) {
        return config.getDouble(path);
    }


    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }


    public int getSeconds(String path) {
        return StringUtils.stringToSeconds(getString(path, null, false));
    }


    public ItemStack getItemStack(String path, Map<VarKey, String> vars) {
        return ItemStackUtils.stringToItemStack(getString(path, vars, true));
    }


    public Material getMaterial(String path) {
        String string = getString(path, null, false);
        return Material.getMaterial((string.contains(":") ? org.apache.commons.lang.StringUtils.split(string, ':')[0] : string).toUpperCase());
    }


    public byte getMaterialData(String path) {
        return Byte.valueOf(getString(path, null, false).contains(":") ? org.apache.commons.lang.StringUtils.split(getString(path, null, false), ':')[1] : "0");
    }


    public List<ItemStack> getItemStackList(String path, Map<VarKey, String> vars) {
        final List<String> stringList = getStringList(path, vars, true);
        final List<ItemStack> itemStackList = new ArrayList<>();

        for (String string : stringList) {
            ItemStack is = ItemStackUtils.stringToItemStack(string);

            if (is != null) {
                itemStackList.add(is);
            }
        }

        return itemStackList;
    }


    public List<Material> getMaterialList(String path, Map<VarKey, String> vars) {
        final List<String> stringList = getStringList(path, vars, false);
        final List<Material> materialList = new ArrayList<>();

        for (String string : stringList) {
            Material material = Material.getMaterial(string);
            if (material != null) {
                materialList.add(material);
            }
        }

        return materialList;
    }


    public File getConfigFile() {
        return new File(plugin.getDataFolder(), "config.yml");
    }


    public void backupFile() throws IOException {
        File backupFile = new File(getConfigFile().getParentFile(), "config.yml.backup");
        Files.copy(getConfigFile().toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }


    public void set(ConfigWrapper c, Object obj) {
        config.set(c.getPath(), obj);
        removeFromCache(c);
    }


    public void save() throws IOException {
        config.save(getConfigFile());
    }
}
