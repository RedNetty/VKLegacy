package me.kayaba.guilds.api.util;

import me.kayaba.guilds.api.basic.*;
import org.bukkit.entity.*;

import java.util.*;

public interface ChatMessage {

    void send();


    void send(Player player);


    void send(GPlayer nPlayer);


    void send(Guild guild);


    void sendToGuilds(List<Guild> guildList);


    void sendToPlayers(List<Player> playerList);


    void sendToKayabaPlayers(List<GPlayer> playerList);


    Player getPlayer();


    String getMessage();


    String getFormat();


    PreparedTag getTag();


    boolean isReportToConsole();


    void setMessage(String message);


    void setFormat(String format);


    void setTag(PreparedTag tag);


    void setReportToConsole(boolean reportToConsole);


    void report();
}
