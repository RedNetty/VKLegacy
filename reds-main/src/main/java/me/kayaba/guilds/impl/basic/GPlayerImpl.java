package me.kayaba.guilds.impl.basic;

import me.bpweber.practiceserver.money.Economy.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.runnable.*;
import me.kayaba.guilds.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;

public class GPlayerImpl extends AbstractResource implements GPlayer {
    private int id = 0;
    private String name;
    private Guild guild;
    private int points = 0;
    private int kills = 0;
    private int deaths = 0;

    private final Collection<Vehicle> vehicles = new HashSet<>();
    private final List<Guild> invitedTo = new ArrayList<>();
    private final List<GUIInventory> guiInventoryHistory = new ArrayList<>();
    private final Map<UUID, Long> killingHistory = new HashMap<>();
    private final Preferences preferences = new PreferencesImpl();

    private Raid partRaid;
    private GRank guildRank;
    private GRegion atRegion;
    private TabList tabList;
    private CommandExecutorHandler commandExecutorHandler;
    private RegionSelection activeSelection;


    public GPlayerImpl(UUID uuid) {
        super(uuid);
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(getUUID());
    }

    @Override
    public Guild getGuild() {
        return guild;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Guild> getInvitedTo() {
        return invitedTo;
    }

    @Override
    public RegionSelection getActiveSelection() {
        return activeSelection;
    }

    @Override
    public GRegion getAtRegion() {
        return atRegion;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public int getDeaths() {
        return deaths;
    }

    @Override
    public int getKills() {
        return kills;
    }

    @Override
    public double getKillDeathRate() {
        return NumberUtils.roundOffTo2DecPlaces((double) getKills() / (getDeaths() == 0 ? 1 : (double) getDeaths()));
    }

    @Override
    public double getMoney() {
        OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(getUUID());

        if (!oPlayer.hasPlayedBefore() || oPlayer.getName() == null) {
            return 0D;
        }
        return Economy.getBalance(getUUID());
    }

    @Override
    public TabList getTabList() {
        return tabList;
    }

    @Override
    public CommandExecutorHandler getCommandExecutorHandler() {
        return commandExecutorHandler;
    }

    @Override
    public Raid getPartRaid() {
        return partRaid;
    }

    @Override
    public GUIInventory getGuiInventory() {
        return guiInventoryHistory.isEmpty() ? null : guiInventoryHistory.get(guiInventoryHistory.size() - 1);
    }

    @Override
    public List<GUIInventory> getGuiInventoryHistory() {
        return guiInventoryHistory;
    }

    @Override
    public GRank getGuildRank() {
        return guildRank;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setGuild(Guild guild) {
        this.guild = guild;
        setChanged();
    }

    @Override
    public Preferences getPreferences() {
        return preferences;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        setChanged();
    }

    @Override
    public void setInvitedTo(Collection<Guild> list) {
        invitedTo.clear();
        invitedTo.addAll(list);
        setChanged();
    }

    @Override
    public void setActiveSelection(RegionSelection selection) {
        activeSelection = selection;
    }

    @Override
    public void setAtRegion(GRegion region) {
        atRegion = region;
    }

    @Override
    public void setPoints(int points) {
        this.points = points;
        setChanged();
    }

    @Override
    public void setDeaths(int deaths) {
        this.deaths = deaths;
        setChanged();
    }

    @Override
    public void setKills(int kills) {
        this.kills = kills;
        setChanged();
    }

    @Override
    public void setTabList(TabList tabList) {
        this.tabList = tabList;
    }

    @Override
    public void setPartRaid(Raid partRaid) {
        this.partRaid = partRaid;
    }

    @Override
    public void setGuiInventory(GUIInventory guiInventory) {
        if (guiInventory == null) {
            removeLastGUIInventoryHistory();
            return;
        }

        if (!guiInventory.equals(getGuiInventory())) {
            guiInventoryHistory.add(guiInventory);
        }
    }

    @Override
    public void setGuildRank(GRank guildRank) {
        if (this.guildRank != null) {
            this.guildRank.removeMember(this);
        }

        if (guildRank != null) {

            guildRank.addMember(this);
        }

        this.guildRank = guildRank;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean hasGuild() {
        return getGuild() != null;
    }

    @Override
    public boolean isOnline() {
        return getPlayer() != null;
    }

    @Override
    public boolean isInvitedTo(Guild guild) {
        return invitedTo.contains(guild);
    }

    @Override
    public boolean isPartRaid() {
        return partRaid != null;
    }

    @Override
    public boolean isVehicleListed(Vehicle vehicle) {
        return vehicles.contains(vehicle);
    }

    @Override
    public boolean isLeader() {
        return hasGuild() && getGuild().isLeader(this);
    }

    @Override
    public boolean isAtRegion() {
        return atRegion != null;
    }

    @Override
    public boolean hasMoney(double money) {
        return getMoney() >= money;
    }

    @Override
    public boolean hasPermission(GuildPermission permission) {
        return guildRank != null && guildRank.hasPermission(permission);
    }

    @Override
    public boolean hasTabList() {
        return tabList != null;
    }

    @Override
    public boolean canGetKillPoints(Player player) {
        return !killingHistory.containsKey(player.getUniqueId()) || NumberUtils.systemSeconds() - killingHistory.get(player.getUniqueId()) > Config.KILLING_COOLDOWN.getSeconds();
    }

    @Override
    public void addInvitation(Guild guild) {
        if (!isInvitedTo(guild)) {
            invitedTo.add(guild);
            setChanged();
        }
    }

    @Override
    public void addPoints(int points) {
        this.points += points;
        setChanged();
    }

    @Override
    public void addKill() {
        kills++;
        setChanged();
    }

    @Override
    public void addDeath() {
        deaths++;
        setChanged();
    }

    @Override
    public void addMoney(double money) {
        OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(getUUID());

        if (!oPlayer.hasPlayedBefore() || oPlayer.getName() == null) {
            return;
        }

        Economy.depositPlayer(getUUID(), ((int) money));
    }

    @Override
    public void addKillHistory(Player player) {
        if (killingHistory.containsKey(player.getUniqueId())) {
            killingHistory.remove(player.getUniqueId());
        }

        killingHistory.put(player.getUniqueId(), NumberUtils.systemSeconds());
    }

    @Override
    public void addVehicle(Vehicle vehicle) {
        if (!isVehicleListed(vehicle)) {
            vehicles.add(vehicle);
        }
    }

    @Override
    public void newCommandExecutorHandler(CommandWrapper command, String[] args) {
        commandExecutorHandler = new CommandExecutorHandlerImpl(command, getPlayer(), args);
        Message.CHAT_CONFIRM_NEEDCONFIRM.send(this);
    }

    @Override
    public void deleteInvitation(Guild guild) {
        invitedTo.remove(guild);
        setChanged();
    }

    @Override
    public void takePoints(int points) {
        this.points -= points;
        setChanged();
    }

    @Override
    public void takeMoney(double money) {
        OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(getUUID());

        if (!oPlayer.hasPlayedBefore() || oPlayer.getName() == null) {
            return;
        }

        Economy.withdrawPlayer(getUUID(), ((int) money));
    }

    @Override
    public void cancelToolProgress() {
        if (isOnline()) {
            if (getActiveSelection() != null) {
                getActiveSelection().reset();
            }

            if (getPreferences().getRegionMode() == RegionMode.RESIZE) {
                getPreferences().setRegionMode(RegionMode.CHECK);
            }
        }
    }

    @Override
    public void removeCommandExecutorHandler() {
        commandExecutorHandler = null;
    }

    @Override
    public void removeLastGUIInventoryHistory() {
        getGuiInventoryHistory().remove(getGuiInventoryHistory().size() - 1);
    }

    public class PreferencesImpl implements Preferences {
        private boolean bypass;
        private boolean compassPointingGuild;
        private boolean spyMode;
        private boolean regionSpectate;
        private ChatMode chatMode = ChatMode.NORMAL;
        private RegionMode regionMode = RegionMode.CHECK;

        @Override
        public boolean getBypass() {
            return bypass;
        }

        @Override
        public ChatMode getChatMode() {
            return chatMode;
        }

        @Override
        public boolean isCompassPointingGuild() {
            return compassPointingGuild;
        }

        @Override
        public RegionMode getRegionMode() {
            return regionMode;
        }

        @Override
        public boolean getRegionSpectate() {
            return regionSpectate;
        }

        @Override
        public boolean getSpyMode() {
            return spyMode;
        }

        @Override
        public void setBypass(boolean bypass) {
            this.bypass = bypass;
        }

        @Override
        public void setChatMode(ChatMode chatMode) {
            this.chatMode = chatMode;
        }

        @Override
        public void setCompassPointingGuild(boolean compassPointingGuild) {
            this.compassPointingGuild = compassPointingGuild;
        }

        @Override
        public void setSpyMode(boolean spyMode) {
            this.spyMode = spyMode;
        }

        @Override
        public void setRegionMode(RegionMode regionMode) {
            this.regionMode = regionMode;
        }

        @Override
        public void setRegionSpectate(boolean regionSpectate) {
            this.regionSpectate = regionSpectate;
        }

        @Override
        public void toggleBypass() {
            bypass = !bypass;
        }

        @Override
        public void toggleRegionSpectate() {
            regionSpectate = !regionSpectate;
        }

        @Override
        public void toggleSpyMode() {
            spyMode = !spyMode;
        }

        @Override
        public void toggleCompassPointingGuild() {
            compassPointingGuild = !compassPointingGuild;
        }
    }
}
