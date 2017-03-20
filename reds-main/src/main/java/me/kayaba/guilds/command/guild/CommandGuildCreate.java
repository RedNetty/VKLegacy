package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.event.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.impl.basic.GroupImpl.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class CommandGuildCreate extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        if (args.length != 2) {
            getCommand().getUsageMessage().send(sender);
            return;
        }

        Player player = (Player) sender;

        String tag = args[0];
        String guildName = args[1];


        guildName = StringUtils.removeColors(guildName);
        tag = StringUtils.removeColors(tag);

        GPlayer nPlayer = PlayerManager.getPlayer(sender);
        Map<VarKey, String> vars = new HashMap<>();

        if (nPlayer.hasGuild()) {
            Message.CHAT_CREATEGUILD_HASGUILD.send(sender);
            return;
        }


        MessageWrapper validName = validName(guildName);
        MessageWrapper validTag = validTag(tag);

        if (validName != null) {
            validName.send(sender);
            return;
        }

        if (validTag != null) {
            validTag.send(sender);
            return;
        }


        if (RegionManager.get(player) != null) {
            Message.CHAT_CREATEGUILD_REGIONHERE.send(sender);
            return;
        }


        Location spawnLocationBedrock = player.getWorld().getSpawnLocation().clone();
        spawnLocationBedrock.setY(0);

        Location playerLocationBedrock = player.getLocation().clone();
        playerLocationBedrock.setY(0);


        if (Config.GUILD_DISABLEDWORLDS.getStringList().contains(player.getWorld().getName())
                || Config.GUILD_DISABLEDWORLDS.getStringList().contains(player.getWorld().getUID().toString())) {
            Message.CHAT_CREATEGUILD_DISABLEDWORLD.send(sender);
            return;
        }


        Group group = GroupManager.getGroup(sender);
        double requiredMoney = GroupManager.getGroup("default").get(Key.CREATE_MONEY);

        if (requiredMoney > 0 && !nPlayer.hasMoney(requiredMoney)) {
            vars.put(VarKey.REQUIREDMONEY, String.valueOf(requiredMoney));
            Message.CHAT_CREATEGUILD_NOTENOUGHMONEY.clone().vars(vars).send(sender);
            return;
        }

        RegionValidity regionValid = RegionValidity.VALID;

        switch (regionValid) {
            case VALID:

                Guild guild;

                if (Config.GUILD_PLAYERPOINTS.getBoolean()) {
                    guild = new GuildIkkaImpl(UUID.randomUUID());
                } else {
                    guild = new GuildImpl(UUID.randomUUID());
                }

                guild.setName(guildName);
                guild.setTag(tag);
                guild.setLeader(nPlayer);
                guild.setHome(player.getLocation());
                guild.addPlayer(nPlayer);
                guild.updateInactiveTime();
                guild.setLives(Config.GUILD_LIVES_START.getInt());
                guild.setPoints(Config.GUILD_START_POINTS.getInt());
                guild.setMoney(Config.GUILD_START_MONEY.getInt());
                guild.setSlots(Config.GUILD_SLOTS_START.getInt());
                guild.setTimeCreated(NumberUtils.systemSeconds());
                guild.setFriendlyPvp(Config.GUILD_DEFAULTPVP.getBoolean());
                guild.setBannerMeta(BannerUtils.getRandomMeta());


                GuildCreateEvent guildCreateEvent = new GuildCreateEvent(guild, (Player) sender);
                plugin.getServer().getPluginManager().callEvent(guildCreateEvent);

                if (!guildCreateEvent.isCancelled()) {

                    plugin.getGuildManager().add(guild);


                    nPlayer.takeMoney(requiredMoney);

                    TagUtils.refresh();
                    TabUtils.refresh(nPlayer);


                    Schematic schematic = GroupManager.getGroup(guild.getLeader().getPlayer()).get(GroupImpl.Key.CREATE_SCHEMATIC);

                    if (schematic != null) {
                        schematic.paste(guild.getHome());
                    }


                    if (Config.VAULT_ENABLED.getBoolean()) {
                        if (!InventoryUtils.containsAtLeast(nPlayer.getPlayer().getInventory(), Config.VAULT_ITEM.getItemStack(), 1)) {
                            nPlayer.getPlayer().getInventory().addItem(Config.VAULT_ITEM.getItemStack());
                        }
                    }


                    if (ConfigManager.getServerVersion().isNewerThan(ConfigManager.ServerVersion.MINECRAFT_1_7_R3)) {
                        ParticleUtils.createSuperKayaba(player);
                    }


                    Message.CHAT_CREATEGUILD_SUCCESS.send(sender);

                    vars.put(VarKey.GUILD_NAME, guild.getName());
                    vars.put(VarKey.PLAYER_NAME, sender.getName());
                    Message.BROADCAST_GUILD_CREATED.clone().vars(vars).broadcast();
                }
                break;
            case OVERLAPS:
                Message.CHAT_REGION_VALIDATION_OVERLAPS.send(sender);
                break;
            case TOOCLOSE:
                Message.CHAT_REGION_VALIDATION_TOOCLOSE.send(sender);
                break;
            case WORLDGUARD:
                Message.CHAT_REGION_VALIDATION_WORLDGUARD.send(nPlayer);
                break;
        }
    }


    public static MessageWrapper validTag(String tag) {
        tag = StringUtils.removeColors(tag);

        if (GuildManager.getGuildByTag(tag) != null) {
            return Message.CHAT_CREATEGUILD_TAGEXISTS;
        }

        if (tag.length() > Config.GUILD_SETTINGS_TAG_MAX.getInt()) {
            return Message.CHAT_CREATEGUILD_TAG_TOOLONG;
        }

        if (StringUtils.removeColors(tag).length() < Config.GUILD_SETTINGS_TAG_MIN.getInt()) {
            return Message.CHAT_CREATEGUILD_TAG_TOOSHORT;
        }

        if (!StringUtils.isStringAllowed(tag)) {
            return Message.CHAT_CREATEGUILD_NOTALLOWEDSTRING;
        }

        return null;
    }


    public static MessageWrapper validName(String name) {
        if (GuildManager.getGuildByName(name) != null) {
            return Message.CHAT_CREATEGUILD_NAMEEXISTS;
        }

        if (name.length() > Config.GUILD_SETTINGS_NAME_MAX.getInt()) {
            return Message.CHAT_CREATEGUILD_NAME_TOOLONG;
        }

        if (name.length() < Config.GUILD_SETTINGS_NAME_MIN.getInt()) {
            return Message.CHAT_CREATEGUILD_NAME_TOOSHORT;
        }

        if (!StringUtils.isStringAllowed(name)) {
            return Message.CHAT_CREATEGUILD_NOTALLOWEDSTRING;
        }

        return null;
    }
}
