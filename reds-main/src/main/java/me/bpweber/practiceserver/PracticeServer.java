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
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.*;
import org.bukkit.scheduler.*;

import java.text.*;
import java.util.*;
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


public class PracticeServer extends JavaPlugin {

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

    private boolean messageGlobalized = false;


    public static PracticeServer getInstance() {
        return instance;
    }

    public PracticeServer() {
        instance = this; // Making sure this shit will work.
    }

    @Override
    public void onEnable() {
        plugin = this;

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

        plugin = null;
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (Alignments.tagged.containsKey(p.getName())) {
                Alignments.tagged.remove(p.getName());
            }
            p.saveData();
            p.kickPlayer(String.valueOf(ChatColor.GREEN.toString()) + "You have been safely logged out by the server." + "\n\n" + ChatColor.GRAY.toString() + "Your player data has been synced.");
        }
    }
}

