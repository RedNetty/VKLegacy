package me.kayaba.guilds.enums;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.util.reflect.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.util.reflect.*;
import org.apache.commons.lang.*;

import java.util.*;

@SuppressWarnings("unchecked")
public abstract class Config {
    public static ConfigWrapper MYSQL_HOST;
    public static ConfigWrapper MYSQL_PORT;
    public static ConfigWrapper MYSQL_USERNAME;
    public static ConfigWrapper MYSQL_PASSWORD;
    public static ConfigWrapper MYSQL_DATABASE;
    public static ConfigWrapper MYSQL_PREFIX;
    public static ConfigWrapper DATASTORAGE_PRIMARY;
    public static ConfigWrapper DATASTORAGE_SECONDARY;
    public static ConfigWrapper LANG_NAME;
    public static ConfigWrapper LANG_OVERRIDEESSENTIALS;
    public static ConfigWrapper DEBUG;
    public static ConfigWrapper DELETEINVALID;
    public static ConfigWrapper BOSSBAR_ENABLED;
    public static ConfigWrapper BOSSBAR_RAIDBAR_ENABLED;
    public static ConfigWrapper BOSSBAR_RAIDBAR_STYLE;
    public static ConfigWrapper BOSSBAR_RAIDBAR_COLOR;
    public static ConfigWrapper TAGAPI_ENABLED;
    public static ConfigWrapper SIGNGUI_ENABLED;
    public static ConfigWrapper POINTSBELOWNAME;
    public static ConfigWrapper HOLOGRAPHICDISPLAYS_ENABLED;
    public static ConfigWrapper HOLOGRAPHICDISPLAYS_REFRESH;
    public static ConfigWrapper ADVANCEDENTITYUSE;
    public static ConfigWrapper LIVEREGENERATION_REGENTIME;
    public static ConfigWrapper CHAT_TAG_CHAT;
    public static ConfigWrapper CHAT_TAG_SCOREBOARD;
    public static ConfigWrapper CHAT_LEADERPREFIX;
    public static ConfigWrapper CHAT_DISPLAYNAMETAGS;
    public static ConfigWrapper CHAT_ADVANCED;
    public static ConfigWrapper CHAT_CONFIRMTIMEOUT;
    public static ConfigWrapper CHAT_TAGCOLORS_NEUTRAL;
    public static ConfigWrapper CHAT_TAGCOLORS_ALLY;
    public static ConfigWrapper CHAT_TAGCOLORS_WAR;
    public static ConfigWrapper CHAT_TAGCOLORS_GUILD;
    public static ConfigWrapper CHAT_ALLY_PREFIX;
    public static ConfigWrapper CHAT_ALLY_ENABLED;
    public static ConfigWrapper CHAT_ALLY_LEADERPREFIX;
    public static ConfigWrapper CHAT_ALLY_FORMAT;
    public static ConfigWrapper CHAT_GUILD_PREFIX;
    public static ConfigWrapper CHAT_GUILD_ENABLED;
    public static ConfigWrapper CHAT_GUILD_LEADERPREFIX;
    public static ConfigWrapper CHAT_GUILD_FORMAT;
    public static ConfigWrapper CHAT_TOP_AMOUNT;
    public static ConfigWrapper CHAT_TOP_FORMAT;
    public static ConfigWrapper CHAT_TOP_POINTS;
    public static ConfigWrapper GUILD_DISABLEDWORLDS;
    public static ConfigWrapper GUILD_CREATEPROTECTION;
    public static ConfigWrapper GUILD_START_POINTS;
    public static ConfigWrapper GUILD_START_MONEY;
    public static ConfigWrapper GUILD_SLOTS_START;
    public static ConfigWrapper GUILD_SLOTS_MAX;
    public static ConfigWrapper GUILD_LIVES_START;
    public static ConfigWrapper GUILD_LIVES_MAX;
    public static ConfigWrapper GUILD_FROMSPAWN;
    public static ConfigWrapper GUILD_DEFAULTPVP;
    public static ConfigWrapper GUILD_STRINGCHECK_ENABLED;
    public static ConfigWrapper GUILD_STRINGCHECK_REGEX;
    public static ConfigWrapper GUILD_STRINGCHECK_PATTERN;
    public static ConfigWrapper GUILD_STRINGCHECK_REGEXPATTERN;
    public static ConfigWrapper GUILD_SETTINGS_TAG_MIN;
    public static ConfigWrapper GUILD_SETTINGS_TAG_MAX;
    public static ConfigWrapper GUILD_SETTINGS_NAME_MIN;
    public static ConfigWrapper GUILD_SETTINGS_NAME_MAX;
    public static ConfigWrapper GUILD_KILLPOINTS;
    public static ConfigWrapper GUILD_DEATHPOINTS;
    public static ConfigWrapper GUILD_EFFECT_DURATION;
    public static ConfigWrapper GUILD_EFFECT_LIST;
    public static ConfigWrapper GUILD_PLAYERPOINTS;
    public static ConfigWrapper RANK_MAXAMOUNT;
    public static ConfigWrapper RANK_GUI;
    public static ConfigWrapper RANK_DEFAULTRANKS;
    public static ConfigWrapper RAID_ENABLED;
    public static ConfigWrapper RAID_TIMEREST;
    public static ConfigWrapper RAID_TIMEINACTIVE;
    public static ConfigWrapper RAID_MINONLINE;
    public static ConfigWrapper RAID_POINTSTAKE;
    public static ConfigWrapper RAID_MULTIPLER;
    public static ConfigWrapper RAID_PVP_BONUSPERCENT_MONEY;
    public static ConfigWrapper RAID_PVP_BONUSPERCENT_POINTS;
    public static ConfigWrapper LIVEREGENERATION_TASKINTERVAL;
    public static ConfigWrapper SAVEINTERVAL;
    public static ConfigWrapper CLEANUP_ENABLED;
    public static ConfigWrapper CLEANUP_INACTIVETIME;
    public static ConfigWrapper CLEANUP_INTERVAL;
    public static ConfigWrapper CLEANUP_STARTUPDELAY;
    public static ConfigWrapper LEADERBOARD_GUILD_ROWS;
    public static ConfigWrapper VAULT_ENABLED;
    public static ConfigWrapper VAULT_ITEM;
    public static ConfigWrapper VAULT_HOLOGRAM_ENABLED;
    public static ConfigWrapper VAULT_HOLOGRAM_LINES;
    public static ConfigWrapper VAULT_DENYRELATIVE;
    public static ConfigWrapper USETITLES;
    public static ConfigWrapper REGION_AUTOREGION;
    public static ConfigWrapper REGION_MINSIZE;
    public static ConfigWrapper REGION_MAXSIZE;
    public static ConfigWrapper REGION_MINDISTANCE;
    public static ConfigWrapper REGION_MAXAMOUNT;
    public static ConfigWrapper REGION_TOOL;
    public static ConfigWrapper REGION_FLUIDPROTECT;
    public static ConfigWrapper REGION_WORLDGUARD;
    public static ConfigWrapper REGION_BLOCKEDCMDS;
    public static ConfigWrapper REGION_WATERFLOW;
    public static ConfigWrapper REGION_ALLYINTERACT;
    public static ConfigWrapper REGION_BORDERPARTICLES;
    public static ConfigWrapper REGION_DENYINTERACT;
    public static ConfigWrapper REGION_DENYUSE;
    public static ConfigWrapper REGION_DENYMOBDAMAGE;
    public static ConfigWrapper REGION_DENYRIDING;
    public static ConfigWrapper REGION_MATERIALS_HIGHLIGHT_REGION_CORNER;
    public static ConfigWrapper REGION_MATERIALS_HIGHLIGHT_REGION_BORDER;
    public static ConfigWrapper REGION_MATERIALS_HIGHLIGHT_RESIZE_CORNER;
    public static ConfigWrapper REGION_MATERIALS_HIGHLIGHT_RESIZE_BORDER;
    public static ConfigWrapper REGION_MATERIALS_SELECTION_VALID_CORNER;
    public static ConfigWrapper REGION_MATERIALS_SELECTION_VALID_BORDER;
    public static ConfigWrapper REGION_MATERIALS_SELECTION_INVALID_CORNER;
    public static ConfigWrapper REGION_MATERIALS_SELECTION_INVALID_BORDER;
    public static ConfigWrapper REGION_MATERIALS_RESIZE_CORNER;
    public static ConfigWrapper REGION_MATERIALS_RESIZE_BORDER;
    public static ConfigWrapper KILLING_STARTPOINTS;
    public static ConfigWrapper KILLING_RANKPERCENT;
    public static ConfigWrapper KILLING_COOLDOWN;
    public static ConfigWrapper KILLING_MONEYFORKILL;
    public static ConfigWrapper KILLING_MONEYFORREVENGE;
    public static ConfigWrapper TABLIST_ENABLED;
    public static ConfigWrapper TABLIST_REFRESH;
    public static ConfigWrapper TABLIST_HEADER;
    public static ConfigWrapper TABLIST_FOOTER;
    public static ConfigWrapper TABLIST_TEXTURE;
    public static ConfigWrapper TABLIST_TOPROW_PLAYERS_POINTS;
    public static ConfigWrapper TABLIST_TOPROW_PLAYERS_KDR;
    public static ConfigWrapper TABLIST_TOPROW_GUILDS;
    public static ConfigWrapper TABLIST_SCHEME;

    private static final Map<String, ConfigWrapper> wrapperMap = new HashMap<>();

    static {
        for (FieldAccessor<ConfigWrapper> field : Reflections.getFields(Config.class, ConfigWrapper.class)) {
            ConfigWrapper wrapper = field.get();
            String path = StringUtils.replace(field.getName(), "_", ".").toLowerCase();

            if (wrapper == null) {
                wrapper = new ConfigWrapperImpl(path, true);
                field.set(wrapper);
            } else {
                wrapper.setPath(path);
            }

            if (wrapper.getName().startsWith("MYSQL_")) {
                wrapper.setFixColors(false);
            }

            wrapperMap.put(field.getName(), wrapper);
        }
    }


    public static ConfigWrapper valueOf(String name) {
        ConfigWrapper wrapper = wrapperMap.get(name);

        if (wrapper == null) {
            throw new IllegalArgumentException("Missing Config wrapper: " + name);
        }

        return wrapper;
    }


    public static ConfigWrapper fromPath(String path) {
        return wrapperMap.get(StringUtils.replace(path, ".", "_").toUpperCase());
    }


    public static ConfigWrapper[] values() {
        return wrapperMap.values().toArray(new ConfigWrapper[wrapperMap.size()]);
    }
}
