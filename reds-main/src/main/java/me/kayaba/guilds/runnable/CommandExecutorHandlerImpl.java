package me.kayaba.guilds.runnable;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.scheduler.*;

public class CommandExecutorHandlerImpl implements CommandExecutorHandler {
    private final CommandSender sender;
    private final CommandWrapper command;
    private final String[] args;
    private State state = State.WAITING;
    private final BukkitTask bukkitTask;
    private Resource executorVariable;


    public CommandExecutorHandlerImpl(CommandWrapper command, CommandSender sender, String[] args) {
        this.command = command;
        this.sender = sender;
        this.args = args;

        if (command.hasFlag(CommandWrapper.Flag.CONFIRM)) {
            bukkitTask = Bukkit.getScheduler().runTaskLater(PracticeServer.getInstance(), this, Config.CHAT_CONFIRMTIMEOUT.getSeconds() * 20);
        } else {
            bukkitTask = null;
        }
    }

    @Override
    public void execute() {
        if (getState() == State.CONFIRMED || !command.hasFlag(CommandWrapper.Flag.CONFIRM)) {
            command.executorVariable(executorVariable);
            command.execute(sender, args);
            PlayerManager.getPlayer(sender).removeCommandExecutorHandler();
        }
    }

    @Override
    public void cancel() {
        state = State.CANCELED;
        bukkitTask.cancel();
        PlayerManager.getPlayer(sender).removeCommandExecutorHandler();
    }

    @Override
    public void confirm() {
        if (state != State.CANCELED) {
            state = State.CONFIRMED;
            execute();
        }
    }

    @Override
    public void run() {
        if (state == State.WAITING) {
            cancel();
            Message.CHAT_CONFIRM_TIMEOUT.send(sender);
        }
    }

    @Override
    public CommandWrapper getCommand() {
        return command;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public Resource getExecutorVariable() {
        return executorVariable;
    }

    @Override
    public void executorVariable(Resource executorVariable) {
        this.executorVariable = executorVariable;
    }
}
