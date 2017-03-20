package me.kayaba.guilds.api;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.manager.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.api.util.packet.*;
import me.kayaba.guilds.manager.*;


public interface GuildsAPI {

    RegionManager getRegionManager();


    GuildManager getGuildManager();


    PlayerManager getPlayerManager();


    MessageManager getMessageManager();


    CommandManager getCommandManager();


    ConfigManager getConfigManager();


    GroupManager getGroupManager();

    RankManager getRankManager();


    TaskManager getTaskManager();


    ListenerManager getListenerManager();


    PacketExtension getPacketExtension();


    DependencyManager getDependencyManager();


    ErrorManager getErrorManager();


    Storage getStorage();


    TabList createTabList(ConfigManager.ServerVersion version, GPlayer nPlayer);


    TabList createTabList(GPlayer nPlayer);
}
