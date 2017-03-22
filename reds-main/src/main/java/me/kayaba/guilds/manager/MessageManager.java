package me.kayaba.guilds.manager;

import com.earth2me.essentials.Essentials;
import me.bpweber.practiceserver.PracticeServer;
import me.kayaba.guilds.api.basic.Guild;
import me.kayaba.guilds.api.basic.MessageWrapper;
import me.kayaba.guilds.api.util.Title;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.exception.FatalKayabaGuildsException;
import me.kayaba.guilds.impl.versionimpl.v1_9_R1.TitleImpl;
import me.kayaba.guilds.util.CompatibilityUtils;
import me.kayaba.guilds.util.LoggerUtils;
import me.kayaba.guilds.util.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.scanner.ScannerException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageManager {
    private static final PracticeServer plugin = PracticeServer.getInstance();
    private FileConfiguration messages = null;
    public String prefix;
    public ChatColor prefixColor = ChatColor.WHITE;
    private static MessageManager instance;
    private File messagesFile;


    public MessageManager() {
        instance = this;
    }


    public void detectLanguage() throws FileNotFoundException {
        detectEssentialsLocale();
        String lang = Config.LANG_NAME.getString();
        messagesFile = new File(plugin.getDataFolder() + "/lang", lang + ".yml");

        if (!messagesFile.exists()) {
            if (plugin.getResource("lang/" + lang + ".yml") != null) {
                plugin.saveResource("lang/" + lang + ".yml", false);
                LoggerUtils.info("New messages file created: " + lang + ".yml");
            } else {
                throw new FileNotFoundException("Couldn't find language file: " + lang + ".yml");
            }
        }
    }


    public boolean existsFile() {
        return messagesFile.exists();
    }


    public void load() throws FatalKayabaGuildsException {
        setupDirectories();

        try {
            detectLanguage();
            messages = Lang.loadConfiguration(messagesFile);


            restorePrefix();

            prefix = Message.CHAT_PREFIX.get();
            prefixColor = ChatColor.getByChar(ChatColor.getLastColors(prefix).charAt(1));

            LoggerUtils.info("Messages loaded: " + Config.LANG_NAME.getString());
        } catch (ScannerException | IOException e) {
            throw new FatalKayabaGuildsException("Failed to load messages", e);
        }
    }


    public void restorePrefix() {
        String prefix = Message.CHAT_PREFIX.get();
        prefix = StringUtils.removeColors(StringUtils.fixColors(prefix));

        if (!prefix.contains("Guilds")) {
            Message.CHAT_PREFIX.set("&3<&lGUILDS&r&3> ");
            LoggerUtils.info("Prefix restored.");
        }
    }


    private void setupDirectories() {
        File langDir = new File(plugin.getDataFolder(), "lang/");

        if (!langDir.exists() && langDir.mkdir()) {
            LoggerUtils.info("Language dir created");
        }
    }


    public static void detectEssentialsLocale() {
        if (plugin.getDependencyManager().isEnabled(Dependency.ESSENTIALS) && !Config.LANG_OVERRIDEESSENTIALS.getBoolean()) {
            Essentials essentials = plugin.getDependencyManager().get(Dependency.ESSENTIALS, Essentials.class);
            if (essentials.getSettings() == null) {
                return;
            }

            String locale = essentials.getSettings().getLocale();
            if (locale.isEmpty()) {
                locale = "en";
            }

            if (ConfigManager.essentialsLocale.containsKey(locale)) {
                Config.LANG_NAME.set(ConfigManager.essentialsLocale.get(locale));
            }

            LoggerUtils.info("Changed lang to Essentials' locale: " + Config.LANG_NAME.getString());
        }
    }


    public static String getMessagesString(MessageWrapper message) {
        String msg = StringUtils.fixColors(getMessages().getString(message.getPath()));

        return msg == null ? message.getPath() : msg;
    }


    public static FileConfiguration getMessages() {
        return instance.messages;
    }


    public static void sendPrefixMessage(CommandSender sender, String msg) {
        if (!msg.equals("none")) {
            sender.sendMessage(StringUtils.fixColors(instance.prefix + msg));
        }
    }


    public static void sendMessage(CommandSender sender, String msg) {
        if (!msg.equals("none")) {
            sender.sendMessage(instance.prefixColor + StringUtils.fixColors(msg));
        }
    }


    public static void sendMessagesList(CommandSender sender, MessageWrapper message) {
        List<String> list = getMessages().getStringList(message.getPath());
        Map<VarKey, String> vars = message.getVars();
        boolean prefix = message.isPrefix();

        if (list != null) {
            for (String msg : list) {
                if (vars != null) {
                    msg = replaceVarKeyMap(msg, vars);
                }

                if (prefix) {
                    sendPrefixMessage(sender, msg);
                } else {
                    sendMessage(sender, msg);
                }
            }
        }
    }


    public static void sendMessagesMsg(CommandSender sender, MessageWrapper message) {
        String msg = getMessagesString(message);
        msg = replaceVarKeyMap(msg, message.getVars());
        boolean title = message.getTitle();

        if (Config.USETITLES.getBoolean() && title && sender instanceof Player) {
            sendTitle((Player) sender, msg);
        } else {
            if (message.isPrefix()) {
                sendPrefixMessage(sender, msg);
            } else {
                sendMessage(sender, msg);
            }
        }
    }


    public static void sendTitle(Player player, String msg) {
        Title title = null;

        switch (ConfigManager.getServerVersion()) {
            case MINECRAFT_1_8_R1:
                title = new me.kayaba.guilds.impl.versionimpl.v1_8_R1.TitleImpl();
                break;
            case MINECRAFT_1_8_R2:
            case MINECRAFT_1_8_R3:
                title = new me.kayaba.guilds.impl.versionimpl.v1_8_R3.TitleImpl();
                break;
            case MINECRAFT_1_9_R1:
            case MINECRAFT_1_9_R2:
            case MINECRAFT_1_10_R1:
            case MINECRAFT_1_10_R2:
            case MINECRAFT_1_11_R1:
                title = new TitleImpl();
        }

        if (title == null) {
            return;
        }

        title.setSubtitleColor(instance.prefixColor);
        title.setSubtitle(StringUtils.fixColors(msg));
        title.send(player);
    }


    public static void broadcast(List<Player> playerList, MessageWrapper message, Permission permission) {
        for (Player player : playerList) {
            if (permission == null || permission.has(player)) {
                message.send(player);
            }
        }
    }


    public static void broadcast(MessageWrapper message, Permission permission) {
        broadcast(new ArrayList<>(CompatibilityUtils.getOnlinePlayers()), message, permission);
    }


    public static void broadcast(MessageWrapper message) {
        broadcast(message, null);
    }


    public static void broadcast(Guild guild, MessageWrapper message) {
        broadcast(guild.getOnlinePlayers(), message, null);
    }


    public static String replaceVarKeyMap(String msg, Map<VarKey, String> vars) {
        return replaceVarKeyMap(msg, vars, true);
    }


    public static String replaceVarKeyMap(String msg, Map<VarKey, String> vars, boolean usePrefixColor) {
        if (vars != null) {
            for (Map.Entry<VarKey, String> entry : vars.entrySet()) {
                vars.put(entry.getKey(), entry.getValue() + (usePrefixColor ? instance.prefixColor : ""));
            }
        }

        return StringUtils.replaceVarKeyMap(msg, vars);
    }


    public static List<String> replaceVarKeyMap(List<String> list, Map<VarKey, String> vars, boolean usePrefixColor) {
        final List<String> newList = new ArrayList<>();

        for (String string : list) {
            string = replaceVarKeyMap(string, vars, usePrefixColor);
            newList.add(string);
        }

        return newList;
    }


    public void setMessages(YamlConfiguration messages) {
        this.messages = messages;
    }


    public static void set(MessageWrapper message, String string) {
        getMessages().set(message.getPath(), string);
    }


    public static void set(MessageWrapper message, List<String> list) {
        getMessages().set(message.getPath(), list);
    }
}
