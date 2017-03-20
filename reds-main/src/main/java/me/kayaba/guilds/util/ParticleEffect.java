package me.kayaba.guilds.util;

import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.reflect.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.lang.reflect.*;
import java.util.*;
import java.util.Map.*;


@SuppressWarnings("unused")
public class ParticleEffect {

    private static final Map<String, ParticleEffect> nameMap = new HashMap<>();
    private static final Map<Integer, ParticleEffect> idMap = new HashMap<>();
    private final String name;
    private final int id;
    private final ConfigManager.ServerVersion requiredVersion;
    private final Set<ParticleProperty> properties = new HashSet<>();

    public static final ParticleEffect EXPLOSION_NORMAL = new ParticleEffect("explode", 0, ParticleProperty.DIRECTIONAL);
    public static final ParticleEffect EXPLOSION_LARGE = new ParticleEffect("largeexplode", 1);
    public static final ParticleEffect EXPLOSION_HUGE = new ParticleEffect("hugeexplosion", 2);
    public static final ParticleEffect FIREWORKS_SPARK = new ParticleEffect("fireworksSpark", 3, ParticleProperty.DIRECTIONAL);
    public static final ParticleEffect WATER_BUBBLE = new ParticleEffect("bubble", 4, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_WATER);
    public static final ParticleEffect WATER_SPLASH = new ParticleEffect("splash", 5, ParticleProperty.DIRECTIONAL);
    public static final ParticleEffect WATER_WAKE = new ParticleEffect("wake", 6, ConfigManager.ServerVersion.MINECRAFT_1_7_R3, ParticleProperty.DIRECTIONAL);
    public static final ParticleEffect SUSPENDED = new ParticleEffect("suspended", 7, ParticleProperty.REQUIRES_WATER);
    public static final ParticleEffect SUSPENDED_DEPTH = new ParticleEffect("depthSuspend", 8, ParticleProperty.DIRECTIONAL);
    public static final ParticleEffect CRIT = new ParticleEffect("crit", 9, ParticleProperty.DIRECTIONAL);
    public static final ParticleEffect CRIT_MAGIC = new ParticleEffect("magicCrit", 10, ParticleProperty.DIRECTIONAL);
    public static final ParticleEffect SMOKE_NORMAL = new ParticleEffect("smoke", 11, ParticleProperty.DIRECTIONAL);
    public static final ParticleEffect SMOKE_LARGE = new ParticleEffect("largesmoke", 12, ParticleProperty.DIRECTIONAL);
    public static final ParticleEffect SPELL = new ParticleEffect("spell", 13);
    public static final ParticleEffect SPELL_INSTANT = new ParticleEffect("instantSpell", 14);
    public static final ParticleEffect SPELL_MOB = new ParticleEffect("mobSpell", 15, ParticleProperty.COLORABLE);
    public static final ParticleEffect SPELL_MOB_AMBIENT = new ParticleEffect("mobSpellAmbient", 16, ParticleProperty.COLORABLE);
    public static final ParticleEffect SPELL_WITCH = new ParticleEffect("witchMagic", 17);
    public static final ParticleEffect DRIP_WATER = new ParticleEffect("dripWater", 18);
    public static final ParticleEffect DRIP_LAVA = new ParticleEffect("dripLava", 19);
    public static final ParticleEffect VILLAGER_ANGRY = new ParticleEffect("angryVillager", 20);
    public static final ParticleEffect VILLAGER_HAPPY = new ParticleEffect("happyVillager", 21, ParticleProperty.DIRECTIONAL);
    public static final ParticleEffect TOWN_AURA = new ParticleEffect("townaura", 22, ParticleProperty.DIRECTIONAL);
    public static final ParticleEffect NOTE = new ParticleEffect("note", 23, ParticleProperty.COLORABLE);
    public static final ParticleEffect PORTAL = new ParticleEffect("portal", 24, ParticleProperty.DIRECTIONAL);
    public static final ParticleEffect ENCHANTMENT_TABLE = new ParticleEffect("enchantmenttable", 25, ParticleProperty.DIRECTIONAL);
    public static final ParticleEffect FLAME = new ParticleEffect("flame", 26, ParticleProperty.DIRECTIONAL);
    public static final ParticleEffect LAVA = new ParticleEffect("lava", 27);
    public static final ParticleEffect FOOTSTEP = new ParticleEffect("footstep", 28);
    public static final ParticleEffect CLOUD = new ParticleEffect("cloud", 29, ParticleProperty.DIRECTIONAL);
    public static final ParticleEffect REDSTONE = new ParticleEffect("reddust", 30, ParticleProperty.COLORABLE);
    public static final ParticleEffect SNOWBALL = new ParticleEffect("snowballpoof", 31);
    public static final ParticleEffect SNOW_SHOVEL = new ParticleEffect("snowshovel", 32, ParticleProperty.DIRECTIONAL);
    public static final ParticleEffect SLIME = new ParticleEffect("slime", 33);
    public static final ParticleEffect HEART = new ParticleEffect("heart", 34);
    public static final ParticleEffect BARRIER = new ParticleEffect("barrier", 35, ConfigManager.ServerVersion.MINECRAFT_1_8_R1);
    public static final ParticleEffect ITEM_CRACK = new ParticleEffect("iconcrack", 36, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA);
    public static final ParticleEffect BLOCK_CRACK = new ParticleEffect("blockcrack", 37, ParticleProperty.REQUIRES_DATA);
    public static final ParticleEffect BLOCK_DUST = new ParticleEffect("blockdust", 38, ConfigManager.ServerVersion.MINECRAFT_1_7_R3, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA);
    public static final ParticleEffect WATER_DROP = new ParticleEffect("droplet", 39, ConfigManager.ServerVersion.MINECRAFT_1_8_R1);
    public static final ParticleEffect ITEM_TAKE = new ParticleEffect("take", 40, ConfigManager.ServerVersion.MINECRAFT_1_8_R1);
    public static final ParticleEffect MOB_APPEARANCE = new ParticleEffect("mobappearance", 41, ConfigManager.ServerVersion.MINECRAFT_1_8_R1);


    ParticleEffect(String name, int id, ConfigManager.ServerVersion requiredVersion, ParticleProperty... properties) {
        this.name = name;
        this.id = id;
        this.requiredVersion = requiredVersion;
        Collections.addAll(this.properties, properties);

        nameMap.put(name, this);
        idMap.put(id, this);
    }


    ParticleEffect(String name, int id, ParticleProperty... properties) {
        this(name, id, ConfigManager.ServerVersion.MINECRAFT_1_7_R3, properties);
    }


    public String getName() {
        return this.name;
    }


    public int getId() {
        return this.id;
    }


    public ConfigManager.ServerVersion getRequiredVersion() {
        return this.requiredVersion;
    }


    public boolean hasProperty(ParticleProperty property) {
        return this.properties.contains(property);
    }


    public boolean isSupported() {
        return !ConfigManager.getServerVersion().isOlderThan(requiredVersion);
    }


    public static ParticleEffect fromName(String name) {
        for (Entry<String, ParticleEffect> entry : nameMap.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(name)) {
                return entry.getValue();
            }
        }

        return null;
    }


    public static ParticleEffect fromId(int id) {
        for (Entry<Integer, ParticleEffect> entry : idMap.entrySet()) {
            if (entry.getKey() == id) {
                return entry.getValue();
            }
        }

        return null;
    }


    private static boolean isLongDistance(Location location, List<Player> players) {
        String world = location.getWorld().getName();
        Iterator i = players.iterator();

        Location playerLocation;
        do {
            if (!i.hasNext()) {
                return false;
            }

            Player player = (Player) i.next();
            playerLocation = player.getLocation();
        }
        while (!world.equals(playerLocation.getWorld().getName()) || playerLocation.distanceSquared(location) < 65536.0D);

        return true;
    }


    public boolean isDataCorrect(ParticleData data) {
        return (this == BLOCK_CRACK || this == BLOCK_DUST) && data instanceof ParticleEffect.BlockData || this == ITEM_CRACK && data instanceof ParticleEffect.ItemData;
    }


    public boolean isColorCorrect(ParticleColor color) {
        return (this == SPELL_MOB || this == SPELL_MOB_AMBIENT || this == REDSTONE) && color instanceof OrdinaryColor || this == NOTE && color instanceof NoteColor;
    }


    public void send(Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount, ParticleData data, List<Player> players) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if (players == null) {
            players = new ArrayList<>(CompatibilityUtils.getOnlinePlayers());
        }

        Packet packet = new ParticlePacket(center, this, offsetX, offsetY, offsetZ, speed, amount, isLongDistance(center, players), data);
        PacketSender.sendPacket(players, packet);
    }


    public void send(Location center, Vector velocity, float speed, int amount, ParticleData data, List<Player> players) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        send(center, (float) velocity.getX(), (float) velocity.getY(), (float) velocity.getZ(), speed, amount, data, players);
    }


    public void send(Location center, ParticleColor color, float speed, int amount, ParticleData data, List<Player> players) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        send(center,
                this == ParticleEffect.REDSTONE && color instanceof ParticleEffect.OrdinaryColor && ((ParticleEffect.OrdinaryColor) color).getRed() == 0
                        ? 1.17549435E-38F
                        : color.getValueX(),
                color.getValueY(),
                color.getValueZ(),
                speed,
                amount,
                data,
                players);
    }


    public void send(Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount, ParticleData data, double range) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        send(center, offsetX, offsetY, offsetZ, speed, amount, data, ParticleUtils.getPlayersInRadius(center, range));
    }


    public void send(Location center, Vector velocity, float speed, int amount, ParticleData data, double range) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        send(center, velocity, speed, amount, data, ParticleUtils.getPlayersInRadius(center, range));
    }


    public void send(Location center, ParticleColor color, float speed, int amount, ParticleData data, double range) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        send(center, color, speed, amount, data, ParticleUtils.getPlayersInRadius(center, range));
    }

    public static final class NoteColor implements ParticleColor {
        private final int note;


        public NoteColor(int note) {
            if (note < 0) {
                throw new IllegalArgumentException("The note value is lower than 0");
            } else if (note > 24) {
                throw new IllegalArgumentException("The note value is higher than 24");
            } else {
                this.note = note;
            }
        }

        @Override
        public float getValueX() {
            return (float) this.note / 24.0F;
        }

        @Override
        public float getValueY() {
            return 0.0F;
        }

        @Override
        public float getValueZ() {
            return 0.0F;
        }
    }

    public static final class OrdinaryColor implements ParticleColor {
        private final int red;
        private final int green;
        private final int blue;


        public OrdinaryColor(int red, int green, int blue) {
            if (red < 0 || green < 0 || blue < 0) {
                throw new IllegalArgumentException("The value is lower than 0");
            }

            if (red > 255 || green > 255 || blue > 255) {
                throw new IllegalArgumentException("The value is higher than 255");
            }

            this.red = red;
            this.green = green;
            this.blue = blue;
        }


        public OrdinaryColor(Color color) {
            this(color.getRed(), color.getGreen(), color.getBlue());
        }


        public int getRed() {
            return this.red;
        }


        public int getGreen() {
            return this.green;
        }


        public int getBlue() {
            return this.blue;
        }

        @Override
        public float getValueX() {
            return (float) this.red / 255.0F;
        }

        @Override
        public float getValueY() {
            return (float) this.green / 255.0F;
        }

        @Override
        public float getValueZ() {
            return (float) this.blue / 255.0F;
        }
    }

    public interface ParticleColor {

        float getValueX();


        float getValueY();


        float getValueZ();
    }

    public static final class BlockData extends ParticleData {

        public BlockData(Material material, byte data) {
            super(material, data);

            if (!material.isBlock()) {
                throw new IllegalArgumentException("The material is not a block");
            }
        }
    }

    public static final class ItemData extends ParticleData {

        public ItemData(Material material, byte data) {
            super(material, data);
        }
    }

    public abstract static class ParticleData {
        private final Material material;
        private final byte data;
        private final int[] packetData;


        public ParticleData(Material material, byte data) {
            this.material = material;
            this.data = data;
            this.packetData = new int[]{
                    material.getId(),
                    data
            };
        }


        public Material getMaterial() {
            return this.material;
        }


        public byte getData() {
            return this.data;
        }


        public int[] getPacketData() {
            return this.packetData;
        }

        @Override
        public String toString() {
            return "_" + this.packetData[0] + "_" + this.packetData[1];
        }
    }

    public enum ParticleProperty {
        REQUIRES_WATER,
        REQUIRES_DATA,
        DIRECTIONAL,
        COLORABLE
    }
}
