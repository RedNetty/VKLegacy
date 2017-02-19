/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.sk89q.worldguard.bukkit.WorldGuardPlugin
 *  net.milkbowl.vault.economy.Economy
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.RegisteredServiceProvider
 *  org.bukkit.plugin.ServicesManager
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package me.bpweber.practiceserver;

import java.util.logging.Logger;

import io.vawke.practice.Game;
import me.bpweber.practiceserver.anticheat.AAC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.bpweber.practiceserver.Crates.CratesMain;
import me.bpweber.practiceserver.Crates.Commands.giveCrate;
import me.bpweber.practiceserver.Crates.Commands.giveKey;
import me.bpweber.practiceserver.ModerationMechanics.ModerationMechanics;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Ban;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Banksee;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Createdrop;
import me.bpweber.practiceserver.ModerationMechanics.Commands.FixItem;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Invsee;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Mute;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Setrank;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Speed;
import me.bpweber.practiceserver.ModerationMechanics.Commands.StaffChat;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Tellall;
import me.bpweber.practiceserver.ModerationMechanics.Commands.ToggleTrail;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Unban;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Unmute;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Vanish;
import me.bpweber.practiceserver.chat.ChatMechanics;
import me.bpweber.practiceserver.damage.Damage;
import me.bpweber.practiceserver.damage.Staffs;
import me.bpweber.practiceserver.drops.Mobdrops;
import me.bpweber.practiceserver.enchants.Enchants;
import me.bpweber.practiceserver.enchants.Orbs;
import me.bpweber.practiceserver.item.Durability;
import me.bpweber.practiceserver.item.Repairing;
import me.bpweber.practiceserver.item.Untradeable;
import me.bpweber.practiceserver.loot.LootChests;
import me.bpweber.practiceserver.mobs.Mobs;
import me.bpweber.practiceserver.mobs.Spawners;
import me.bpweber.practiceserver.money.Banks;
import me.bpweber.practiceserver.money.GemPouches;
import me.bpweber.practiceserver.money.Commands.Givepouch;
import me.bpweber.practiceserver.money.Economy.Economy;
import me.bpweber.practiceserver.party.Parties;
import me.bpweber.practiceserver.player.Buddies;
import me.bpweber.practiceserver.player.Energy;
import me.bpweber.practiceserver.player.Horses;
import me.bpweber.practiceserver.player.Listeners;
import me.bpweber.practiceserver.player.Speedfish;
import me.bpweber.practiceserver.player.Toggles;
import me.bpweber.practiceserver.player.Trading;
import me.bpweber.practiceserver.profession.Mining;
import me.bpweber.practiceserver.profession.ProfessionMechanics;
import me.bpweber.practiceserver.pvp.Alignments;
import me.bpweber.practiceserver.pvp.Respawn;
import me.bpweber.practiceserver.teleport.Hearthstone;
import me.bpweber.practiceserver.teleport.TeleportBooks;
import me.bpweber.practiceserver.vendors.ItemVendors;
import me.bpweber.practiceserver.vendors.MerchantMechanics;
import me.bpweber.practiceserver.world.Antibuild;
import me.bpweber.practiceserver.world.Logout;

/**
 * Jaxon's native Dungeon Realms practice server code.
 *
 * @author Jaxon (Red29 - uncureableAutism@outlook.com)
 * @author Giovanni (VawkeNetty - development@vawke.io)
 *
 * Original Authors ->
 *  - I Can't Code (BPWeber - Naughty, Naughty, Naughty)
 *  - Randal Gay Boy (iFamasssRAWRxD - Hentai, Hentai, Hentai)
 *
 * Updated to Minecraft 1.9 ->
 *  - Written by Giovanni (VawkeNetty) 2017.
 *
 * Development continued by ->
 *  - Written by Jaxon (Red29) 2016/2017.
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
    private static AAC aac;

    private Game game;


    public void onEnable() {
        plugin = this;
        ((World) Bukkit.getWorlds().get(0)).setAutoSave(false);
        ((World) Bukkit.getWorlds().get(0)).setTime(14000);
        ((World) Bukkit.getWorlds().get(0)).setGameRuleValue("doDaylightCycle", "false");
        new BukkitRunnable() {

            public void run() {
                Bukkit.getServer().getOnlinePlayers().forEach(Player::saveData);
            }
        }.runTaskTimerAsynchronously((Plugin) this, 6000, 6000);
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }
        log = plugin.getLogger();
        this.getCommand("tellall").setExecutor((CommandExecutor) new Tellall());
		this.getCommand("gl").setExecutor((CommandExecutor) new ChatMechanics());
		this.getCommand("givePouch").setExecutor((CommandExecutor) new Givepouch());
		this.getCommand("message").setExecutor((CommandExecutor) new ChatMechanics());
		this.getCommand("reply").setExecutor((CommandExecutor) new ChatMechanics());
		this.getCommand("roll").setExecutor((CommandExecutor) new ChatMechanics());
		this.getCommand("toggle").setExecutor((CommandExecutor) new Toggles());
		this.getCommand("togglepvp").setExecutor((CommandExecutor) new Toggles());
		this.getCommand("togglechaos").setExecutor((CommandExecutor) new Toggles());
		this.getCommand("toggleff").setExecutor((CommandExecutor) new Toggles());
		this.getCommand("toggledebug").setExecutor((CommandExecutor) new Toggles());
		this.getCommand("add").setExecutor((CommandExecutor) new Buddies());
		this.getCommand("del").setExecutor((CommandExecutor) new Buddies());
		this.getCommand("logout").setExecutor((CommandExecutor) new Logout());
		this.getCommand("sync").setExecutor((CommandExecutor) new Logout());
		this.getCommand("setrank").setExecutor((CommandExecutor) new Setrank());
		this.getCommand("psban").setExecutor((CommandExecutor) new Ban());
		this.getCommand("psunban").setExecutor((CommandExecutor) new Unban());
		this.getCommand("psmute").setExecutor((CommandExecutor) new Mute());
		this.getCommand("psunmute").setExecutor((CommandExecutor) new Unmute());
		this.getCommand("banksee").setExecutor((CommandExecutor) new Banksee());
		this.getCommand("psvanish").setExecutor((CommandExecutor) new Vanish());
		this.getCommand("toggletrail").setExecutor((CommandExecutor) new ToggleTrail());
		this.getCommand("invsee").setExecutor((CommandExecutor) new Invsee());
		this.getCommand("speed").setExecutor((CommandExecutor) new Speed());
		this.getCommand("createdrop").setExecutor((CommandExecutor) new Createdrop());
		this.getCommand("sc").setExecutor((CommandExecutor) new StaffChat());
		this.getCommand("showms").setExecutor((CommandExecutor) new Spawners());
		this.getCommand("hidems").setExecutor((CommandExecutor) new Spawners());
		this.getCommand("killall").setExecutor((CommandExecutor) new Spawners());
		this.getCommand("monspawn").setExecutor((CommandExecutor) new Spawners());
		this.getCommand("showloot").setExecutor((CommandExecutor) new LootChests());
		this.getCommand("hideloot").setExecutor((CommandExecutor) new LootChests());
		this.getCommand("pinvite").setExecutor((CommandExecutor) new Parties());
		this.getCommand("paccept").setExecutor((CommandExecutor) new Parties());
		this.getCommand("pkick").setExecutor((CommandExecutor) new Parties());
		this.getCommand("pquit").setExecutor((CommandExecutor) new Parties());
		this.getCommand("pdecline").setExecutor((CommandExecutor) new Parties());
		this.getCommand("p").setExecutor((CommandExecutor) new Parties());
		this.getCommand("giveKey").setExecutor((CommandExecutor) new giveKey());
		this.getCommand("giveCrate").setExecutor((CommandExecutor) new giveCrate());
		this.getCommand("dump").setExecutor((CommandExecutor) new FixItem());


        // Start VawkeNetty Game
        //this.game = new Game(this);


        cm = new CratesMain();
        trading = new Trading();
        alignments = new Alignments();
        antibuild = new Antibuild();
        banks = new Banks();
        buddies = new Buddies();
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
        aac = new AAC();
        aac.onEnable();
        alignments.onEnable();
        antibuild.onEnable();
        banks.onEnable();
        buddies.onEnable();
        chatMechanics.onEnable();
        damage.onEnable();
        durability.onEnable();
        enchants.onEnable();
        energy.onEnable();
        gemPouches.onEnable();
        hearthstone.onEnable();
        horses.onEnable();
        itemVendors.onEnable();
        cm.onEnable();
        listeners.onEnable();
        logout.onEnable();
        lootChests.onEnable();
        merchantMechanics.onEnable();
        mining.onEnable();
        mobdrops.onEnable();
        mobs.onEnable();
        moderationMechanics.onEnable();
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
    }

    public void onDisable() {
       // this.game.shutdownHook();
        aac.onDisable();
        trading.onDisable();
        em.onDisable();
        alignments.onDisable();
        antibuild.onDisable();
        banks.onDisable();
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
        lootChests.onDisable();
        merchantMechanics.onDisable();
        mining.onDisable();
        mobdrops.onDisable();
        mobs.onDisable();
        moderationMechanics.onDisable();
        orbs.onDisable();
        parties.onDisable();
        professionMechanics.onDisable();
        repairing.onDisable();
        cm.onDisable();
        respawn.onDisable();
        spawners.onDisable();
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
