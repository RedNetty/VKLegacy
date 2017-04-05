package me.bpweber.practiceserver;

import me.bpweber.practiceserver.DonationMechanics.Commands.*;
import me.bpweber.practiceserver.DonationMechanics.Crates.*;
import me.bpweber.practiceserver.DonationMechanics.Nametags.*;
import me.bpweber.practiceserver.ModerationMechanics.Commands.*;
import me.bpweber.practiceserver.ModerationMechanics.*;
import me.bpweber.practiceserver.chat.*;
import me.bpweber.practiceserver.damage.*;
import me.bpweber.practiceserver.drops.*;
import me.bpweber.practiceserver.enchants.*;
import me.bpweber.practiceserver.item.*;
import me.bpweber.practiceserver.loot.*;
import me.bpweber.practiceserver.mobs.*;
import me.bpweber.practiceserver.money.*;
import me.bpweber.practiceserver.money.Commands.*;
import me.bpweber.practiceserver.money.Economy.*;
import me.bpweber.practiceserver.party.*;
import me.bpweber.practiceserver.player.*;
import me.bpweber.practiceserver.player.GamePlayer.Commands.*;
import me.bpweber.practiceserver.player.GamePlayer.*;
import me.bpweber.practiceserver.player.Stats.*;
import me.bpweber.practiceserver.player.Tutorial.Commands.*;
import me.bpweber.practiceserver.player.Tutorial.*;
import me.bpweber.practiceserver.profession.*;
import me.bpweber.practiceserver.pvp.*;
import me.bpweber.practiceserver.teleport.*;
import me.bpweber.practiceserver.vendors.*;
import me.bpweber.practiceserver.world.*;
import me.kayaba.guilds.api.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.event.PlayerInteractEntityEvent;
import me.kayaba.guilds.api.manager.ErrorManager;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.api.util.packet.*;
import me.kayaba.guilds.api.util.reflect.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.exception.*;
import me.kayaba.guilds.impl.storage.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.impl.util.bossbar.*;
import me.kayaba.guilds.impl.util.logging.*;
import me.kayaba.guilds.impl.versionimpl.v1_8_R1.*;
import me.kayaba.guilds.listener.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.reflect.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.*;
import org.bukkit.scheduler.*;

import java.lang.reflect.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

/**
 * Jaxson's native Dungeon Realms practice server code.
 *
 * @author Jaxson (Red29 - uncureableAutism@outlook.com)
 * @author Giovanni (VawkeNetty - development@vawke.io)
 *         <p>
 *         Original Authors ->
 *         - I Can't Code (BPWeber - Naughty, Naughty, Naughty)
 *         - Randal Gay Boy (iFamasssRAWRxD - Hentai, Hentai, Hentai)
 *         <p>
 *         Updated to Minecraft 1.9 ->
 *         - Written by Giovanni (VawkeNetty) 2017.
 *         - Written by Jaxson (Red29) 2016/2017.
 *         <p>
 *         Development continued by ->
 *         - Written by Jaxson (Red29) 2016/2017.
 *         - Written by Brandon (Kayaba) 2017
 */


public class PracticeServer extends JavaPlugin implements GuildsAPI {

    public static Plugin plugin;
    public static Logger log;
    private static Alignments alignments;
    private static Antibuild antibuild;
    private static Banks banks;
    private static Buddies buddies;
    private static ChatMechanics chatMechanics;
    private static Damage damage;
    private static Durability durability;
    private static Enchants enchants;
    private static Energy energy;
    private static GemPouches gemPouches;
    private static Hearthstone hearthstone;
    private static Horses horses;
    private static ItemVendors itemVendors;
    private static Listeners listeners;
    private static Logout logout;
    private static LootChests lootChests;
    private static MerchantMechanics merchantMechanics;
    private static Mining mining;
    private static Mobdrops mobdrops;
    private static Mobs mobs;
    private static ModerationMechanics moderationMechanics;
    private static Orbs orbs;
    private static Parties parties;
    private static ProfessionMechanics professionMechanics;
    private static Repairing repairing;
    private static Respawn respawn;
    private static Spawners spawners;
    private static Speedfish speedfish;
    private static Staffs staffs;
    private static TeleportBooks teleportBooks;
    private static Toggles toggles;
    private static Untradeable untradeable;
    private static Trading trading;
    private static CratesMain cm;
    private static Economy em;
    private static ForceField ff;
    private static Nametag nt;
    private static StatsMain stat;
    private static GamePlayer gap;
    private static TutorialMain tut;
    private static PracticeServer instance;


    private final DependencyManager dependencyManager;
    private final ListenerManager listenerManager;
    private final CommandManager commandManager;
    private final MessageManager messageManager;
    private final RegionManager regionManager;
    private final PlayerManager playerManager;
    private final ConfigManager configManager;
    private final ErrorManager errorManager;
    private final GuildManager guildManager;
    private final GroupManager groupManager;
    private final RankManager rankManager;
    private final TaskManager taskManager;

    private PacketExtension packetExtension;
    private Storage storage;
    private SignGUI signGUI;
    private final Map<ConfigManager.ServerVersion, Constructor<? extends TabList>> tabListConstructorMap = new HashMap<>();

    private boolean messageGlobalized = false;


    public static PracticeServer getInstance() {
        return instance;
    }

    public PracticeServer() {

        instance = this; // Making sure this shit will work.
        dependencyManager = new DependencyManager();
        listenerManager = new ListenerManager();
        messageManager = new MessageManager();
        commandManager = new CommandManager();
        regionManager = new RegionManager();
        playerManager = new PlayerManager();
        configManager = new ConfigManager();
        errorManager = new ErrorManagerImpl();
        guildManager = new GuildManager();
        groupManager = new GroupManager();
        rankManager = new RankManager();
        taskManager = new TaskManager();
    }

    @Override
    public void onLoad() {
        System.out.println("Stuff is loading...");
        try {
            getConfigManager().reload();
            getDependencyManager().setUp();
        } catch (Exception e) {
            LoggerUtils.exception(e);
        }
        System.out.println("Stuff is done!");
    }

    @Override
    public void onEnable() {
        plugin = this;

        try {
            getMessageManager().load();
        } catch (FatalKayabaGuildsException e) {
            e.printStackTrace();
        }
        getCommandManager().setUp();
        getGroupManager().load();
        getListenerManager().registerListeners();

        try {
            setupWrappedLogger();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            setUpStorage();
        } catch (FatalKayabaGuildsException e) {
            e.printStackTrace();
        }


        getGuildManager().load();
        LoggerUtils.info("Guilds data loaded");
        getRegionManager().load();
        LoggerUtils.info("Regions data loaded");
        getRankManager().loadDefaultRanks();
        getPlayerManager().load();
        LoggerUtils.info("Players data loaded");

        LoggerUtils.info("Post checks running");
        getGuildManager().postCheck();

        getRankManager().load();
        LoggerUtils.info("Ranks data loaded");

        ConfigManager.ServerVersion serverVersion = ConfigManager.getServerVersion();

        switch (serverVersion) {
            case MINECRAFT_1_8_R1:
            case MINECRAFT_1_8_R2:
            case MINECRAFT_1_8_R3:
            case MINECRAFT_1_9_R1:
            case MINECRAFT_1_9_R2:
            case MINECRAFT_1_10_R1:
            case MINECRAFT_1_10_R2:
            case MINECRAFT_1_11_R1:
            default:
                packetExtension = new me.kayaba.guilds.impl.versionimpl.v1_8_R3.PacketExtensionImpl();
                break;
        }


        if (Config.SIGNGUI_ENABLED.getBoolean()) {
            switch (serverVersion) {
                case MINECRAFT_1_8_R1:
                    signGUI = new SignGUIImpl();
                    break;
                case MINECRAFT_1_8_R2:
                case MINECRAFT_1_8_R3:
                    signGUI = new me.kayaba.guilds.impl.versionimpl.v1_8_R3.SignGUIImpl();
                    break;
                case MINECRAFT_1_9_R1:
                    signGUI = new me.kayaba.guilds.impl.versionimpl.v1_9_R1.SignGUIImpl();
                    break;
                case MINECRAFT_1_9_R2:
                case MINECRAFT_1_10_R1:
                case MINECRAFT_1_10_R2:
                case MINECRAFT_1_11_R1:
                default:
                    signGUI = new me.kayaba.guilds.impl.versionimpl.v1_9_R2.SignGUIImpl();
                    break;
            }
        }

        if (Config.TABLIST_ENABLED.getBoolean()) {
            final Map<ConfigManager.ServerVersion, Class<? extends TabList>> tabListClassMap = new HashMap<>();
            tabListClassMap.put(ConfigManager.ServerVersion.MINECRAFT_1_8_R1, me.kayaba.guilds.impl.versionimpl.v1_8_R1.TabListImpl.class);
            tabListClassMap.put(ConfigManager.ServerVersion.MINECRAFT_1_8_R2, me.kayaba.guilds.impl.versionimpl.v1_8_R3.TabListImpl.class);
            tabListClassMap.put(ConfigManager.ServerVersion.MINECRAFT_1_8_R3, me.kayaba.guilds.impl.versionimpl.v1_8_R3.TabListImpl.class);
            tabListClassMap.put(ConfigManager.ServerVersion.MINECRAFT_1_9_R1, me.kayaba.guilds.impl.versionimpl.v1_8_R3.TabListImpl.class);
            tabListClassMap.put(ConfigManager.ServerVersion.MINECRAFT_1_9_R2, me.kayaba.guilds.impl.versionimpl.v1_8_R3.TabListImpl.class);
            tabListClassMap.put(ConfigManager.ServerVersion.MINECRAFT_1_10_R1, me.kayaba.guilds.impl.versionimpl.v1_10_R1.TabListImpl.class);
            tabListClassMap.put(ConfigManager.ServerVersion.MINECRAFT_1_11_R1, me.kayaba.guilds.impl.versionimpl.v1_10_R1.TabListImpl.class);

            for (ConfigManager.ServerVersion version : ConfigManager.ServerVersion.values()) {
                Class<? extends TabList> tabListClass = tabListClassMap.get(version);
                Constructor<? extends TabList> tabListConstructor = null;

                if (tabListClass != null) {
                    try {
                        tabListConstructor = tabListClass.getConstructor(GPlayer.class);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }

                tabListConstructorMap.put(version, tabListConstructor);
            }

            if (tabListConstructorMap.get(serverVersion) == null) {
                Config.TABLIST_ENABLED.set(false);
                LoggerUtils.error("TabList not found for version " + serverVersion.getString());
            }
        }


        for (Player p : CompatibilityUtils.getOnlinePlayers()) {
            getPacketExtension().registerPlayer(p);
        }

        if (!Config.ADVANCEDENTITYUSE.getBoolean()) {
            new AbstractListener() {
                @EventHandler(priority = EventPriority.LOWEST)
                public void onPlayerInteractEntity(org.bukkit.event.player.PlayerInteractEntityEvent event) {
                    PlayerInteractEntityEvent clickEvent = new PlayerInteractEntityEvent(event.getPlayer(), event.getRightClicked(), EntityUseAction.INTERACT);
                    getServer().getPluginManager().callEvent(clickEvent);
                    event.setCancelled(clickEvent.isCancelled());
                }
            };

            if (ConfigManager.getServerVersion().isNewerThan(ConfigManager.ServerVersion.MINECRAFT_1_7_R4)) {
                new AbstractListener() {
                    @EventHandler(priority = EventPriority.LOWEST)
                    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
                        PlayerInteractEntityEvent interactEntityEvent = new PlayerInteractEntityEvent(event.getPlayer(), event.getRightClicked(), EntityUseAction.INTERACT_AT);
                        ListenerManager.getLoggedPluginManager().callEvent(interactEntityEvent);
                        event.setCancelled(interactEntityEvent.isCancelled());
                    }
                };
            }
        }

        if (signGUI == null) {
            Config.SIGNGUI_ENABLED.set(false);
        }

        if (getDependencyManager().isEnabled(Dependency.VANISHNOPACKET)) {
            new VanishListener();
        }


        TagUtils.refresh();
        TabUtils.refresh();

        try {
            FieldAccessor<Boolean> acceptingNewField = Reflections.getField(Enchantment.class, "acceptingNew", boolean.class);
            acceptingNewField.set(true);
            Enchantment.registerEnchantment(new EnchantmentGlow());
            acceptingNewField.set(false);
        } catch (Exception e) {
            LoggerUtils.exception(e);
        }


        getTaskManager().runTasks();
        Bukkit.getWorlds().get(0).setAutoSave(false);
        Bukkit.getWorlds().get(0).setTime(14000);
        Bukkit.getWorlds().get(0).setGameRuleValue("doDaylightCycle", "false");
        new BukkitRunnable() {

            public void run() {
                Bukkit.getServer().getOnlinePlayers().forEach(Player::saveData);
            }
        }.runTaskTimerAsynchronously(this, 6000, 6000);
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }
        log = plugin.getLogger();
        this.getCommand("reboot").setExecutor(new Reboot());
        this.getCommand("tellall").setExecutor(new Tellall());
        this.getCommand("gl").setExecutor(new ChatMechanics());
        this.getCommand("givePouch").setExecutor(new Givepouch());
        this.getCommand("message").setExecutor(new ChatMechanics());
        this.getCommand("reply").setExecutor(new ChatMechanics());
        this.getCommand("roll").setExecutor(new ChatMechanics());
        this.getCommand("toggle").setExecutor(new Toggles());
        this.getCommand("togglepvp").setExecutor(new Toggles());
        this.getCommand("togglechaos").setExecutor(new Toggles());
        this.getCommand("toggleff").setExecutor(new Toggles());
        this.getCommand("toggledebug").setExecutor(new Toggles());
        this.getCommand("add").setExecutor(new Buddies());
        this.getCommand("del").setExecutor(new Buddies());
        this.getCommand("logout").setExecutor(new Logout());
        this.getCommand("sync").setExecutor(new Logout());
        this.getCommand("setrank").setExecutor(new Setrank());
        this.getCommand("psban").setExecutor(new Ban());
        this.getCommand("psunban").setExecutor(new Unban());
        this.getCommand("psmute").setExecutor(new Mute());
        this.getCommand("psunmute").setExecutor(new Unmute());
        this.getCommand("banksee").setExecutor(new Banksee());
        this.getCommand("psvanish").setExecutor(new Vanish());
        this.getCommand("toggletrail").setExecutor(new ToggleTrail());
        this.getCommand("toggleholodmg").setExecutor(new Toggles());
        this.getCommand("invsee").setExecutor(new Invsee());
        this.getCommand("giveNameTag").setExecutor(new giveNameTag());
        this.getCommand("speed").setExecutor(new Speed());
        this.getCommand("createdrop").setExecutor(new Createdrop());
        this.getCommand("sc").setExecutor(new StaffChat());
        this.getCommand("showms").setExecutor(new Spawners());
        this.getCommand("hidems").setExecutor(new Spawners());
        this.getCommand("killall").setExecutor(new Spawners());
        this.getCommand("monspawn").setExecutor(new Spawners());
        this.getCommand("showloot").setExecutor(new LootChests());
        this.getCommand("hideloot").setExecutor(new LootChests());
        this.getCommand("pinvite").setExecutor(new Parties());
        this.getCommand("paccept").setExecutor(new Parties());
        this.getCommand("pkick").setExecutor(new Parties());
        this.getCommand("pquit").setExecutor(new Parties());
        this.getCommand("pdecline").setExecutor(new Parties());
        this.getCommand("p").setExecutor(new Parties());
        this.getCommand("dump").setExecutor(new FixItem());
        this.getCommand("giveOrb").setExecutor(new giveOrb());
        this.getCommand("Skip").setExecutor(new Skip());
        this.getCommand("wipeall").setExecutor(new wipeAll());
        gap = new GamePlayer();
        cm = new CratesMain();
        tut = new TutorialMain();
        stat = new StatsMain();
        trading = new Trading();
        nt = new Nametag();
        alignments = new Alignments();
        antibuild = new Antibuild();
        banks = new Banks();
        buddies = new Buddies();
        ff = new ForceField();
        chatMechanics = new ChatMechanics();
        damage = new Damage();
        durability = new Durability();
        enchants = new Enchants();
        energy = new Energy();
        gemPouches = new GemPouches();
        hearthstone = new Hearthstone();
        horses = new Horses();
        itemVendors = new ItemVendors();
        listeners = new Listeners();
        em = new Economy();
        logout = new Logout();
        lootChests = new LootChests();
        merchantMechanics = new MerchantMechanics();
        mining = new Mining();
        mobdrops = new Mobdrops();
        mobs = new Mobs();
        moderationMechanics = new ModerationMechanics();
        orbs = new Orbs();
        parties = new Parties();
        professionMechanics = new ProfessionMechanics();
        repairing = new Repairing();
        respawn = new Respawn();
        spawners = new Spawners();
        speedfish = new Speedfish();
        staffs = new Staffs();
        teleportBooks = new TeleportBooks();
        toggles = new Toggles();
        untradeable = new Untradeable();
        gap.onEnable();
        alignments.onEnable();
        antibuild.onEnable();
        banks.onEnable();
        moderationMechanics.onEnable();
        buddies.onEnable();
        chatMechanics.onEnable();
        damage.onEnable();
        durability.onEnable();
        enchants.onEnable();
        nt.onEnable();
        energy.onEnable();
        gemPouches.onEnable();
        hearthstone.onEnable();
        stat.onEnable();
        horses.onEnable();
        itemVendors.onEnable();
        ff.onEnable();
        cm.onEnable();
        listeners.onEnable();
        logout.onEnable();
        lootChests.onEnable();
        merchantMechanics.onEnable();
        mining.onEnable();
        mobdrops.onEnable();
        tut.onEnable();
        mobs.onEnable();
        orbs.onEnable();
        parties.onEnable();
        professionMechanics.onEnable();
        repairing.onEnable();
        respawn.onEnable();
        spawners.onEnable();
        speedfish.onEnable();
        staffs.onEnable();
        teleportBooks.onEnable();
        em.onEnable();
        toggles.onEnable();
        trading.onEnable();
        untradeable.onEnable();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            String dayNames[] = new DateFormatSymbols().getWeekdays();
            Calendar date1 = Calendar.getInstance();
            if(dayNames[date1.get(Calendar.DAY_OF_WEEK)] == "Saturday" && !messageGlobalized)
            {
                messageGlobalized = true;
                Bukkit.broadcastMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + ">>> " + ChatColor.AQUA + "The Guild War is now over! You can redeem your current guild points at Cyrennica's Guild God!");
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    pl.playSound(pl.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
                    pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                }
                for(Guild guild : this.getGuildManager().getGuilds())
                {
                    Collection<Guild> empty = new ArrayList<Guild>();
                    guild.setWars(empty);
                }
            }
        }, 0L, 20L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            String dayNames[] = new DateFormatSymbols().getWeekdays();
            Calendar date1 = Calendar.getInstance();
            if(dayNames[date1.get(Calendar.DAY_OF_WEEK)] != "Saturday" && dayNames[date1.get(Calendar.DAY_OF_WEEK)] != "Sunday" && messageGlobalized)
            {
                messageGlobalized = false; // Reset the variable..
            }
        }, 0L, 20L);
    }

    public void onDisable() {
        trading.onDisable();
        em.onDisable();
        alignments.onDisable();
        antibuild.onDisable();
        banks.onDisable();
        lootChests.onEnable();
        spawners.onDisable();
        buddies.onDisable();
        chatMechanics.onDisable();
        damage.onDisable();
        durability.onDisable();
        enchants.onDisable();
        energy.onDisable();
        gemPouches.onDisable();
        hearthstone.onDisable();
        horses.onDisable();
        itemVendors.onDisable();
        listeners.onDisable();
        logout.onDisable();
        merchantMechanics.onDisable();
        mining.onDisable();
        mobdrops.onDisable();
        mobs.onDisable();
        moderationMechanics.onDisable();
        orbs.onDisable();
        stat.onDisable();
        parties.onDisable();
        professionMechanics.onDisable();
        repairing.onDisable();
        cm.onDisable();
        respawn.onDisable();
        speedfish.onDisable();
        staffs.onDisable();
        teleportBooks.onDisable();
        toggles.onDisable();
        untradeable.onDisable();
        if (FatalKayabaGuildsException.fatal) {
            return;
        }

        getTaskManager().stopTasks();
        getGuildManager().save();
        getRegionManager().save();
        getPlayerManager().save();
        getRankManager().save();
        LoggerUtils.info("Saved all data");

        if (getPacketExtension() != null) {
            getPacketExtension().unregisterChannel();
        }

        if (getSignGUI() != null) {
            getSignGUI().destroy();
        }


        if (Config.BOSSBAR_ENABLED.getBoolean()) {
            for (Player player : CompatibilityUtils.getOnlinePlayers()) {
                BossBarUtils.removeBar(player);
            }
        }

        for (Player p : CompatibilityUtils.getOnlinePlayers()) {
            PlayerManager.getPlayer(p).cancelToolProgress();
        }

        for (GPlayer nPlayer : getPlayerManager().getPlayers()) {
            if (nPlayer.getActiveSelection() != null) {
                nPlayer.getActiveSelection().reset();
            }
        }
        plugin = null;
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (Alignments.tagged.containsKey(p.getName())) {
                Alignments.tagged.remove(p.getName());
            }
            p.saveData();
            p.kickPlayer(String.valueOf(ChatColor.GREEN.toString()) + "You have been safely logged out by the server." + "\n\n" + ChatColor.GRAY.toString() + "Your player data has been synced.");
        }
    }

    @Override
    public GuildManager getGuildManager() {
        return guildManager;
    }

    @Override
    public RegionManager getRegionManager() {
        return regionManager;
    }

    @Override
    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    @Override
    public CommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public MessageManager getMessageManager() {
        return messageManager;
    }

    @Override
    public ConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public GroupManager getGroupManager() {
        return groupManager;
    }

    @Override
    public TaskManager getTaskManager() {
        return taskManager;
    }

    @Override
    public ListenerManager getListenerManager() {
        return listenerManager;
    }

    @Override
    public ErrorManager getErrorManager() {
        return errorManager;
    }

    @Override
    public Storage getStorage() {
        return storage;
    }

    @Override
    public TabList createTabList(ConfigManager.ServerVersion serverVersion, GPlayer nPlayer) {
        if (!Config.TABLIST_ENABLED.getBoolean()) {
            throw new IllegalArgumentException("TabList is disabled");
        }

        try {
            return tabListConstructorMap.get(serverVersion).newInstance(nPlayer);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            LoggerUtils.exception(e);
            Config.TABLIST_ENABLED.set(false);
            return null;
        }
    }

    @Override
    public TabList createTabList(GPlayer nPlayer) {
        return createTabList(ConfigManager.getServerVersion(), nPlayer);
    }

    @Override
    public RankManager getRankManager() {
        return rankManager;
    }

    @Override
    public PacketExtension getPacketExtension() {
        return packetExtension;
    }

    @Override
    public DependencyManager getDependencyManager() {
        return dependencyManager;
    }

    public void setUpStorage() throws FatalKayabaGuildsException {
        try {
            storage = new StorageConnector(getConfigManager().getDataStorageType()).getStorage();
        } catch (StorageConnectionFailedException | IllegalArgumentException e) {
            if (e instanceof IllegalArgumentException) {
                if (e.getCause() == null || !(e.getCause() instanceof StorageConnectionFailedException)) {
                    throw (IllegalArgumentException) e;
                }

                LoggerUtils.error(e.getMessage());
            }

            if (getConfigManager().isSecondaryDataStorageType()) {
                throw new FatalKayabaGuildsException("Storage connection failed", e);
            }

            getConfigManager().setToSecondaryDataStorageType();
            setUpStorage();
        }
    }


    public static void runTaskLater(Runnable runnable, long delay, TimeUnit timeUnit) {
        Bukkit.getScheduler().runTaskLater(instance, runnable, timeUnit.toSeconds(delay) * 20);
    }


    public static void runTask(Runnable runnable) {
        Bukkit.getScheduler().runTask(instance, runnable);
    }


    public SignGUI getSignGUI() {
        return signGUI;
    }


    private void setupWrappedLogger() throws NoSuchFieldException, IllegalAccessException {
        FieldAccessor<PluginLogger> loggerField = Reflections.getField(JavaPlugin.class, "logger", PluginLogger.class);
        loggerField.set(this, new WrappedLogger(this));
    }

}

