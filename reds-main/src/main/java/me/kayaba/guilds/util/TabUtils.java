package me.kayaba.guilds.util;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;
import java.util.concurrent.*;

public final class TabUtils {
    private static final PracticeServer plugin = PracticeServer.getInstance();

    private TabUtils() {

    }


    public static void refresh(final GPlayer nPlayer) {
        if (!Config.TABLIST_ENABLED.getBoolean()) {
            return;
        }

        if (!nPlayer.hasTabList()) {
            return;
        }

        if (Bukkit.isPrimaryThread()) {
            new Thread() {
                @Override
                public void run() {
                    nPlayer.getTabList().send();
                }
            }.start();
        } else {
            nPlayer.getTabList().send();
        }
    }


    public static void refresh(Player player) {
        refresh(PlayerManager.getPlayer(player));
    }


    public static void refresh() {
        refresh(new ArrayList<>(plugin.getPlayerManager().getOnlinePlayers()));
    }


    public static void refresh(Guild guild) {
        refresh(guild.getPlayers());
    }


    public static void refresh(final List<GPlayer> list) {
        new Thread() {
            @Override
            public void run() {
                for (GPlayer nPlayer : list) {
                    refresh(nPlayer);
                }
            }
        }.start();
    }


    @SuppressWarnings("deprecation")
    public static void fillVars(TabList tabList) {
        GPlayer nPlayer = tabList.getPlayer();
        Map<VarKey, String> vars = tabList.getVars();
        tabList.clear();


        int onlinePlayersCount = 0;
        for (Player player : CompatibilityUtils.getOnlinePlayers()) {
            if (!plugin.getPlayerManager().isVanished(player)) {
                onlinePlayersCount++;
            }
        }


        vars.put(VarKey.SERVER_ONLINE, String.valueOf(onlinePlayersCount));
        vars.put(VarKey.SERVER_ONLINE_ALL, String.valueOf(CompatibilityUtils.getOnlinePlayers().size()));
        vars.put(VarKey.SERVER_MAX, String.valueOf(Bukkit.getMaxPlayers()));


        Date date = Calendar.getInstance().getTime();
        vars.put(VarKey.DATE_YEAR, String.valueOf(1900 + date.getYear()));
        vars.put(VarKey.DATE_MONTH, String.valueOf((date.getMonth() < 10 ? "0" : "") + date.getMonth()));
        vars.put(VarKey.DATE_DAY, String.valueOf((date.getDay() < 10 ? "0" : "") + date.getDay()));
        vars.put(VarKey.DATE_HOURS, String.valueOf((date.getHours() < 10 ? "0" : "") + date.getHours()));
        vars.put(VarKey.DATE_MINUTES, String.valueOf((date.getMinutes() < 10 ? "0" : "") + date.getMinutes()));
        vars.put(VarKey.DATE_SECONDS, String.valueOf((date.getSeconds() < 10 ? "0" : "") + date.getSeconds()));


        if (nPlayer.isOnline()) {
            World world = Bukkit.getWorlds().get(0);
            vars.put(VarKey.WORLD_NAME, world.getName());
            vars.put(VarKey.WORLD_SPAWN, Message.getCoords3D(world.getSpawnLocation()).get());
        }


        vars.put(VarKey.PLAYER_NAME, nPlayer.getName());
        vars.put(VarKey.PLAYER_BALANCE, String.valueOf(NumberUtils.roundOffTo2DecPlaces(nPlayer.getMoney())));
        vars.put(VarKey.PLAYER_KILLS, String.valueOf(nPlayer.getKills()));
        vars.put(VarKey.PLAYER_DEATHS, String.valueOf(nPlayer.getDeaths()));
        vars.put(VarKey.PLAYER_KDR, String.valueOf(nPlayer.getKillDeathRate()));
        vars.put(VarKey.PLAYER_CHATMODE, nPlayer.getPreferences().getChatMode().getName().get());
        vars.put(VarKey.PLAYER_SPYMODE, Message.getOnOff(nPlayer.getPreferences().getSpyMode()));
        vars.put(VarKey.PLAYER_BYPASS, Message.getOnOff(nPlayer.getPreferences().getBypass()));
        vars.put(VarKey.PLAYER_POINTS, String.valueOf(nPlayer.getPoints()));


        Guild guild = nPlayer.getGuild();
        String guildName, guildTag, guildPlayersOnline, guildPlayersMax, guildLives, guildTimeRegen, guildRaidProgress, guildPvp, guildMoney, guildPoints, guildSlots = "";
        String guildTimeRest, guildTimeCreated, guildHomeCoordinates, guildOpenInvitation, guildTimeProtection = "";
        guildName = guildTag = guildPlayersOnline = guildPlayersMax = guildLives = guildTimeRegen = guildRaidProgress = guildPvp = guildMoney = guildPoints = guildSlots;
        guildTimeRest = guildTimeCreated = guildHomeCoordinates = guildOpenInvitation = guildTimeProtection;

        if (nPlayer.hasGuild()) {
            long liveRegenerationTime = Config.LIVEREGENERATION_REGENTIME.getSeconds() - (NumberUtils.systemSeconds() - guild.getLostLiveTime());
            long createdTime = NumberUtils.systemSeconds() - guild.getTimeCreated();
            long restTime = Config.RAID_TIMEREST.getSeconds() - (NumberUtils.systemSeconds() - guild.getTimeRest());
            long timeProtection = Config.GUILD_CREATEPROTECTION.getSeconds() - createdTime;

            guildName = guild.getName();
            guildTag = guild.getTag();
            guildPlayersOnline = String.valueOf(guild.getOnlinePlayers().size());
            guildPlayersMax = String.valueOf(guild.getPlayers().size());
            guildLives = String.valueOf(guild.getLives());
            guildRaidProgress = guild.isRaid() ? String.valueOf(guild.getRaid().getProgress()) : "";
            guildPvp = Message.getOnOff(guild.getFriendlyPvp());
            guildMoney = String.valueOf(guild.getMoney());
            guildPoints = String.valueOf(guild.getPoints());
            guildSlots = String.valueOf(guild.getSlots());
            guildTimeRegen = StringUtils.secondsToString(liveRegenerationTime, TimeUnit.HOURS);
            guildTimeRest = StringUtils.secondsToString(restTime, TimeUnit.HOURS);
            guildTimeCreated = StringUtils.secondsToString(createdTime, TimeUnit.HOURS);
            guildTimeProtection = StringUtils.secondsToString(timeProtection, TimeUnit.HOURS);
            guildHomeCoordinates = Message.getCoords3D(guild.getHome()).get();
            guildOpenInvitation = Message.getOnOff(guild.isOpenInvitation());
        }

        vars.put(VarKey.GUILD_NAME, guildName);
        vars.put(VarKey.GUILD_TAG, guildTag);
        vars.put(VarKey.GUILD_PLAYERS_ONLINE, guildPlayersOnline);
        vars.put(VarKey.GUILD_PLAYERS_MAX, guildPlayersMax);
        vars.put(VarKey.GUILD_LIVES, guildLives);
        vars.put(VarKey.GUILD_RAIDPROGRESS, guildRaidProgress);
        vars.put(VarKey.GUILD_PVP, guildPvp);
        vars.put(VarKey.GUILD_MONEY, guildMoney);
        vars.put(VarKey.GUILD_POINTS, guildPoints);
        vars.put(VarKey.GUILD_SLOTS, guildSlots);
        vars.put(VarKey.GUILD_TIME_REGEN, guildTimeRegen);
        vars.put(VarKey.GUILD_TIME_REST, guildTimeRest);
        vars.put(VarKey.GUILD_TIME_CREATED, guildTimeCreated);
        vars.put(VarKey.GUILD_TIME_PROTECTION, guildTimeProtection);
        vars.put(VarKey.GUILD_HOME, guildHomeCoordinates);
        vars.put(VarKey.GUILD_OPENINVITATION, guildOpenInvitation);


        List<Guild> topGuildsList = plugin.getGuildManager().getTopGuildsByPoints(20);
        for (int i = 1; i <= 20; i++) {
            if (i <= topGuildsList.size()) {
                Guild guildTop = topGuildsList.get(i - 1);
                String row = Config.TABLIST_TOPROW_GUILDS.getString();
                Map<VarKey, String> rowVars = new HashMap<>();
                rowVars.put(VarKey.N, String.valueOf(i));
                rowVars.put(VarKey.GUILD_NAME, guildTop.getName());
                rowVars.put(VarKey.GUILD_TAG, guildTop.getTag());
                rowVars.put(VarKey.GUILD_POINTS, String.valueOf(guildTop.getPoints()));
                row = StringUtils.replaceVarKeyMap(row, rowVars);

                vars.put(VarKey.valueOf("GUILD_TOP_N" + i), row);
            } else {
                vars.put(VarKey.valueOf("GUILD_TOP_N" + i), "");
            }
        }


        final List<ListDisplay> listDisplays = new ArrayList<>();
        listDisplays.add(new ListDisplay(Config.TABLIST_TOPROW_PLAYERS_POINTS, VarKey.PLAYER_POINTS, plugin.getPlayerManager().getTopPlayersByPoints(20)));
        listDisplays.add(new ListDisplay(Config.TABLIST_TOPROW_PLAYERS_KDR, VarKey.PLAYER_KDR, plugin.getPlayerManager().getTopPlayersByKDR(20)));

        for (ListDisplay listDisplay : listDisplays) {
            List<GPlayer> topPlayersList = listDisplay.getList();

            for (int i = 1; i <= 20; i++) {
                if (i <= topPlayersList.size()) {
                    GPlayer nPlayerTop = topPlayersList.get(i - 1);
                    String row = listDisplay.getRowPattern().getString();
                    Map<VarKey, String> rowVars = new HashMap<>();
                    rowVars.put(VarKey.N, String.valueOf(i));
                    rowVars.put(VarKey.PLAYER_NAME, nPlayerTop.getName());
                    rowVars.put(VarKey.PLAYER_KILLS, String.valueOf(nPlayerTop.getKills()));
                    rowVars.put(VarKey.PLAYER_DEATHS, String.valueOf(nPlayerTop.getDeaths()));
                    rowVars.put(VarKey.PLAYER_KDR, String.valueOf(nPlayerTop.getKillDeathRate()));
                    rowVars.put(VarKey.PLAYER_POINTS, String.valueOf(nPlayerTop.getPoints()));
                    row = StringUtils.replaceVarKeyMap(row, rowVars);

                    vars.put(VarKey.valueOf("PLAYER_TOP_" + listDisplay.getVarKey() + "_N" + i), row);
                } else {
                    vars.put(VarKey.valueOf("PLAYER_TOP_" + listDisplay.getVarKey() + "_N" + i), "");
                }
            }
        }
    }

    private static class ListDisplay {
        private final ConfigWrapper rowPattern;
        private final VarKey varKey;
        private final List<GPlayer> list;


        ListDisplay(ConfigWrapper rowPattern, VarKey varKey, List<GPlayer> list) {
            this.rowPattern = rowPattern;
            this.varKey = varKey;
            this.list = list;
        }


        public ConfigWrapper getRowPattern() {
            return rowPattern;
        }


        public VarKey getVarKey() {
            return varKey;
        }


        public List<GPlayer> getList() {
            return list;
        }
    }
}
