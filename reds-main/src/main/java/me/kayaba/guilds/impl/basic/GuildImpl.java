package me.kayaba.guilds.impl.basic;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.event.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.impl.util.bossbar.*;
import me.kayaba.guilds.impl.util.converter.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.NumberUtils;
import org.apache.commons.lang.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.*;

import java.util.*;

public class GuildImpl extends AbstractResource implements Guild {
    private int id;
    private String name;
    private String tag;
    private final List<GRegion> regions = new ArrayList<>();
    private String leaderName;
    private GPlayer leader;
    private Location homeLocation;
    private double money = 0;
    private int points;
    private Raid raid;
    private long timeRest;
    private long lostLiveTime;
    private long inactiveTime;
    private long timeCreated;
    private int lives;
    private int slots;
    private BannerMeta bannerMeta;
    private boolean openInvitation = false;
    private boolean friendlyPvp = false;
    private Location vaultLocation;
    private Hologram vaultHologram;
    private final LoadingWrapper loadingWrapper;

    private final List<GPlayer> players = new ArrayList<>();

    private final List<Guild> allies = new NonNullArrayList<>();
    private final List<Guild> allyInvitations = new NonNullArrayList<>();
    private final List<Guild> war = new NonNullArrayList<>();
    private final List<Guild> noWarInvited = new NonNullArrayList<>();
    private final List<GPlayer> invitedPlayers = new ArrayList<>();
    private final List<GRank> ranks = new ArrayList<>();


    public GuildImpl(UUID uuid) {
        this(uuid, null);
    }


    public GuildImpl(UUID uuid, LoadingWrapper loadingWrapper) {
        super(uuid);

        if (loadingWrapper != null) {
            this.loadingWrapper = loadingWrapper;
        } else {
            this.loadingWrapper = new LoadingWrapperImpl<>(new UUIDToGuildConverterImpl());
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public List<GRegion> getRegions() {
        return regions;
    }

    @Override
    public GRegion getRegion(int index) {
        if (regions.size() >= index) {
            return regions.get(index - 1);
        }

        return null;
    }

    @Override
    public Hologram getVaultHologram() {
        return vaultHologram;
    }

    @Override
    public List<Guild> getAllies() {
        return allies;
    }

    @Override
    public List<Guild> getAllyInvitations() {
        return allyInvitations;
    }

    @Override
    public List<Guild> getWars() {
        return war;
    }

    @Override
    public List<Guild> getNoWarInvitations() {
        return noWarInvited;
    }

    @Override
    public List<GPlayer> getPlayers() {
        return players;
    }

    @Override
    public List<GPlayer> getOnlineKayabaPlayers() {
        final List<GPlayer> list = new ArrayList<>();

        for (GPlayer nPlayer : getPlayers()) {
            if (nPlayer.isOnline()) {
                list.add(nPlayer);
            }
        }

        return list;
    }

    @Override
    public List<Player> getOnlinePlayers() {
        final List<Player> list = new ArrayList<>();

        for (GPlayer nPlayer : getOnlineKayabaPlayers()) {
            list.add(nPlayer.getPlayer());
        }

        return list;
    }

    @Override
    public List<GPlayer> getInvitedPlayers() {
        return invitedPlayers;
    }

    @Override
    public List<GRank> getRanks() {
        return ranks;
    }

    @Override
    public Location getVaultLocation() {
        return vaultLocation;
    }

    @Override
    public GPlayer getLeader() {
        return leader;
    }


    public String getLeaderName() {
        return leaderName;
    }

    @Override
    public Location getHome() {
        return homeLocation;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public double getMoney() {
        return money;
    }

    @Override
    public Raid getRaid() {
        return raid;
    }

    @Override
    public int getLives() {
        return lives;
    }

    @Override
    public long getTimeRest() {
        return timeRest;
    }

    @Override
    public long getLostLiveTime() {
        return lostLiveTime;
    }

    @Override
    public long getInactiveTime() {
        return inactiveTime;
    }

    @Override
    public long getTimeCreated() {
        return timeCreated;
    }

    @Override
    public boolean getFriendlyPvp() {
        return friendlyPvp;
    }

    @Override
    public int getSlots() {
        return slots;
    }

    @Override
    public BannerMeta getBannerMeta() {
        return bannerMeta;
    }

    @Override
    public GRank getDefaultRank() {
        for (GRank rank : getRanks()) {
            if (rank.isDefault()) {
                return rank;
            }
        }

        return RankManager.getDefaultRank();
    }

    @Override
    public GRank getCloneOfGenericRank(GRank genericRank) {
        if (genericRank != null && genericRank.isGeneric()) {
            for (GRank rank : getRanks()) {
                if (rank.isClone() && genericRank.getName().equals(rank.getName())) {
                    return rank;
                }
            }
        }

        return null;
    }

    @Override
    public void setVaultHologram(Hologram hologram) {
        vaultHologram = hologram;
    }

    @Override
    public void setName(String n) {
        name = n;
        setChanged();
    }

    @Override
    public void setTag(String t) {
        tag = t;
        setChanged();
    }

    @Override
    public void setRegions(List<GRegion> list) {
        for (GRegion region : getRegions()) {
            removeRegion(region);
        }

        for (GRegion region : list) {
            addRegion(region);
        }
    }

    @Override
    public void addRegion(GRegion region) {
        Validate.notNull(region);
        regions.add(region);
        region.setIndex(regions.size());
        region.setGuild(this);
    }

    @Override
    public void removeRegion(GRegion region) {
        Validate.notNull(region);
        regions.remove(region);
        region.setIndex(0);
    }

    @Override
    public void setLeaderName(String name) {
        leaderName = name;
    }

    @Override
    public void setVaultLocation(Location location) {
        vaultLocation = location;
        setChanged();
    }

    @Override
    public void setLeader(GPlayer nPlayer) {
        if (leader != null) {
            leader.setGuildRank(getDefaultRank());
        }

        leader = nPlayer;
        leader.setGuildRank(RankManager.getLeaderRank());
        setChanged();
    }

    @Override
    public void setHome(Location location) {
        homeLocation = location;
        setChanged();
    }

    @Override
    public void setId(int i) {
        id = i;
        setChanged();
    }

    @Override
    public void setMoney(double m) {
        money = m;
        setChanged();
    }

    @Override
    public void setAllies(Collection<Guild> list) {
        allies.clear();
        allies.addAll(list);

        setChanged();
    }

    @Override
    public void setAllyInvitations(Collection<Guild> list) {
        allyInvitations.clear();
        allyInvitations.addAll(list);

        setChanged();
    }

    @Override
    public void setWars(Collection<Guild> list) {
        war.clear();
        war.addAll(list);

        setChanged();
    }

    @Override
    public void setNoWarInvitations(Collection<Guild> list) {
        noWarInvited.clear();
        noWarInvited.addAll(list);

        setChanged();
    }

    @Override
    public void setPoints(int points) {
        this.points = points;
        setChanged();
    }

    @Override
    public void setOpenInvitation(boolean openInvitation) {
        this.openInvitation = openInvitation;
        setChanged();
    }

    @Override
    public void updateTimeRest() {
        timeRest = NumberUtils.systemSeconds();
        setChanged();
    }

    @Override
    public void updateLostLive() {
        lostLiveTime = NumberUtils.systemSeconds();
        setChanged();
    }

    @Override
    public void updateInactiveTime() {
        inactiveTime = NumberUtils.systemSeconds();
        setChanged();
    }

    @Override
    public void setLostLiveTime(long time) {
        lostLiveTime = time;
        setChanged();
    }

    @Override
    public void setInactiveTime(long time) {
        inactiveTime = time;
        setChanged();
    }

    @Override
    public void resetLostLiveTime() {
        lostLiveTime = 0;
        setChanged();
    }

    @Override
    public void setLives(int lives) {
        this.lives = lives;
        setChanged();
    }

    @Override
    public void setTimeRest(long time) {
        this.timeRest = time;
        setChanged();
    }

    @Override
    public void setFriendlyPvp(boolean pvp) {
        friendlyPvp = pvp;
        setChanged();
    }

    @Override
    public void setTimeCreated(long time) {
        this.timeCreated = time;
        setChanged();
    }

    @Override
    public void setSlots(int slots) {
        this.slots = slots;
        setChanged();
    }

    @Override
    public void setBannerMeta(BannerMeta bannerMeta) {
        this.bannerMeta = bannerMeta;
        setChanged();
    }

    @Override
    public void setRanks(List<GRank> ranks) {
        this.ranks.clear();
        this.ranks.addAll(ranks);

        setChanged();
    }

    @Override
    public boolean isInvitedToAlly(Guild guild) {
        return allyInvitations.contains(guild);
    }

    @Override
    public boolean isWarWith(Guild guild) {
        return war.contains(guild);
    }

    @Override
    public boolean isNoWarInvited(Guild guild) {
        return noWarInvited.contains(guild);
    }

    @Override
    public boolean isLeader(GPlayer nPlayer) {
        return nPlayer.equals(leader) || (leaderName != null && nPlayer.getName().equals(leaderName));
    }

    @Override
    public boolean hasRegion() {
        return !regions.isEmpty();
    }

    @Override
    public boolean ownsRegion(GRegion region) {
        return regions.contains(region);
    }

    @Override
    public boolean isAlly(Guild guild) {
        return guild != null && allies.contains(guild);
    }

    @Override
    public boolean isRaid() {
        return raid != null && raid.getResult() == Raid.Result.DURING;
    }

    @Override
    public boolean isFull() {
        return getPlayers().size() >= slots;
    }

    @Override
    public boolean isOpenInvitation() {
        return openInvitation;
    }

    @Override
    public boolean hasMoney(double money) {
        return this.money >= money;
    }

    @Override
    public void addAlly(Guild guild) {
        if (guild != null && !isAlly(guild)) {
            allies.add(guild);
            setChanged();
        }
    }

    @Override
    public void addAllyInvitation(Guild guild) {
        if (guild != null && !isInvitedToAlly(guild)) {
            allyInvitations.add(guild);
            setChanged();
        }
    }

    @Override
    public void addWar(Guild guild) {
        if (guild != null && !isWarWith(guild)) {
            war.add(guild);
            setChanged();
        }
    }

    @Override
    public void addNoWarInvitation(Guild guild) {
        if (guild != null && !isNoWarInvited(guild)) {
            noWarInvited.add(guild);
            setChanged();
        }
    }

    @Override
    public void addPlayer(GPlayer nPlayer) {
        if (nPlayer == null) {
            LoggerUtils.info("Tried to add null player to a guild! " + name);
            return;
        }

        if (!players.contains(nPlayer)) {
            players.add(nPlayer);
            nPlayer.setGuild(this);

            if (getLeaderName() != null) {
                try {
                    UUID leaderUUID = UUID.fromString(getLeaderName());

                    if (leaderUUID.equals(nPlayer.getUUID())) {
                        setLeader(nPlayer);
                        leaderName = null;
                    }
                } catch (IllegalArgumentException e) {
                    if (getLeaderName().equalsIgnoreCase(nPlayer.getName())) {
                        setLeader(nPlayer);
                        leaderName = null;
                    }
                }
            }

            if (PracticeServer.getInstance().getRankManager().isLoaded() && !nPlayer.isLeader()) {
                nPlayer.setGuildRank(getDefaultRank());
            }
        }
    }

    @Override
    public void addMoney(double money) {
        if (money == 0) {
            return;
        }

        if (money < 0) {
            throw new IllegalArgumentException("Cannot add negative amount of money");
        }

        this.money += money;
        setChanged();
    }

    @Override
    public void addPoints(int points) {
        if (points == 0) {
            return;
        }

        if (points < 0) {
            throw new IllegalArgumentException("Cannot add negative amount of points");
        }

        this.points += points;
        setChanged();
    }

    @Override
    public void addSlot() {
        slots++;
    }

    @Override
    public void addRank(GRank rank) {
        if (!ranks.contains(rank)) {
            ranks.add(rank);
            if (rank.getGuild() == null || !rank.getGuild().equals(this)) {
                rank.setGuild(this);
            }
        }
    }

    @Override
    public void removePlayer(GPlayer nPlayer) {
        if (players.contains(nPlayer)) {
            players.remove(nPlayer);
            nPlayer.setGuild(null);
            nPlayer.setGuildRank(null);
        }
    }

    @Override
    public void removeAlly(Guild guild) {
        if (allies.contains(guild)) {
            allies.remove(guild);
            setChanged();
        }
    }

    @Override
    public void removeWar(Guild guild) {
        if (war.contains(guild)) {
            war.remove(guild);
            setChanged();
        }
    }

    @Override
    public void removeNoWarInvitation(Guild guild) {
        if (noWarInvited.contains(guild)) {
            noWarInvited.remove(guild);
            setChanged();
        }
    }

    @Override
    public void removeRank(GRank rank) {
        ranks.remove(rank);
        rank.setGuild(null);
    }

    @Override
    public void takeMoney(double money) {
        if (money == 0) {
            return;
        }

        if (money < 0) {
            throw new IllegalArgumentException("Cannot take negative amount of money");
        }

        this.money -= money;
        setChanged();
    }

    @Override
    public void takeLive() {
        lives--;
        setChanged();
    }

    @Override
    public void addLive() {
        lives++;
        setChanged();
    }

    @Override
    public void takePoints(int points) {
        if (points == 0) {
            return;
        }

        if (points < 0) {
            throw new IllegalArgumentException("Cannot add negative amount of points");
        }

        this.points -= points;
        setChanged();
    }

    @Override
    public void removeAllyInvitation(Guild guild) {
        if (isInvitedToAlly(guild)) {
            allyInvitations.remove(guild);
            setChanged();
        }
    }

    @Override
    public void createRaid(Guild attacker) {
        raid = new RaidImpl(attacker, this);
    }

    @Override
    public boolean isMember(GPlayer nPlayer) {
        return players.contains(nPlayer);
    }

    @Override
    public void destroy() {
        destroy(AbandonCause.PLAYER);
    }

    @Override
    public void destroy(AbandonCause cause) {
        final PracticeServer plugin = PracticeServer.getInstance();


        for (GPlayer nPlayer : getPlayers()) {
            nPlayer.cancelToolProgress();
            nPlayer.setGuild(null);
            nPlayer.setGuildRank(null);

            if (nPlayer.isOnline()) {

                if (nPlayer.getGuiInventory() != null) {
                    nPlayer.getGuiInventoryHistory().clear();
                    nPlayer.getPlayer().closeInventory();
                }
            }
        }

        for (GPlayer nPlayer : plugin.getPlayerManager().getPlayers()) {

            if (nPlayer.isInvitedTo(this)) {
                nPlayer.deleteInvitation(this);
            }


            if (nPlayer.isPartRaid()) {
                nPlayer.getPartRaid().removePlayerOccupying(nPlayer);
            }

            if (nPlayer.isOnline() && nPlayer.isAtRegion()) {
                GRegion atRegion = nPlayer.getAtRegion();


                if (atRegion.getGuild().equals(this)) {
                    plugin.getRegionManager().playerExitedRegion(nPlayer.getPlayer());
                }
            }
        }


        for (Guild nGuild : plugin.getGuildManager().getGuilds()) {

            if (nGuild.isAlly(this)) {
                nGuild.removeAlly(this);
            }


            if (nGuild.isInvitedToAlly(this)) {
                nGuild.removeAllyInvitation(this);
            }


            if (nGuild.isWarWith(this)) {
                nGuild.removeWar(this);
            }


            if (nGuild.isNoWarInvited(this)) {
                nGuild.removeNoWarInvitation(this);
            }
        }

        if (isRaid()) {
            getRaid().setResult(Raid.Result.DESTROYED);
        }


        if (this.getVaultHologram() != null) {
            this.getVaultHologram().delete();
        }

        GuildManager.checkVaultDestroyed(this);
        if (cause != AbandonCause.UNLOADED && getVaultLocation() != null) {
            getVaultLocation().getBlock().breakNaturally();
            getVaultLocation().getWorld().playEffect(getVaultLocation(), Effect.SMOKE, 1000);
        }

        if (getLeader() != null) {
            if (getLeader().isOnline() && getLeader().getPlayer().getGameMode() != GameMode.CREATIVE) {
                while (InventoryUtils.containsAtLeast(getLeader().getPlayer().getInventory(), Config.VAULT_ITEM.getItemStack(), 1)) {
                    getLeader().getPlayer().getInventory().removeItem(Config.VAULT_ITEM.getItemStack());
                }
            }


            getLeader().addMoney(getMoney());
        }


        plugin.getRankManager().delete(this);


        for (GRegion region : new ArrayList<>(getRegions())) {
            RegionDeleteEvent event = new RegionDeleteEvent(region, RegionDeleteEvent.Cause.fromGuildAbandonCause(cause));
            plugin.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                plugin.getRegionManager().remove(region);
            }
        }

        TabUtils.refresh(this);
        TagUtils.refresh(this);
    }

    @Override
    public void showVaultHologram(Player player) {
        if (vaultHologram != null) {
            vaultHologram.getVisibilityManager().showTo(player);
        }
    }

    @Override
    public void hideVaultHologram(Player player) {
        if (vaultHologram != null) {
            vaultHologram.getVisibilityManager().hideTo(player);
        }
    }

    @Override
    public void removeRaidBar() {
        if (Config.BOSSBAR_ENABLED.getBoolean()) {
            for (Player player : getOnlinePlayers()) {
                BossBarUtils.removeBar(player);
            }
        }
    }

    @Override
    public void postSetUp() {
        setAllies(loadingWrapper.convert(loadingWrapper.getAllies()));
        setAllyInvitations(loadingWrapper.convert(loadingWrapper.getAllyInvitations()));
        setNoWarInvitations(loadingWrapper.convert(loadingWrapper.getNoWarInvitations()));
        setWars(loadingWrapper.convert(loadingWrapper.getWars()));

        setUnchanged();

        if (loadingWrapper.getAllies().size() != getAllies().size()
                || loadingWrapper.getAllyInvitations().size() != getAllyInvitations().size()
                || loadingWrapper.getNoWarInvitations().size() != getNoWarInvitations().size()
                || loadingWrapper.getWars().size() != getWars().size()) {
            setChanged();
        }
    }

    @Override
    public LoadingWrapper getLoadingWrapper() {
        return loadingWrapper;
    }

    public static class LoadingWrapperImpl<T> implements LoadingWrapper<T> {
        protected final List<T> allies = new ArrayList<>();
        protected final List<T> allyInvitations = new ArrayList<>();
        protected final List<T> wars = new ArrayList<>();
        protected final List<T> noWarInvitations = new ArrayList<>();
        protected final IConverter<T, Guild> converter;

        public LoadingWrapperImpl(IConverter<T, Guild> converter) {
            this.converter = converter;
        }

        @Override
        public Collection<T> getAllies() {
            return allies;
        }

        @Override
        public Collection<T> getAllyInvitations() {
            return allyInvitations;
        }

        @Override
        public Collection<T> getWars() {
            return wars;
        }

        @Override
        public Collection<T> getNoWarInvitations() {
            return noWarInvitations;
        }

        @Override
        public Collection<Guild> convert(Collection<T> list) {
            return converter.convert(list);
        }

        @Override
        public void setAllies(Collection<T> list) {
            allies.clear();
            allies.addAll(list);
        }

        @Override
        public void setAllyInvitations(Collection<T> list) {
            allyInvitations.clear();
            allyInvitations.addAll(list);
        }

        @Override
        public void setWars(Collection<T> list) {
            wars.clear();
            wars.addAll(list);
        }

        @Override
        public void setNoWarInvitations(Collection<T> list) {
            noWarInvitations.clear();
            noWarInvitations.addAll(list);
        }
    }

    public static class LoadingWrapper37MigrationImpl extends LoadingWrapperImpl<String> {
        public LoadingWrapper37MigrationImpl() {
            super(new NameToGuildConverterImpl());
        }
    }
}
