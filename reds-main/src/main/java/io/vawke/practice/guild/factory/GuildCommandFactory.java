package io.vawke.practice.guild.factory;

import io.vawke.practice.factory.Factory;
import io.vawke.practice.guild.factory.command.*;
import org.bukkit.command.CommandExecutor;

/**
 * Created by Giovanni on 12-2-2017.
 */
public class GuildCommandFactory extends Factory<GuildCommand> {

    public GuildCommandFactory() {
        this.register(new GuildMotdCommand());
        this.register(new GuildInfoCommand());
        this.register(new GuildTestCommand());
        this.register(new GuildInviteCommand());
        this.register(new GuildDenyCommand());
        this.register(new GuildAcceptCommand());
        this.register(new GuildKickCommand());
    }
}
