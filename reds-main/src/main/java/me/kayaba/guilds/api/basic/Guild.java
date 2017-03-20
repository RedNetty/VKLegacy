package me.kayaba.guilds.api.basic;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.enums.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.*;

import java.util.*;

public interface Guild extends Resource {

    String getName();


    int getPoints();


    List<Guild> getAllies();


    List<Guild> getAllyInvitations();


    List<Guild> getWars();


    List<Guild> getNoWarInvitations();


    List<GPlayer> getPlayers();


    List<GPlayer> getOnlineKayabaPlayers();


    List<Player> getOnlinePlayers();


    List<GPlayer> getInvitedPlayers();


    List<GRank> getRanks();


    String getTag();


    List<GRegion> getRegions();


    GRegion getRegion(int index);


    Hologram getVaultHologram();


    Location getVaultLocation();


    Location getHome();


    GPlayer getLeader();


    int getId();


    double getMoney();


    Raid getRaid();


    int getLives();


    long getTimeRest();


    long getLostLiveTime();


    long getInactiveTime();


    long getTimeCreated();


    boolean getFriendlyPvp();


    int getSlots();


    BannerMeta getBannerMeta();


    GRank getDefaultRank();


    GRank getCloneOfGenericRank(GRank genericRank);


    void setVaultHologram(Hologram hologram);


    void setName(String name);


    void setTag(String tag);


    void setRegions(List<GRegion> region);


    void addRegion(GRegion region);


    void removeRegion(GRegion region);


    void setLeaderName(String name);


    void setVaultLocation(Location location);


    void setHome(Location location);


    void setLeader(GPlayer nPlayer);


    void setId(int id);


    void setMoney(double money);


    void setAllies(Collection<Guild> list);


    void setAllyInvitations(Collection<Guild> list);


    void setWars(Collection<Guild> list);


    void setNoWarInvitations(Collection<Guild> list);


    void setPoints(int points);


    void setOpenInvitation(boolean openInvitation);


    void updateTimeRest();


    void updateLostLive();


    void updateInactiveTime();


    void setLostLiveTime(long time);


    void setInactiveTime(long time);


    void resetLostLiveTime();


    void setTimeRest(long time);


    void setTimeCreated(long time);


    void setLives(int lives);


    void setFriendlyPvp(boolean pvp);


    void setSlots(int slots);


    void setBannerMeta(BannerMeta bannerMeta);


    void setRanks(List<GRank> list);


    boolean isAlly(Guild guild);


    boolean isInvitedToAlly(Guild guild);


    boolean isWarWith(Guild guild);


    boolean isNoWarInvited(Guild guild);


    boolean isLeader(GPlayer nPlayer);


    boolean hasRegion();


    boolean ownsRegion(GRegion region);


    boolean isRaid();


    boolean isFull();


    boolean isOpenInvitation();


    boolean isMember(GPlayer nPlayer);


    boolean hasMoney(double money);


    void addAlly(Guild guild);


    void addAllyInvitation(Guild guild);


    void addWar(Guild guild);


    void addNoWarInvitation(Guild guild);


    void addPlayer(GPlayer nPlayer);


    void addMoney(double money);


    void addPoints(int points);


    void addSlot();


    void addRank(GRank rank);


    void addLive();


    void removePlayer(GPlayer nPlayer);


    void removeAlly(Guild guild);


    void removeWar(Guild guild);


    void removeNoWarInvitation(Guild guild);


    void removeRank(GRank rank);


    void takeMoney(double money);


    void takePoints(int points);


    void takeLive();


    void removeAllyInvitation(Guild guild);


    void createRaid(Guild attacker);


    @Deprecated
    void destroy();


    void destroy(AbandonCause cause);


    void showVaultHologram(Player player);


    void hideVaultHologram(Player player);


    void removeRaidBar();


    void postSetUp();


    LoadingWrapper getLoadingWrapper();

    interface LoadingWrapper<T> {

        void setAllies(Collection<T> list);


        void setAllyInvitations(Collection<T> list);


        void setWars(Collection<T> list);


        void setNoWarInvitations(Collection<T> list);


        Collection<T> getAllies();


        Collection<T> getAllyInvitations();


        Collection<T> getWars();


        Collection<T> getNoWarInvitations();


        Collection<Guild> convert(Collection<T> list);
    }
}
