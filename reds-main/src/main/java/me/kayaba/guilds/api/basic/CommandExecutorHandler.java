package me.kayaba.guilds.api.basic;

import me.kayaba.guilds.api.storage.*;

public interface CommandExecutorHandler extends Runnable {
    enum State {
        WAITING,
        CANCELED,
        CONFIRMED
    }


    void execute();


    void cancel();


    void confirm();


    CommandWrapper getCommand();


    State getState();


    Resource getExecutorVariable();


    void executorVariable(Resource executorVariable);
}
