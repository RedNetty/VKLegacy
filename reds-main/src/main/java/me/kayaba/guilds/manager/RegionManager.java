package me.kayaba.guilds.manager;

import com.sk89q.worldguard.bukkit.*;
import com.sk89q.worldguard.protection.flags.*;
import com.sk89q.worldguard.protection.regions.*;
import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.event.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.runnable.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.reflect.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;

import java.awt.*;
import java.awt.geom.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

public class RegionManager {
    private static final PracticeServer plugin = PracticeServer.getInstance();
    public static Object WORLDGUARD_FLAG;


    public static GRegion get(Location location) {
        int x = location.getBlockX();
        int z = location.getBlockZ();

        for (GRegion region : plugin.getRegionManager().getRegions()) {
            if (!location.getWorld().equals(region.getWorld())) {
                continue;
            }

            Location c1 = region.getCorner(0);
            Location c2 = region.getCorner(1);

            if ((x >= c1.getBlockX() && x <= c2.getBlockX()) || (x <= c1.getBlockX() && x >= c2.getBlockX())) {
                if ((z >= c1.getBlockZ() && z <= c2.getBlockZ()) || (z <= c1.getBlockZ() && z >= c2.getBlockZ())) {
                    return region;
                }
            }
        }

        return null;
    }


    public static GRegion get(Block block) {
        return get(block.getLocation());
    }


    public static GRegion get(Entity entity) {
        return get(entity.getLocation());
    }


    public static GRegion get(GPlayer nPlayer) {
        if (nPlayer.isOnline()) {
            return get(nPlayer.getPlayer());
        }

        return null;
    }


    public Collection<GRegion> getRegions() {
        Collection<GRegion> regions = new HashSet<>();

        for (Guild guild : plugin.getGuildManager().getGuilds()) {
            if (guild.hasRegion()) {
                regions.addAll(guild.getRegions());
            }
        }

        return regions;
    }


    public void load() {
        for (Guild guild : plugin.getGuildManager().getGuilds()) {
            guild.setRegions(new ArrayList<GRegion>());
        }

        getResourceManager().load();

        LoggerUtils.info("Loaded " + getRegions().size() + " regions.");
    }


    public void save(GRegion region) {
        getResourceManager().save(region);
    }


    public void save() {
        long startTime = System.nanoTime();
        int count = getResourceManager().executeSave() + getResourceManager().save(getRegions());
        LoggerUtils.info("Regions data saved in " + TimeUnit.MILLISECONDS.convert((System.nanoTime() - startTime), TimeUnit.NANOSECONDS) / 1000.0 + "s (" + count + " regions)");

        startTime = System.nanoTime();
        count = getResourceManager().executeRemoval();
        LoggerUtils.info("Regions removed in " + TimeUnit.MILLISECONDS.convert((System.nanoTime() - startTime), TimeUnit.NANOSECONDS) / 1000.0 + "s (" + count + " regions)");
    }


    public void remove(GRegion region) {
        getResourceManager().addToRemovalQueue(region);

        if (region.getGuild() != null) {
            region.getGuild().removeRegion(region);
        }
    }


    public RegionValidity checkRegionSelect(GPlayer nPlayer, Location l1, Location l2) {
        return RegionValidity.VALID; // CODE LATERRRR
    }


    public RegionValidity checkRegionSelect(RegionSelection selection) {
        Location l1 = selection.getCorner(0);
        Location l2 = selection.getCorner(1);

        int difX = selection.getWidth();
        int difZ = selection.getLength();

        int minSize = Config.REGION_MINSIZE.getInt();
        int maxSize = Config.REGION_MAXSIZE.getInt();

        List<GRegion> regionsInsideArea = getRegionsInsideArea(l1, l2);
        List<Guild> guildsTooClose = getGuildsTooClose(selection);

        if (difX < minSize || difZ < minSize) {
            return RegionValidity.TOOSMALL;
        }

        if (difX > maxSize || difZ > maxSize) {
            return RegionValidity.TOOBIG;
        }

        if (!regionsInsideArea.isEmpty()) {
            if (selection.getType() != RegionSelection.Type.RESIZE) {
                return RegionValidity.OVERLAPS;
            }

            for (GRegion region : regionsInsideArea) {
                if (!region.equals(selection.getSelectedRegion())) {
                    return RegionValidity.OVERLAPS;
                }
            }
        }

        if (!guildsTooClose.isEmpty()) {
            for (Guild guild : guildsTooClose) {
                if (!guild.isMember(selection.getPlayer())) {
                    return RegionValidity.TOOCLOSE;
                }
            }
        }

        if (Config.REGION_WORLDGUARD.getBoolean() && !checkWorldGuardValidity(selection)) {
            return RegionValidity.WORLDGUARD;
        }

        return RegionValidity.VALID;
    }


    public List<GRegion> getRegionsInsideArea(Location l1, Location l2) {
        final List<GRegion> list = new ArrayList<>();
        int x1 = l1.getBlockX();
        int x2 = l2.getBlockX();
        int z1 = l1.getBlockZ();
        int z2 = l2.getBlockZ();

        boolean i1;
        boolean i2;
        boolean i3;
        boolean i4;

        boolean ov1;
        boolean ov2;
        boolean overlaps;

        for (GRegion region : getRegions()) {
            Location c1 = region.getCorner(0);
            Location c2 = region.getCorner(1);


            i1 = (c1.getBlockX() <= x1 && c1.getBlockX() >= x2) || (c1.getBlockX() >= x1 && c1.getBlockX() <= x2);
            i2 = (c1.getBlockZ() <= z1 && c1.getBlockZ() >= z2) || (c1.getBlockZ() >= z1 && c1.getBlockZ() <= z2);


            i3 = (c2.getBlockX() <= x1 && c2.getBlockX() >= x2) || (c2.getBlockX() >= x1 && c2.getBlockX() <= x2);
            i4 = (c2.getBlockZ() <= z1 && c2.getBlockZ() >= z2) || (c2.getBlockZ() >= z1 && c2.getBlockZ() <= z2);

            ov1 = i1 && i2;
            ov2 = i3 && i4;

            overlaps = ov1 || ov2;

            if (overlaps) {
                list.add(region);
            }
        }

        return list;
    }


    public boolean canInteract(Player player, Location location) {
        GRegion region = get(location);
        GPlayer nPlayer = PlayerManager.getPlayer(player);
        return region == null || nPlayer.getPreferences().getBypass() || (nPlayer.hasGuild() && region.getGuild().isMember(nPlayer));
    }


    public boolean canInteract(Player player, Block block) {
        return canInteract(player, block.getLocation());
    }


    public boolean canInteract(Player player, Entity entity) {
        return canInteract(player, entity.getLocation());
    }


    public List<Guild> getGuildsTooClose(RegionSelection selection) {
        final List<Guild> list = new ArrayList<>();
        int radius1 = Math.round((int) Math.sqrt((int) (Math.pow(selection.getWidth(), 2) + Math.pow(selection.getLength(), 2))) / 2);
        int min = radius1 + Config.REGION_MINDISTANCE.getInt();
        Location centerLocation = selection.getCenter();

        for (Guild guildLoop : plugin.getGuildManager().getGuilds()) {
            if (guildLoop.hasRegion()) {
                for (GRegion region : guildLoop.getRegions()) {
                    if (!region.getWorld().equals(selection.getWorld())) {
                        continue;
                    }

                    int radius2 = region.getDiagonal() / 2;

                    double distance = centerLocation.distance(region.getCenter());
                    if (distance < min + radius2) {
                        list.add(guildLoop);
                    }
                }
            } else {
                if (!guildLoop.getHome().getWorld().equals(selection.getWorld())) {
                    continue;
                }

                Location homeLocation = guildLoop.getHome().clone();
                homeLocation.setY(0);
                double distance = centerLocation.distance(homeLocation);
                if (distance < min) {
                    list.add(guildLoop);
                }
            }
        }

        return list;
    }


    public void playerEnteredRegion(Player player, GRegion region) {
        PlayerEnterRegionEvent regionEvent = new PlayerEnterRegionEvent(player, region);
        plugin.getServer().getPluginManager().callEvent(regionEvent);

        if (regionEvent.isCancelled()) {
            return;
        }

        if (region == null) {
            return;
        }

        GPlayer nPlayer = PlayerManager.getPlayer(player);

        if (plugin.getPlayerManager().isVanished(player)) {
            return;
        }


        if (Config.REGION_BORDERPARTICLES.getBoolean()) {
            List<Block> blocks = RegionUtils.getBorderBlocks(region);
            for (Block block : blocks) {
                block.getLocation().setY(block.getLocation().getY() + 1);
                block.getWorld().playEffect(block.getLocation(), Effect.SMOKE, 100);
            }
        }


        boolean sameGuildRegion = nPlayer.isAtRegion() && region.getGuild().ownsRegion(region);
        Map<VarKey, String> vars = new HashMap<>();
        if (!sameGuildRegion) {
            vars.put(VarKey.GUILD_NAME, region.getGuild().getName());
            vars.put(VarKey.PLAYER_NAME, player.getName());
            Message.CHAT_REGION_ENTERED.clone().vars(vars).send(player);
        }


        nPlayer.setAtRegion(region);

        if (!region.getGuild().isMember(nPlayer)) {
            checkRaidInit(nPlayer);

            if (!sameGuildRegion) {
                Message.CHAT_REGION_NOTIFYGUILD_ENTERED.clone().vars(vars).broadcast(region.getGuild());
            }
        }


        Entity vehicle = player.getVehicle();
        if (vehicle != null && vehicle instanceof Vehicle) {
            nPlayer.addVehicle((Vehicle) vehicle);
        }
    }


    public void playerExitedRegion(Player player) {
        GPlayer nPlayer = PlayerManager.getPlayer(player);
        GRegion region = nPlayer.getAtRegion();

        PlayerExitRegionEvent regionEvent = new PlayerExitRegionEvent(player, region);
        plugin.getServer().getPluginManager().callEvent(regionEvent);

        if (regionEvent.isCancelled() || region == null) {
            return;
        }

        Guild guild = region.getGuild();

        nPlayer.setAtRegion(null);
        Message.CHAT_REGION_EXITED.clone().setVar(VarKey.GUILD_NAME, region.getGuild().getName()).send(player);

        if (nPlayer.hasGuild() && nPlayer.getGuild().isWarWith(guild) && guild.isRaid()) {
            guild.getRaid().removePlayerOccupying(nPlayer);
        }
    }


    public void checkRaidInit(GPlayer nPlayer) {
        if (!Config.RAID_ENABLED.getBoolean() || !nPlayer.hasGuild() || !nPlayer.isAtRegion()) {
            return;
        }

        if (!nPlayer.getAtRegion().getGuild().getHome().getWorld().equals(nPlayer.getPlayer().getLocation().getWorld())
                || nPlayer.getAtRegion().getGuild().getHome().distance(nPlayer.getPlayer().getLocation()) > nPlayer.getAtRegion().getDiagonal()) {
            return;
        }

        Guild guildDefender = nPlayer.getAtRegion().getGuild();

        if (nPlayer.getGuild().isWarWith(guildDefender)) {
            if (guildDefender.isRaid()) {
                nPlayer.setPartRaid(guildDefender.getRaid());
                guildDefender.getRaid().addPlayerOccupying(nPlayer);
            } else {
                if (NumberUtils.systemSeconds() - Config.RAID_TIMEREST.getSeconds() > guildDefender.getTimeRest()) {
                    if (guildDefender.getOnlinePlayers().size() >= Config.RAID_MINONLINE.getInt() || guildDefender.getOnlinePlayers().size() == guildDefender.getPlayers().size()) {
                        if (NumberUtils.systemSeconds() - guildDefender.getTimeCreated() > Config.GUILD_CREATEPROTECTION.getSeconds()) {
                            guildDefender.createRaid(nPlayer.getGuild());
                            guildDefender.getRaid().addPlayerOccupying(nPlayer);

                            if (!RunnableRaid.isRaidRunnableRunning()) {
                                new RunnableRaid().schedule();
                            }
                        } else {
                            Message.CHAT_RAID_PROTECTION.send(nPlayer);
                        }
                    }
                } else {
                    final long timeWait = Config.RAID_TIMEREST.getSeconds() - (NumberUtils.systemSeconds() - guildDefender.getTimeRest());

                    Message.CHAT_RAID_RESTING.clone().setVar(VarKey.GUILD_TIME_REST, StringUtils.secondsToString(timeWait)).send(nPlayer);
                }
            }
        }
    }


    public void checkAtRegionChange() {
        for (GPlayer nPlayer : plugin.getPlayerManager().getOnlinePlayers()) {
            checkAtRegionChange(nPlayer);
        }
    }


    public void checkAtRegionChange(GPlayer nPlayer) {
        GRegion region = get(nPlayer);

        if (nPlayer.isAtRegion()) {
            if (region == null || plugin.getPlayerManager().isVanished(nPlayer.getPlayer())) {
                playerExitedRegion(nPlayer.getPlayer());
            }
        } else if (region != null && !plugin.getPlayerManager().isVanished(nPlayer.getPlayer())) {
            playerEnteredRegion(nPlayer.getPlayer(), region);
        }
    }


    public ResourceManager<GRegion> getResourceManager() {
        return plugin.getStorage().getResourceManager(GRegion.class);
    }


    private boolean checkWorldGuardValidity(RegionSelection selection) {
        if (!plugin.getDependencyManager().isEnabled(Dependency.WORLDGUARD)) {
            return true;
        }

        WorldGuardPlugin worldGuard = plugin.getDependencyManager().get(Dependency.WORLDGUARD, WorldGuardPlugin.class);
        Area selectionArea = new Area(new Rectangle(
                selection.getCorner(selection.getCorner(0).getBlockX() < selection.getCorner(1).getBlockX() ? 0 : 1).getBlockX(),
                selection.getCorner(selection.getCorner(0).getBlockZ() < selection.getCorner(1).getBlockZ() ? 0 : 1).getBlockZ(),
                selection.getWidth(),
                selection.getLength())
        );

        for (ProtectedRegion region : worldGuard.getRegionManager(selection.getWorld()).getRegions().values()) {
            if (region.getFlag((Flag) RegionManager.WORLDGUARD_FLAG) == StateFlag.State.ALLOW) {
                continue;
            }

            Area regionArea = RegionUtils.toArea(region);

            regionArea.intersect(selectionArea);
            if (!regionArea.isEmpty()) {
                return false;
            }
        }

        return true;
    }


    public void createWorldGuardFlag() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
        if (WORLDGUARD_FLAG != null) {
            throw new IllegalArgumentException("WorldGuard flag has been already created");
        }

        Class<?> stateFlagClass = Reflections.getClass("com.sk89q.worldguard.protection.flags.StateFlag");
        Class<?> regionGroupClass = Reflections.getClass("com.sk89q.worldguard.protection.flags.RegionGroup");
        WORLDGUARD_FLAG = stateFlagClass.getConstructor(
                String.class,
                boolean.class,
                regionGroupClass
        ).newInstance(
                "ngregion",
                false,
                null
        );
    }
}
