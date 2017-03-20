package me.kayaba.guilds.api.basic;

import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.enums.*;
import org.bukkit.entity.*;

import java.util.*;

public interface GPlayer extends Resource {

    Player getPlayer();


    Guild getGuild();


    String getName();


    List<Guild> getInvitedTo();


    RegionSelection getActiveSelection();


    GRegion getAtRegion();


    int getPoints();


    int getDeaths();


    int getKills();


    double getKillDeathRate();


    double getMoney();


    TabList getTabList();


    CommandExecutorHandler getCommandExecutorHandler();


    Raid getPartRaid();


    GUIInventory getGuiInventory();


    List<GUIInventory> getGuiInventoryHistory();


    GRank getGuildRank();


    int getId();


    void setGuild(Guild guild);


    Preferences getPreferences();


    void setName(String name);


    void setInvitedTo(Collection<Guild> invitedTo);


    void setActiveSelection(RegionSelection selection);


    void setAtRegion(GRegion region);


    void setPoints(int points);


    void setDeaths(int deaths);


    void setKills(int kills);


    void setTabList(TabList tabList);


    void setPartRaid(Raid partRaid);


    void setGuiInventory(GUIInventory guiInventory);


    void setGuildRank(GRank guildRank);


    void setId(int id);


    boolean hasGuild();


    boolean isOnline();


    boolean isInvitedTo(Guild guild);


    boolean isPartRaid();


    boolean isVehicleListed(Vehicle vehicle);


    boolean isLeader();


    boolean isAtRegion();


    boolean hasMoney(double money);


    boolean hasPermission(GuildPermission permission);


    boolean hasTabList();


    boolean canGetKillPoints(Player player);


    void addInvitation(Guild guild);


    void addPoints(int points);


    void addKill();


    void addDeath();


    void addMoney(double money);


    void addKillHistory(Player player);


    void addVehicle(Vehicle vehicle);


    void newCommandExecutorHandler(CommandWrapper command, String[] args);


    void deleteInvitation(Guild guild);


    void takePoints(int points);


    void takeMoney(double money);


    void cancelToolProgress();


    void removeCommandExecutorHandler();


    void removeLastGUIInventoryHistory();

    interface Preferences {

        boolean getBypass();


        ChatMode getChatMode();


        boolean isCompassPointingGuild();


        RegionMode getRegionMode();


        boolean getRegionSpectate();


        boolean getSpyMode();


        void setBypass(boolean bypass);


        void setChatMode(ChatMode chatMode);


        void setCompassPointingGuild(boolean compassPointingGuild);


        void setSpyMode(boolean spyMode);


        void setRegionMode(RegionMode regionMode);


        void setRegionSpectate(boolean regionSpectate);


        void toggleBypass();


        void toggleRegionSpectate();


        void toggleSpyMode();


        void toggleCompassPointingGuild();
    }
}
