package me.kayaba.guilds.manager;

import com.earth2me.essentials.*;
import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.util.*;
import org.apache.commons.lang.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.kitteh.vanish.*;

import java.util.*;
import java.util.concurrent.*;

public class PlayerManager {
    private static final PracticeServer plugin = PracticeServer.getInstance();
    private final Map<String, GPlayer> players = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);


    public static GPlayer getPlayer(String playerName) {
        plugin.getPlayerManager().addIfNotExists(Bukkit.getPlayerExact(playerName));

        return plugin.getPlayerManager().players.get(playerName);
    }


    public static GPlayer getPlayer(CommandSender sender) {
        if (sender instanceof Player) {
            plugin.getPlayerManager().addIfNotExists((Player) sender);
        }

        return getPlayer(sender.getName());
    }


    public static GPlayer getPlayer(UUID uuid) {
        for (GPlayer nPlayer : plugin.getPlayerManager().getPlayers()) {
            if (nPlayer.getUUID().equals(uuid)) {
                return nPlayer;
            }
        }

        return null;
    }


    public Collection<GPlayer> getPlayers() {
        return players.values();
    }


    public Collection<GPlayer> getOnlinePlayers() {
        Collection<GPlayer> collection = new HashSet<>();

        for (Player player : CompatibilityUtils.getOnlinePlayers()) {
            collection.add(getPlayer(player));
        }

        return collection;
    }


    public void save() {
        long startTime = System.nanoTime();
        int count = getResourceManager().executeSave() + getResourceManager().save(getPlayers());
        LoggerUtils.info("Players data saved in " + TimeUnit.MILLISECONDS.convert((System.nanoTime() - startTime), TimeUnit.NANOSECONDS) / 1000.0 + "s (" + count + " players)");

        startTime = System.nanoTime();
        count = getResourceManager().executeRemoval();
        LoggerUtils.info("Players removed in " + TimeUnit.MILLISECONDS.convert((System.nanoTime() - startTime), TimeUnit.NANOSECONDS) / 1000.0 + "s (" + count + " players)");
    }


    public void load() {

        for (GPlayer nPlayer : getPlayers()) {
            if (nPlayer.isOnline()) {

                if (nPlayer.getGuiInventory() != null) {
                    nPlayer.getGuiInventoryHistory().clear();
                    nPlayer.getPlayer().closeInventory();
                }
            }
        }

        players.clear();

        for (GPlayer nPlayer : getResourceManager().load()) {
            players.put(nPlayer.getName(), nPlayer);
        }

        LoggerUtils.info("Loaded " + players.size() + " players.");
    }


    private void add(Player player) {
        Validate.notNull(player);

        GPlayer nPlayer = new GPlayerImpl(player.getUniqueId());
        nPlayer.setName(player.getName());
        nPlayer.setPoints(Config.KILLING_STARTPOINTS.getInt());

        players.put(nPlayer.getName(), nPlayer);
    }


    public void addIfNotExists(Player player) {
        if (player != null && !players.containsKey(player.getName())) {
            add(player);
        }
    }


    public boolean isGuildMate(Player player1, Player player2) {
        GPlayer nPlayer1 = getPlayer(player1);
        GPlayer nPlayer2 = getPlayer(player2);

        return nPlayer1.getGuild().isMember(nPlayer2) || nPlayer1.equals(nPlayer2);
    }


    public boolean isAlly(Player player1, Player player2) {
        GPlayer nPlayer1 = getPlayer(player1);
        GPlayer nPlayer2 = getPlayer(player2);

        return nPlayer1.getGuild().isAlly(nPlayer2.getGuild()) || nPlayer1.equals(nPlayer2);
    }


    public void sendPlayerInfo(CommandSender sender, GPlayer nCPlayer) {
        Map<VarKey, String> vars = new HashMap<>();
        vars.put(VarKey.PLAYER_NAME, nCPlayer.getName());
        vars.put(VarKey.PLAYER_POINTS, String.valueOf(nCPlayer.getPoints()));
        vars.put(VarKey.PLAYER_KILLS, String.valueOf(nCPlayer.getKills()));
        vars.put(VarKey.PLAYER_DEATHS, String.valueOf(nCPlayer.getDeaths()));
        vars.put(VarKey.PLAYER_KDR, String.valueOf(nCPlayer.getKillDeathRate()));

        String guildRow = "";
        if (nCPlayer.hasGuild()) {
            vars.put(VarKey.GUILD_NAME, nCPlayer.getGuild().getName());
            vars.put(VarKey.TAG, nCPlayer.getGuild().getTag());
            guildRow = Message.CHAT_PLAYER_INFO_GUILDROW.clone().vars(vars).get();
        }

        vars.put(VarKey.GUILDROW, guildRow);

        Message.CHAT_PLAYER_INFO_HEADER.send(sender);

        for (String row : Message.CHAT_PLAYER_INFO_ITEMS.getList()) {
            if (!row.contains("{GUILDROW}") || nCPlayer.hasGuild()) {
                row = MessageManager.replaceVarKeyMap(row, vars);
                MessageManager.sendMessage(sender, row);
            }
        }
    }


    public List<GPlayer> getTopPlayersByPoints(int count) {
        return limitList(getTopPlayersByPoints(), count);
    }


    public List<GPlayer> getTopPlayersByPoints() {
        final List<GPlayer> playerList = new ArrayList<>(players.values());

        Collections.sort(playerList, new Comparator<GPlayer>() {
            public int compare(GPlayer o1, GPlayer o2) {
                return o2.getPoints() - o1.getPoints();
            }
        });

        return playerList;
    }


    public List<GPlayer> getTopPlayersByKDR() {
        final List<GPlayer> playerList = new ArrayList<>(players.values());

        Collections.sort(playerList, new Comparator<GPlayer>() {
            public int compare(GPlayer p1, GPlayer p2) {
                if (p1.getKillDeathRate() > p2.getKillDeathRate()) {
                    return -1;
                }
                if (p1.getKillDeathRate() < p2.getKillDeathRate()) {
                    return 1;
                }
                return 0;
            }
        });

        return playerList;
    }


    public List<GPlayer> getTopPlayersByKDR(int count) {
        return limitList(getTopPlayersByKDR(), count);
    }


    public boolean isVanished(Player player) {
        return player != null
                && (plugin.getDependencyManager().isEnabled(Dependency.VANISHNOPACKET) && plugin.getDependencyManager().get(Dependency.VANISHNOPACKET, VanishPlugin.class).getManager().isVanished(player)
                || plugin.getDependencyManager().isEnabled(Dependency.ESSENTIALS) && plugin.getDependencyManager().get(Dependency.ESSENTIALS, Essentials.class).getVanishedPlayers().contains(player.getName()));

    }


    public static <T> List<T> limitList(List<T> list, int limit) {
        return list.subList(0, list.size() < limit ? list.size() : limit);
    }


    public ResourceManager<GPlayer> getResourceManager() {
        return plugin.getStorage().getResourceManager(GPlayer.class);
    }
}
