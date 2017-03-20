package me.kayaba.guilds.api.util;

import me.kayaba.guilds.api.basic.*;

public interface ChatBroadcast {

    void send();


    void setTag(Integer index, PreparedTag preparedTag);


    PreparedTag getTag(Integer index);


    MessageWrapper getMessage();
}
