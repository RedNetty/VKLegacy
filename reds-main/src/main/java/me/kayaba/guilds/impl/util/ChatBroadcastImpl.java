package me.kayaba.guilds.impl.util;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.entity.*;

import java.util.*;

public class ChatBroadcastImpl implements ChatBroadcast {
    private final MessageWrapper message;
    private final Map<Integer, PreparedTag> preparedTagMap = new HashMap<>();


    public ChatBroadcastImpl(MessageWrapper message) {
        this.message = message.clone();
    }

    @Override
    public void send() {
        for (Player player : CompatibilityUtils.getOnlinePlayers()) {
            for (Map.Entry<Integer, PreparedTag> entry : preparedTagMap.entrySet()) {
                GPlayer nPlayer = PlayerManager.getPlayer(player);
                PreparedTag tag = entry.getValue();

                tag.setTagColorFor(nPlayer);
                message.setVar(VarKey.valueOf("TAG" + entry.getKey()), tag.get());
            }

            message.send(player);
        }
    }

    @Override
    public void setTag(Integer index, PreparedTag preparedTag) {
        if (index > 10 || index < 1) {
            throw new IllegalArgumentException("Index must be between 1 and 10");
        }

        preparedTagMap.put(index, preparedTag);
    }

    @Override
    public PreparedTag getTag(Integer index) {
        return preparedTagMap.get(index);
    }

    @Override
    public MessageWrapper getMessage() {
        return message;
    }
}
