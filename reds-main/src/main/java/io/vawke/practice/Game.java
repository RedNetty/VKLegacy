package io.vawke.practice;

import com.google.common.collect.Maps;
import io.vawke.practice.data.Registry;
import io.vawke.practice.guild.GuildRegistry;
import io.vawke.practice.guild.factory.GuildCommandFactory;
import io.vawke.practice.guild.factory.GuildEventFactory;
import io.vawke.practice.util.IOUtil;
import lombok.Getter;
import me.bpweber.practiceserver.PracticeServer;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

import java.io.File;
import java.util.HashMap;

/**
 * Jaxon's FEATHER Dungeon Realms practice server code.
 * <p>
 * Owner ->
 * @author Jaxon (Red29 - gay, gay, gay) - 2017.
 *
 * Original Author/Developer ->
 * @author Giovanni (VawkeNetty - development@vawke.io) - 2017.
 */
public class Game {

    @Getter
    private static Game game;

    @Getter
    private static ConsoleCommandSender logger;

    @Getter
    private static PracticeServer practiceServer;

    private HashMap<String, Registry> dataRegistries;

    public Game(PracticeServer practiceServer) {
        this.practiceServer = practiceServer;
        game = this;

        logger = practiceServer.getServer().getConsoleSender();
        this.dataRegistries = Maps.newHashMap();

        this.initIO();

        this.dataRegistries.put("guilds", new GuildRegistry());
        this.dataRegistries.values().forEach(Registry::init);

        this.directGuildRegistry().readFromPath(practiceServer.getDataFolder() + "/guilds");

        new GuildCommandFactory().build();
        new GuildEventFactory().build();
    }

    public void shutdownHook() {
        this.directGuildRegistry().quitAndSave();
    }

    public void initIO() {
        new IOUtil();
        if (!practiceServer.getDataFolder().exists()) {
            practiceServer.getDataFolder().mkdir();
        }
        File guildRoot = new File(practiceServer.getDataFolder(), "/guilds/");
        if (!guildRoot.exists()) {
            guildRoot.mkdir();
        }
    }

    public GuildRegistry directGuildRegistry() {
        return (GuildRegistry) this.dataRegistries.get("guilds");
    }

    public static void log(String par1) {
        logger.sendMessage(ChatColor.translateAlternateColorCodes('&', par1));
    }
}
