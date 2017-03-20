package me.kayaba.guilds.manager;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.*;
import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.event.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.runnable.*;
import me.kayaba.guilds.util.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

import java.util.*;
import java.util.concurrent.*;

public class GuildManager {
    private static final PracticeServer plugin = PracticeServer.getInstance();
    private final Map<String, Guild> guilds = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);


    public static Guild getGuildByName(String name) {
        return plugin.getGuildManager().guilds.get(name);
    }


    public static Guild getGuildByTag(String tag) {
        for (Guild guild : plugin.getGuildManager().getGuilds()) {
            if (StringUtils.removeColors(guild.getTag()).equalsIgnoreCase(tag)) {
                return guild;
            }
        }
        return null;
    }


    public static Guild getGuild(UUID uuid) {
        for (Guild guild : plugin.getGuildManager().getGuilds()) {
            if (guild.getUUID().equals(uuid)) {
                return guild;
            }
        }
        return null;
    }


    public static Guild getGuildFind(String mixed) {
        try {
            return getGuild(UUID.fromString(mixed));
        } catch (IllegalArgumentException e) {
            Guild guild = getGuildByName(mixed);

            if (guild == null) {
                guild = getGuildByTag(mixed);
            }

            if (guild == null) {
                GPlayer nPlayer = PlayerManager.getPlayer(mixed);

                if (nPlayer == null) {
                    return null;
                }

                guild = nPlayer.getGuild();
            }

            return guild;
        }
    }


    public Collection<Guild> getGuilds() {
        return guilds.values();
    }


    public boolean exists(String guildName) {
        return guilds.containsKey(guildName);
    }


    public void load() {
        guilds.clear();
        for (Guild guild : getResourceManager().load()) {
            if (guilds.containsKey(guild.getName())) {
                if (Config.DELETEINVALID.getBoolean()) {
                    getResourceManager().addToRemovalQueue(guild);
                }

                LoggerUtils.error("Removed guild with doubled name (" + guild.getName() + ")");
                continue;
            }

            guilds.put(guild.getName(), guild);
        }

        LoggerUtils.info("Loaded " + guilds.size() + " guilds.");

        loadVaultHolograms();
        LoggerUtils.info("Generated bank holograms.");
    }


    public void add(Guild guild) {
        guilds.put(guild.getName(), guild);
    }


    public void save(Guild guild) {
        getResourceManager().save(guild);
    }


    public void save() {
        long startTime = System.nanoTime();
        int count = getResourceManager().executeSave() + getResourceManager().save(getGuilds());
        LoggerUtils.info("Guilds data saved in " + TimeUnit.MILLISECONDS.convert((System.nanoTime() - startTime), TimeUnit.NANOSECONDS) / 1000.0 + "s (" + count + " guilds)");

        startTime = System.nanoTime();
        count = getResourceManager().executeRemoval();
        LoggerUtils.info("Guilds removed in " + TimeUnit.MILLISECONDS.convert((System.nanoTime() - startTime), TimeUnit.NANOSECONDS) / 1000.0 + "s (" + count + " guilds)");
    }


    public void delete(Guild guild, AbandonCause cause) {
        getResourceManager().addToRemovalQueue(guild);


        for (GRegion region : new ArrayList<>(guild.getRegions())) {
            RegionDeleteEvent event = new RegionDeleteEvent(region, RegionDeleteEvent.Cause.fromGuildAbandonCause(cause));
            plugin.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                plugin.getRegionManager().remove(region);
            }
        }

        guilds.remove(guild.getName());
        guild.destroy(cause);
    }


    public void delete(GuildAbandonEvent event) {
        if (event.isCancelled()) {
            return;
        }

        delete(event.getGuild(), event.getCause());
    }


    public void changeName(Guild guild, String newName) {
        guilds.remove(guild.getName());
        guilds.put(newName, guild);
        guild.setName(newName);
    }


    public List<Raid> getRaidsTakingPart(Guild guild) {
        final List<Raid> list = new ArrayList<>();
        for (Guild raidGuild : getGuilds()) {
            if (raidGuild.isRaid() && raidGuild.getRaid().getGuildAttacker().equals(guild)) {
                list.add(raidGuild.getRaid());
            }
        }

        return list;
    }


    public void postCheck() {
        int i = 0;

        for (Guild guild : new ArrayList<>(getGuilds())) {
            if (!postCheck(guild)) {
                i++;
            }
        }

        LoggerUtils.info("Postcheck finished. Found " + i + " invalid guilds");
    }

    public boolean postCheck(Guild guild) {
        boolean remove = false;
        guild.postSetUp();

        if (((GuildImpl) guild).getLeaderName() != null) {
            LoggerUtils.info("(" + guild.getName() + ") Leader's name is set. Probably leader is null");
        }

        if (guild.getLeader() == null) {
            LoggerUtils.info("(" + guild.getName() + ") Leader is null");
            remove = true;
        }

        if (guild.getPlayers().isEmpty()) {
            LoggerUtils.info("(" + guild.getName() + ") 0 players");
            remove = true;
        }

        if (guild.getHome() == null) {
            LoggerUtils.info("(" + guild.getName() + ") home location is null");
            remove = true;
        }

        if (guild.getId() <= 0 && plugin.getConfigManager().getDataStorageType() != DataStorageType.FLAT) {
            LoggerUtils.info("(" + guild.getName() + ") ID <= 0 !");
            remove = true;
        }

        if (remove) {
            LoggerUtils.info("Unloaded guild " + guild.getName());

            if (Config.DELETEINVALID.getBoolean()) {
                GuildAbandonEvent guildAbandonEvent = new GuildAbandonEvent(guild, AbandonCause.INVALID);
                plugin.getServer().getPluginManager().callEvent(guildAbandonEvent);

                if (!guildAbandonEvent.isCancelled()) {
                    delete(guild, AbandonCause.INVALID);
                    LoggerUtils.info("DELETED guild " + guild.getName());
                }
            } else {
                if (guilds.containsKey(guild.getName())) {
                    guilds.remove(guild.getName());
                }

                guild.destroy(AbandonCause.UNLOADED);
            }

            guild.unload();
            return false;
        }

        return true;
    }


    public List<Guild> getTopGuildsByPoints(int count) {
        final List<Guild> guildsByPoints = new ArrayList<>(guilds.values());

        Collections.sort(guildsByPoints, new Comparator<Guild>() {
            public int compare(Guild o1, Guild o2) {
                return o2.getPoints() - o1.getPoints();
            }
        });

        final List<Guild> guildsLimited = new ArrayList<>();

        int i = 0;
        for (Guild guild : guildsByPoints) {
            guildsLimited.add(guild);

            i++;
            if (i == count) {
                break;
            }
        }

        return guildsLimited;
    }


    public List<Guild> getMostInactiveGuilds() {
        final List<Guild> guildsByInactive = new ArrayList<>(guilds.values());

        Collections.sort(guildsByInactive, new Comparator<Guild>() {
            public int compare(Guild o1, Guild o2) {
                return (int) (NumberUtils.systemSeconds() - o2.getInactiveTime()) - (int) (NumberUtils.systemSeconds() - o1.getInactiveTime());
            }
        });

        return guildsByInactive;
    }


    private void loadVaultHolograms() {
        for (Guild guild : getGuilds()) {
            if (guild.getVaultLocation() != null) {
                appendVaultHologram(guild);
            }
        }
    }


    public boolean isVaultItemStack(ItemStack itemStack) {
        return ItemStackUtils.isSimilar(itemStack, Config.VAULT_ITEM.getItemStack());
    }


    public void appendVaultHologram(Guild guild) {
        if (Config.HOLOGRAPHICDISPLAYS_ENABLED.getBoolean()) {
            if (Config.VAULT_HOLOGRAM_ENABLED.getBoolean()) {
                checkVaultDestroyed(guild);
                if (guild.getVaultLocation() != null) {
                    if (guild.getVaultHologram() == null) {
                        Location hologramLocation = guild.getVaultLocation().clone();
                        hologramLocation.add(0.5, 2, 0.5);
                        Hologram hologram = HologramsAPI.createHologram(plugin, hologramLocation);
                        hologram.getVisibilityManager().setVisibleByDefault(false);
                        for (String hologramLine : Config.VAULT_HOLOGRAM_LINES.getStringList()) {
                            if (hologramLine.startsWith("[ITEM]")) {
                                hologramLine = hologramLine.substring(6);
                                ItemStack itemStack = ItemStackUtils.stringToItemStack(hologramLine);
                                if (itemStack != null) {
                                    hologram.appendItemLine(itemStack);
                                }
                            } else {
                                hologram.appendTextLine(StringUtils.fixColors(hologramLine));
                            }
                        }

                        guild.setVaultHologram(hologram);

                        for (Player player : guild.getOnlinePlayers()) {
                            guild.showVaultHologram(player);
                        }
                    }
                }
            }
        }
    }


    public boolean isVaultBlock(Block block) {
        if (block.getType() == Config.VAULT_ITEM.getItemStack().getType()) {
            for (Guild guild : getGuilds()) {
                checkVaultDestroyed(guild);
                if (guild.getVaultLocation() != null) {
                    if (guild.getVaultLocation().getWorld().equals(block.getWorld())) {
                        if (guild.getVaultLocation().distance(block.getLocation()) < 1) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public static void checkVaultDestroyed(Guild guild) {
        if (guild.getVaultLocation() != null) {
            if (guild.getVaultLocation().getBlock().getType() != Material.CHEST) {
                guild.setVaultLocation(null);
                Hologram hologram = guild.getVaultHologram();

                if (hologram != null) {
                    hologram.delete();
                }

                guild.setVaultHologram(null);
            }
        }
    }


    public void delayedTeleport(Player player, Location location, MessageWrapper message) {
        Runnable task = new RunnableTeleportRequest(player, location, message);
        int delay = GroupManager.getGroup(player) == null ? 0 : GroupManager.getGroup(player).get(GroupImpl.Key.HOME_DELAY);

        if (delay > 0) {
            Map<VarKey, String> vars = new HashMap<>();
            vars.put(VarKey.DELAY, String.valueOf(GroupManager.getGroup(player).get(GroupImpl.Key.HOME_DELAY)));
            PracticeServer.runTaskLater(task, delay, TimeUnit.SECONDS);
            Message.CHAT_DELAYEDTELEPORT.clone().vars(vars).send(player);
        } else {
            task.run();
        }
    }


    public List<String> getTopGuilds() {
        int limit = Config.LEADERBOARD_GUILD_ROWS.getInt();
        int i = 1;

        final List<String> list = new ArrayList<>();
        Map<VarKey, String> vars = new HashMap<>();

        for (Guild guild : plugin.getGuildManager().getTopGuildsByPoints(limit)) {
            vars.clear();
            vars.put(VarKey.GUILD_NAME, guild.getName());
            vars.put(VarKey.N, String.valueOf(i));
            vars.put(VarKey.GUILD_POINTS, String.valueOf(guild.getPoints()));
            list.add(Message.HOLOGRAPHICDISPLAYS_TOPGUILDS_ROW.clone().vars(vars).get());
            i++;
        }

        return list;
    }


    public void cleanInactiveGuilds() {
        int count = 0;

        for (Guild guild : plugin.getGuildManager().getMostInactiveGuilds()) {
            if (NumberUtils.systemSeconds() - guild.getInactiveTime() < Config.CLEANUP_INACTIVETIME.getSeconds()) {
                break;
            }


            GuildAbandonEvent guildAbandonEvent = new GuildAbandonEvent(guild, AbandonCause.INACTIVE);
            plugin.getServer().getPluginManager().callEvent(guildAbandonEvent);

            if (!guildAbandonEvent.isCancelled()) {
                Map<VarKey, String> vars = new HashMap<>();
                vars.put(VarKey.GUILD_NAME, guild.getName());
                Message.BROADCAST_ADMIN_GUILD_CLEANUP.clone().vars(vars).broadcast();
                LoggerUtils.info("Abandoned guild " + guild.getName() + " due to inactivity.");
                count++;

                plugin.getGuildManager().delete(guildAbandonEvent);
            }
        }

        LoggerUtils.info("Guilds cleanup finished, removed " + count + " guilds.");
    }


    public ResourceManager<Guild> getResourceManager() {
        return plugin.getStorage().getResourceManager(Guild.class);
    }
}
