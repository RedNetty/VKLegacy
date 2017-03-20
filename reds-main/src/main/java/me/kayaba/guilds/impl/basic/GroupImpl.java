package me.kayaba.guilds.impl.basic;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.api.util.reflect.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.reflect.*;
import org.bukkit.inventory.*;

import java.util.*;

public class GroupImpl implements Group {
    public static class Key<T> implements Group.Key<T> {
        public static final Group.Key<Double> CREATE_MONEY = new GroupImpl.Key<>(Double.class);
        public static final Group.Key<List<ItemStack>> CREATE_ITEMS = new GroupImpl.Key<>();
        public static final Group.Key<Schematic> CREATE_SCHEMATIC = new GroupImpl.Key<>(Schematic.class);
        public static final Group.Key<Integer> HOME_DELAY = new GroupImpl.Key<>(Integer.class);
        public static final Group.Key<Double> HOME_MONEY = new GroupImpl.Key<>(Double.class);
        public static final Group.Key<List<ItemStack>> HOME_ITEMS = new GroupImpl.Key<>();
        public static final Group.Key<Double> JOIN_MONEY = new GroupImpl.Key<>(Double.class);
        public static final Group.Key<List<ItemStack>> JOIN_ITEMS = new GroupImpl.Key<>();
        public static final Group.Key<Double> EFFECT_MONEY = new GroupImpl.Key<>(Double.class);
        public static final Group.Key<List<ItemStack>> EFFECT_ITEMS = new GroupImpl.Key<>();
        public static final Group.Key<Double> BUY_LIFE_MONEY = new GroupImpl.Key<>(Double.class);
        public static final Group.Key<List<ItemStack>> BUY_LIFE_ITEMS = new GroupImpl.Key<>();
        public static final Group.Key<Double> BUY_SLOT_MONEY = new GroupImpl.Key<>(Double.class);
        public static final Group.Key<List<ItemStack>> BUY_SLOT_ITEMS = new GroupImpl.Key<>();
        public static final Group.Key<Double> BUY_BANNER_MONEY = new GroupImpl.Key<>(Double.class);
        public static final Group.Key<List<ItemStack>> BUY_BANNER_ITEMS = new GroupImpl.Key<>();

        private final Class<T> type;

        @SuppressWarnings("unchecked")
        public Key() {
            this((Class<T>) List.class);
        }

        public Key(Class<T> type) {
            this.type = type;
        }


        @Override
        @SuppressWarnings("unchecked")
        public Class<T> getType() {
            return type;
        }


        public static Group.Key<?>[] values() {
            final List<Group.Key<?>> values = new ArrayList<>();

            for (FieldAccessor<Group.Key> fieldAccessor : Reflections.getFields(GroupImpl.Key.class, Group.Key.class)) {
                values.add(fieldAccessor.get());
            }

            return values.toArray(new Group.Key<?>[values.size()]);
        }
    }

    private static final PracticeServer plugin = PracticeServer.getInstance();
    private static final Map<Group.Key<?>, String> paths = new HashMap<Group.Key<?>, String>() {{
        put(Key.CREATE_MONEY, "guild.create.money");
        put(Key.CREATE_ITEMS, "guild.create.items");
        put(Key.CREATE_SCHEMATIC, "guild.create.schematic");
        put(Key.HOME_DELAY, "guild.home.tpdelay");
        put(Key.JOIN_MONEY, "guild.join.money");
        put(Key.JOIN_ITEMS, "guild.join.items");
        put(Key.EFFECT_MONEY, "guild.effect.money");
        put(Key.EFFECT_ITEMS, "guild.effect.items");
        put(Key.BUY_LIFE_MONEY, "guild.buylife.money");
        put(Key.BUY_LIFE_ITEMS, "guild.buylife.items");
        put(Key.BUY_SLOT_MONEY, "guild.buyslot.money");
        put(Key.BUY_SLOT_ITEMS, "guild.buyslot.items");
        put(Key.BUY_BANNER_MONEY, "guild.banner.money");
        put(Key.BUY_BANNER_ITEMS, "guild.banner.items");
    }};

    private final String name;
    private final Map<Group.Key<?>, Object> values = new HashMap<>();


    public GroupImpl(String group) {
        name = group;
        LoggerUtils.info("Loading group '" + name + "'...");

        // REDCHANGETHIS
        values.put(Key.CREATE_MONEY, 5000d); // Amount in bank gems to create a guild.
        values.put(Key.BUY_LIFE_MONEY, 5000d); // Amount in bank gems to buy a life.
        values.put(Key.BUY_SLOT_MONEY, 5000d); // Amount in bank gems to buy an additional slot.
        values.put(Key.EFFECT_MONEY, 10000d); // Amount in bank gems to buy a guild effect!
        values.put(Key.JOIN_MONEY, 0d); // Amount in bank gems to join a guild. (Suggest you dont do this)
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Group.Key<T> key) {
        T value = (T) values.get(key);

        return value;
    }
}
