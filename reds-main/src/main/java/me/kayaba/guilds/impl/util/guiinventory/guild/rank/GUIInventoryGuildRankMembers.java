package me.kayaba.guilds.impl.util.guiinventory.guild.rank;


import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.impl.util.guiinventory.guild.player.*;

import java.util.*;

public class GUIInventoryGuildRankMembers extends GUIInventoryGuildPlayersList {
    private final GRank rank;


    public GUIInventoryGuildRankMembers(Guild guild, GRank rank) {
        super(guild);
        this.rank = rank;
    }

    @Override
    public void generateContent() {
        final List<GPlayer> list = new ArrayList<>();

        if (rank.isGeneric()) {
            list.addAll(getMembers(guild, rank));
        } else {
            list.addAll(rank.getMembers());
        }

        generateContent(list);
    }


    public static List<GPlayer> getMembers(Guild guild, GRank rank) {
        final List<GPlayer> list = new ArrayList<>();

        for (GPlayer nPlayer : rank.getMembers()) {
            if (guild.isMember(nPlayer)) {
                list.add(nPlayer);
            }
        }

        return list;
    }
}
