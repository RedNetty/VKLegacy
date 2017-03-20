package me.kayaba.guilds.manager;

import com.google.common.collect.*;
import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.util.*;
import org.bukkit.configuration.*;

import java.util.*;
import java.util.concurrent.*;

public class RankManager {
    private static final PracticeServer plugin = PracticeServer.getInstance();
    private final List<GRank> genericRanks = new ArrayList<>();
    private boolean loaded = false;


    public boolean isLoaded() {
        return loaded;
    }


    public void load() {
        int count = getResourceManager().load().size();

        LoggerUtils.info("Loaded " + count + " ranks.");


        assignRanks();

        loaded = true;
    }


    public void save() {
        long nanoTime = System.nanoTime();
        int count = getResourceManager().executeSave() + getResourceManager().save(get());
        LoggerUtils.info("Ranks data saved in " + TimeUnit.MILLISECONDS.convert((System.nanoTime() - nanoTime), TimeUnit.NANOSECONDS) / 1000.0 + "s (" + count + " ranks)");

        nanoTime = System.nanoTime();
        count = getResourceManager().executeRemoval();
        LoggerUtils.info("Ranks removed in " + TimeUnit.MILLISECONDS.convert((System.nanoTime() - nanoTime), TimeUnit.NANOSECONDS) / 1000.0 + "s (" + count + " ranks)");
    }


    public void delete(GRank rank) {
        if (rank.isGeneric()) {
            return;
        }

        getResourceManager().addToRemovalQueue(rank);

        rank.getGuild().removeRank(rank);

        for (GPlayer nPlayer : new ArrayList<>(rank.getMembers())) {
            rank.removeMember(nPlayer);
        }
    }


    public void delete(Guild guild) {
        for (GRank rank : guild.getRanks()) {
            getResourceManager().addToRemovalQueue(rank);
        }

        guild.setRanks(new ArrayList<GRank>());
    }


    public void loadDefaultRanks() {
        genericRanks.clear();
        GRank leaderRank = new GenericRankImpl(Message.INVENTORY_GUI_RANKS_LEADERNAME.get());
        leaderRank.setPermissions(Lists.newArrayList(GuildPermission.values()));
        genericRanks.add(leaderRank);
        int count = 1;

        final Map<String, List<String>> parentMap = new HashMap<>();

        ConfigurationSection section = plugin.getConfigManager().getConfig().getConfigurationSection("rank");
        for (String rankName : section.getKeys(false)) {
            if (rankName.equalsIgnoreCase("gui")) continue;
            if (rankName.equalsIgnoreCase("maxamount")) continue;
            System.out.println(rankName);
            ConfigurationSection rankSection = section.getConfigurationSection(rankName);
            GRank rank = new GenericRankImpl(rankName);
            final List<String> parents = new ArrayList<>();

            for (String permName : rankSection.getStringList("permissions")) {
                rank.addPermission(GuildPermission.valueOf(permName.toUpperCase()));
            }

            if (rankSection.contains("inherit")) {
                for (String parentName : (rankSection.isList("inherit") ? rankSection.getStringList("inherit") : Collections.singletonList(rankSection.getString("inherit")))) {
                    parents.add(parentName);
                }
            }

            parentMap.put(rankName, parents);
            genericRanks.add(rank);
            count++;
        }

        for (Map.Entry<String, List<String>> parentMapEntry : parentMap.entrySet()) {
            GRank rank = getGeneric(parentMapEntry.getKey());

            if (rank == null) {
                LoggerUtils.error("Invalid rank: '" + parentMapEntry.getKey() + "'");
                continue;
            }

            for (String parentName : parentMapEntry.getValue()) {
                GRank parent = getGeneric(parentName);

                if (parent == null) {
                    LoggerUtils.error("Invalid rank: '" + parentName + "'");
                    continue;
                }

                rank.addPermissions(parent.getPermissions());
            }
        }


        if (count == 1) {
            GRank defaultRank = new GenericRankImpl("Member");
            genericRanks.add(defaultRank);
            count++;
        }

        LoggerUtils.info("Loaded " + count + " default (guild) ranks.");
    }


    public List<GRank> getGenericRanks() {
        return genericRanks;
    }


    public Collection<GRank> get() {
        Collection<GRank> collection = new HashSet<>();

        for (Guild guild : plugin.getGuildManager().getGuilds()) {
            for (GRank rank : guild.getRanks()) {
                if (!rank.isGeneric()) {
                    collection.add(rank);
                }
            }
        }

        return collection;
    }


    public GRank getGeneric(String name) {
        for (GRank rankLoop : getGenericRanks()) {
            if (rankLoop.getName().equalsIgnoreCase(name)) {
                return rankLoop;
            }
        }

        return null;
    }


    private void assignRanks() {
        for (Guild guild : plugin.getGuildManager().getGuilds()) {
            assignRanks(guild);
        }
    }


    public void assignRanks(Guild guild) {
        for (GPlayer nPlayer : guild.getPlayers()) {
            if (nPlayer.getGuildRank() == null) {
                GRank defaultRank = guild.getDefaultRank();
                GRank rank;

                if (nPlayer.isLeader()) {
                    rank = getLeaderRank();
                } else {
                    if (defaultRank == null) {
                        rank = getGenericRanks().get(1);
                    } else {
                        rank = defaultRank;
                    }
                }

                nPlayer.setGuildRank(rank);
            }
        }
    }


    public static GRank getLeaderRank() {
        return PracticeServer.getInstance().getRankManager().getGenericRanks().get(0);
    }


    public static GRank getDefaultRank() {
        return plugin.getRankManager().getGenericRanks().get(1);
    }


    private ResourceManager<GRank> getResourceManager() {
        return plugin.getStorage().getResourceManager(GRank.class);
    }
}
